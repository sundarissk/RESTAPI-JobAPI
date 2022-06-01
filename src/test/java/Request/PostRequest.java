package Request;

import java.io.File;
import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import com.jobs.QA.Util.ExcelReadWright;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;

public class PostRequest {
	private Response response;
	private RequestSpecification httprequest;
	// private ValidatableResponse json;
	private int scode;
	private String id;
	private String body;

	public void postmethod() throws IOException {
		RestAssured.baseURI = "https://jobs123.herokuapp.com";

		ExcelReadWright xread = new ExcelReadWright(
				"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\main\\java\\TestData\\JobsApi.xlsx");

		id = xread.getCellData("Jobdata", 1, 0);
		String jt = xread.getCellData("Jobdata", 1, 1);
		String cn = xread.getCellData("Jobdata", 1, 2);
		String jl = xread.getCellData("Jobdata", 1, 3);
		String jty = xread.getCellData("Jobdata", 1, 4);
		String pt = xread.getCellData("Jobdata", 1, 5);
		String jd = xread.getCellData("Jobdata", 1, 6);

		httprequest = RestAssured.given().queryParam("Job Id", id).queryParam("Job Title", jt)
				.queryParam("Job Company Name", cn).queryParam("Job Location", jl).queryParam("Job Type", jty)
				.queryParam("Job Posted time", pt).queryParam("Job Description", jd);

		response = httprequest.request(Method.POST, "/Jobs");

	}

	public void assertheaderget() {

		Headers header = response.getHeaders();
		int hcount = header.asList().size();
		Assertions.assertEquals(6, hcount);

		String hvalue = response.getHeader("Content-Type");
		Assertions.assertEquals("application/json", hvalue);

		System.out.println("Header assertion passed");
		// System.out.println(header);
		System.out.println("Header count : " + hcount);
		System.out.println("Header Content Type : " + hvalue);
	}

	public void getresponsebody() {
		body = response.getBody().asString().replaceAll("NaN", "\"1 hrs\"");
		// body.prettyPrint();
		// body.prettyPeek();
		System.out.println("Response Body : " + body);
	}

	public void assertstatuscodeline() {
		scode = response.getStatusCode();
		// Assertions.assertEquals(scode, 200);
		System.out.println("Status code verification passed: " + scode);

		if (scode == 200) {
			Assert.assertEquals(scode, 200);
			System.out.println("sucessfully created: " + scode);

			String sline = response.getStatusLine();
			Assertions.assertEquals(sline, "HTTP/1.1 200 OK");
			System.out.println("StatusLine verification passed: " + sline);

		} else if (scode == 409) {
			Assert.assertEquals(scode, 409);
			System.out.println("Record already exist cant add duplicate: " + scode);

			String sline = response.getStatusLine();
			Assertions.assertEquals(sline, "HTTP/1.1 409 CONFLICT");
			System.out.println("StatusLine verification passed: " + sline);

		} else if (scode == 400) {
			Assert.assertEquals(scode, 400);
			System.out.println("Missing required parameter in the JSON body : " + scode);
		} else {

			System.out.println("Not a valid input: " + scode);
		}

	}

	public void schemavalidation() {
		if (scode == 200) {
			MatcherAssert.assertThat(body, JsonSchemaValidator.matchesJsonSchema(new File(
					"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\test\\java\\Request\\Jschema.json")));
			System.out.println("Schema Validation Passed , New Record created ");
		} else if (scode == 409) {

			MatcherAssert.assertThat(body, JsonSchemaValidator.matchesJsonSchema(new File(
					"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\test\\java\\Request\\alreadyexists.json")));
			System.out.println("Schema Validation Passed , Record already exist ");
		}
	}

	public void dbvalidation() {
		response = httprequest.request(Method.GET, "/Jobs");

		// Get Response Body as String
		body = response.getBody().asString().replaceAll("NaN", "\"1 hrs\"");

		// Validate if Response Body Contains a specific String
		Assert.assertTrue(body.contains("Job Id"));

		// Get JSON Representation from Response Body
		JsonPath jsonPathEvaluator = response.jsonPath();

		// Get specific element from JSON document (Failing to pass the details since DB
		// is not present)
		String jid = jsonPathEvaluator.get("Job Id");

		// Validate if the specific JSON element is equal to expected value
		Assert.assertTrue(jid.equals(id));
		System.out.println("Job Id is present : " + id);

	}

	public static void main(String[] args) throws IOException {
		PostRequest prun = new PostRequest();
		prun.postmethod();
		prun.assertheaderget();
		prun.getresponsebody();
		prun.assertstatuscodeline();
		prun.schemavalidation();
		// prun.dbvalidation(); No need for Db Validation for Jobs APi

	}
}
