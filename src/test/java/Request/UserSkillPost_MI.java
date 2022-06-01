package Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jobs.QA.Util.ExcelUtil;
import com.jobs.QA.Util.UserSkillPojo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSkillPost_MI {
	private Response response;
	private RequestSpecification httprequest;
	// private ValidatableResponse json;

	String uname = "APIPROCESSING";
	String pword = "2xx@Success";

	private int scode;
	// private String id;
	private String body;
	public String Sheetname = "UserSkill";
	public String path = "C:\\Users\\skuma\\eclipse-workspace\\RestAPIJobs\\src\\main\\java\\TestData\\UserSkillMultipleInput.xlsx";

	static String url = "https://springboot-lms-userskill.herokuapp.com/UserSkills";

	public void postmethodJasonObject() throws IOException {

		System.out.println("POST Request : Multiple Records Using JSON Object and JSON ARRAY");

		// Creating 1st record
		String sid = ExcelUtil.getCellData(Sheetname, 1, 0, path);
		String uide = ExcelUtil.getCellData(Sheetname, 1, 1, path);
		String ex = ExcelUtil.getCellData(Sheetname, 1, 2, path);

		JSONObject data1 = new JSONObject();
		data1.put("user_id", uide);
		data1.put("skill_id", sid);
		data1.put("months_of_exp", ex);

		System.out.println(data1);

		// Creating 2nd record
		String sid2 = ExcelUtil.getCellData(Sheetname, 2, 0, path);
		String uide2 = ExcelUtil.getCellData(Sheetname, 2, 1, path);
		String ex2 = ExcelUtil.getCellData(Sheetname, 2, 2, path);

		JSONObject data2 = new JSONObject();
		data2.put("user_id", uide2);
		data2.put("skill_id", sid2);
		data2.put("months_of_exp", ex2);

		System.out.println(data2);

		// Creating JSON array to add both JSON objects

		JSONArray jarray = new JSONArray();
		jarray.put(data1);
		jarray.put(data2);

		for (Object oj : jarray) {
			httprequest = RestAssured.given().auth().basic(uname, pword).contentType(ContentType.JSON)
					.body(oj.toString()).log().all();

			response = httprequest.request(Method.POST, url);

			body = response.getBody().asString();

			System.out.println("Response Body : " + body);

			scode = response.getStatusCode();
			System.out.println("Status code verification passed: " + scode);
		}
		System.out.println(
				"*************************************************************************************************");
	}

	public void postmethodPojo() throws IOException {

		System.out.println("POST Request : Multiple Records Using POJO CLASS");

		UserSkillPojo uspojo1 = new UserSkillPojo();
		uspojo1.setUser_id(ExcelUtil.getCellData(Sheetname, 1, 1, path));
		uspojo1.setSkill_id(ExcelUtil.getCellData(Sheetname, 1, 0, path));
		uspojo1.setMonths_of_exp(ExcelUtil.getCellData(Sheetname, 1, 2, path));

		UserSkillPojo uspojo2 = new UserSkillPojo();
		uspojo2.setUser_id(ExcelUtil.getCellData(Sheetname, 2, 1, path));
		uspojo2.setSkill_id(ExcelUtil.getCellData(Sheetname, 2, 0, path));
		uspojo2.setMonths_of_exp(ExcelUtil.getCellData(Sheetname, 2, 2, path));

		UserSkillPojo uspojo3 = new UserSkillPojo();
		uspojo3.setUser_id(ExcelUtil.getCellData(Sheetname, 3, 1, path));
		uspojo3.setSkill_id(ExcelUtil.getCellData(Sheetname, 3, 0, path));
		uspojo3.setMonths_of_exp(ExcelUtil.getCellData(Sheetname, 3, 2, path));

		// Creating a List of Employees
		List<UserSkillPojo> us = new ArrayList<UserSkillPojo>();
		us.add(uspojo1);
		us.add(uspojo2);
		us.add(uspojo3);
		
		//us.get(0).getSkill_id();

		for (UserSkillPojo usPojo : us) {
			httprequest = RestAssured.given().auth().basic(uname, pword).contentType(ContentType.JSON).body(usPojo)
					.log().all();

			response = httprequest.request(Method.POST, url);

			body = response.getBody().asString();

			System.out.println("Response Body : " + body);

			scode = response.getStatusCode();
			System.out.println("Status code verification passed: " + scode);
		}
		System.out.println(
				"************************************************************************************************");
	}

	public void postmethodMapList() throws IOException {

		System.out.println("POST Request : Multiple Records Using MAP and LIST");
		// Creating 1st record

		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("user_id", ExcelUtil.getCellData(Sheetname, 1, 0, path));
		data1.put("skill_id", ExcelUtil.getCellData(Sheetname, 1, 1, path));
		data1.put("months_of_exp", ExcelUtil.getCellData(Sheetname, 1, 2, path));

		System.out.println(data1);

		// Creating 2nd record

		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("user_id", ExcelUtil.getCellData(Sheetname, 2, 0, path));
		data2.put("skill_id", ExcelUtil.getCellData(Sheetname, 2, 1, path));
		data2.put("months_of_exp", ExcelUtil.getCellData(Sheetname, 2, 2, path));

		System.out.println(data2);

		// Creating JSON array to add both JSON objects

		List<Map<String, Object>> jarray = new ArrayList<Map<String, Object>>();

		jarray.add(data1);
		jarray.add(data2);
		
		//jarray.get(1).get("user_id");

		for (Object oj : jarray) {
			httprequest = RestAssured.given().auth().basic(uname, pword).contentType(ContentType.JSON).body(oj).log()
					.all();

			response = httprequest.request(Method.POST, url);

			body = response.getBody().asString();

			System.out.println("Response Body : " + body);

			scode = response.getStatusCode();
			System.out.println("Status code verification passed: " + scode);
		}
		System.out.println(
				"***********************************************************************************************");
	}

	public static void main(String[] args) throws IOException {
		UserSkillPost_MI prun = new UserSkillPost_MI();
		prun.postmethodJasonObject();
		prun.postmethodPojo();
		prun.postmethodMapList();

	}

}