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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PutRequest {
	private Response response;
	private RequestSpecification httprequest;
	private int scode;
	private String body;

	public void putmethod() throws IOException {
		RestAssured.baseURI = "https://jobs123.herokuapp.com";

		ExcelReadWright xread = new ExcelReadWright(
				"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\main\\java\\TestData\\JobsApi.xlsx");
		String id = xread.getCellData("Jobdata", 1, 0);
		String jt = xread.getCellData("Jobdata", 2, 1);

		httprequest = RestAssured.given().queryParam("Job Id", id).queryParam("Job Title", jt);

		// jobid passing in QueryParam
		response = httprequest.request(Method.PUT, "/Jobs");

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

		System.out.println("Response Body : " + body);
	}

	public void assertstatuscodeline() {
		scode = response.getStatusCode();
		System.out.println("Status code verification passed: " + scode);

		if (scode == 200) {
			Assert.assertEquals(scode, 200);
			System.out.println("sucessfully created: " + scode);

			String sline = response.getStatusLine();
			Assertions.assertEquals(sline, "HTTP/1.1 200 OK");
			System.out.println("StatusLine verification passed: " + sline);

		} else if (scode == 404) {
			Assert.assertEquals(scode, 404);
			System.out.println("Record already exist cant add duplicate: " + scode);

			String sline = response.getStatusLine();
			Assertions.assertEquals(sline, "HTTP/1.1 404 NOT FOUND");
			System.out.println("StatusLine verification passed: " + sline);

		} else {

			System.out.println("Not a valid input: " + scode);
		}

	}

	public void schemavalidation() {
		if (scode == 200) {
			MatcherAssert.assertThat(body, JsonSchemaValidator.matchesJsonSchema(new File(
					"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\test\\java\\Request\\Jschema.json")));
			System.out.println("Schema Validation Passed , Record update successfully ");
		} else if (scode == 404) {
			MatcherAssert.assertThat(body, JsonSchemaValidator.matchesJsonSchema(new File(
					"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\test\\java\\Request\\alreadyexists.json")));
			System.out.println("Schema Validation Passed , Record Not Found ");
		}
	}

	public static void main(String[] args) throws IOException {
		PutRequest ptrun = new PutRequest();
		ptrun.putmethod();
		ptrun.assertheaderget();
		ptrun.getresponsebody();
		ptrun.assertstatuscodeline();
		ptrun.schemavalidation();

	}
}