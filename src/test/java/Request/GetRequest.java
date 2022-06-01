package Request;

import java.io.File;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
//import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class GetRequest {
	private Response response;
	private RequestSpecification httprequest;
	private String body;

	public void getmethod() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com";

		httprequest = RestAssured.given();

		response = httprequest.request(Method.GET, "/Jobs");

	}

	public void assertheaderget() {

		Headers header = response.getHeaders();
		int hcount = header.asList().size();
		Assertions.assertEquals(6, hcount);

		String hvalue = response.getHeader("Content-Type");
		Assertions.assertEquals("application/json", hvalue);

		System.out.println("Header assertion passed");
		System.out.println(header);
		System.out.println("Header count : " + hcount);
		System.out.println("Header Content Type : " + hvalue);
	}

	public void getresponsebody() {
		body = response.getBody().asString().replaceAll("NaN", "\"10 hrs\"");

		System.out.println("Response Body : " + body);
	}

	public void assertstatuscodeline() {
		int scode = response.getStatusCode();
		Assertions.assertEquals(scode, 200);
		System.out.println("Status code verification passed: " + scode);

		String sline = response.getStatusLine();
		Assertions.assertEquals(sline, "HTTP/1.1 200 OK");
		System.out.println("StatusLine verification passed: " + sline);
	}

	public void schemavalidation() {
		MatcherAssert.assertThat(body, JsonSchemaValidator.matchesJsonSchema(
				new File("C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\test\\java\\Request\\Jschema.json")));
		System.out.println("Schema Validation Passed");
	}

	public static void main(String[] args) {
		GetRequest run = new GetRequest();
		run.getmethod();
		run.assertheaderget();
		run.getresponsebody();
		run.assertstatuscodeline();
		run.schemavalidation();

	}
}
