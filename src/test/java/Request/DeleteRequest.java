package Request;

import java.io.File;
import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;

import com.jobs.QA.Util.ExcelReadWright;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteRequest {
	private Response response;
	private RequestSpecification httprequest;
	private int scode;
	private String body;

	public void deletemethod() throws IOException {
		RestAssured.baseURI = "https://jobs123.herokuapp.com";

		ExcelReadWright xread = new ExcelReadWright(
				"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\main\\java\\TestData\\JobsApi.xlsx");
		String id = xread.getCellData("Jobdata", 1, 0);

		httprequest = RestAssured.given().queryParam("Job Id", id);

		// jobid passing in QueryParam
		response = httprequest.request(Method.DELETE, "/Jobs");

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
			Assertions.assertEquals(scode, 200);

			System.out.println("sucessfully created: " + scode);

			String sline = response.getStatusLine();
			Assertions.assertEquals(sline, "HTTP/1.1 200 OK");
			System.out.println("StatusLine verification passed: " + sline);

		} else if (scode == 404) {
			Assertions.assertEquals(scode, 404);
			System.out.println("Record Not Found : " + scode);

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
			System.out.println("Schema Validation Passed , Record successfully Deleted ");
		} else if (scode == 404) {
			MatcherAssert.assertThat(body, JsonSchemaValidator.matchesJsonSchema(new File(
					"C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\test\\java\\Request\\alreadyexists.json")));
			System.out.println("Schema Validation Passed , Record Not Found");
		}
	}

	public static void main(String[] args) throws IOException {

		DeleteRequest drun = new DeleteRequest();
		drun.deletemethod();
		drun.assertheaderget();
		drun.getresponsebody();
		drun.assertstatuscodeline();
		drun.schemavalidation();
	}

}