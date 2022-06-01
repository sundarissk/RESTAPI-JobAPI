package Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hpsf.Array;
import org.json.JSONObject;

import com.jobs.QA.Util.ExcelUtil;
import com.jobs.QA.Util.UserSkillPojo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSkillMR_POST {

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

	public void PostJsonObject() throws IOException {

		int rowcount = ExcelUtil.getRowCount(Sheetname, path);
		int colcount = ExcelUtil.getCellCount(Sheetname, 1, path);
		System.out.println("Row : " + rowcount + "Column :" + colcount);

		List<JSONObject> jo = new ArrayList<JSONObject>();

		for (int i = 1; i <= rowcount; i++) {
			JSONObject data = new JSONObject();
			for (int j = 0; j < colcount; j++) {
				data.put("skill_id", ExcelUtil.getCellData(Sheetname, i, j, path));
				data.put("user_id", ExcelUtil.getCellData(Sheetname, i, j + 1, path));
				data.put("months_of_exp", ExcelUtil.getCellData(Sheetname, i, j + 2, path));
				jo.add(data);
				break;

			}
			// jo.add(data);
		}

		System.out.println(jo);

		for (JSONObject jsonObject : jo) {
			httprequest = RestAssured.given().auth().basic(uname, pword).contentType(ContentType.JSON)
					.body(jsonObject.toString()).log().all();
			response = httprequest.request(Method.POST, url);

			body = response.getBody().asString();

			System.out.println("Response Body : " + body);

			scode = response.getStatusCode();
			System.out.println("Status code verification passed: " + scode);
			System.out.println(
					"*************************************************************************************************");
		}

	}

	
	public void PostPojoClass() throws IOException {
		int rowcount = ExcelUtil.getRowCount(Sheetname, path);
		int colcount = ExcelUtil.getCellCount(Sheetname, 1, path);
		System.out.println("Row : " + rowcount + "Column :" + colcount);
		List<UserSkillPojo> jo = new ArrayList<UserSkillPojo>();
		for (int i = 1; i <= rowcount; i++) {
			UserSkillPojo data = new UserSkillPojo();
			for (int j = 0; j < colcount; j++) {
				data.setSkill_id(ExcelUtil.getCellData(Sheetname, i, j, path));
				data.setUser_id(ExcelUtil.getCellData(Sheetname, i, j + 1, path));
				data.setMonths_of_exp(ExcelUtil.getCellData(Sheetname, i, j + 2, path));
				jo.add(data);
				break;

			}
		}
		System.out.println(jo);

		for (UserSkillPojo uspojo : jo) {
			httprequest = RestAssured.given().auth().basic(uname, pword).contentType(ContentType.JSON).body(uspojo)
					.log().all();
			
			response = httprequest.request(Method.POST, url);

			body = response.getBody().asString();

			System.out.println("Response Body : " + body);

			scode = response.getStatusCode();
			System.out.println("Status code verification passed: " + scode);
			System.out.println(
					"*************************************************************************************************");
		}
	}

	
	public void PostMapList() throws IOException {
		int rowcount = ExcelUtil.getRowCount(Sheetname, path);
		int colcount = ExcelUtil.getCellCount(Sheetname, 1, path);
		System.out.println("Row : " + rowcount + "Column :" + colcount);

		List<Map<String, Object>> jo = new ArrayList<Map<String, Object>>();

		for (int i = 1; i <= rowcount; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			for (int j = 0; j < colcount; j++) {
				data.put("skill_id", ExcelUtil.getCellData(Sheetname, i, j, path));
				data.put("user_id", ExcelUtil.getCellData(Sheetname, i, j + 1, path));
				data.put("months_of_exp", ExcelUtil.getCellData(Sheetname, i, j + 2, path));
				jo.add(data);
				break;

			}
		}
		System.out.println(jo);
		for (Map<String, Object> map : jo) {
			httprequest = RestAssured.given().auth().basic(uname, pword).contentType(ContentType.JSON).body(map).log()
					.all();
			//map.get("user_id");

			response = httprequest.request(Method.POST, url);

			body = response.getBody().asString();

			System.out.println("Response Body : " + body);

			scode = response.getStatusCode();
			System.out.println("Status code verification passed: " + scode);
			System.out.println(
					"*************************************************************************************************");
		}

	}

	public static void main(String[] args) throws IOException {
		UserSkillMR_POST run = new UserSkillMR_POST();
		// run.PostJsonObject();
		//run.PostPojoClass();
		run.PostMapList();

	}
}
