package org.proj.auth;

import io.restassured.RestAssured.*;
import static io.restassured.RestAssured.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers.*;
import org.proj.pojos.Library;

import groovy.util.logging.Log;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Demo {
	public static void main(String[] args) {
		RequestSpecification requestLib = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.setContentType(ContentType.JSON).build();

		ResponseSpecification responseLib = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
				.expectStatusCode(200).build();
		Library library = new Library();
		library.setAisle("2926");
		library.setAuthor("John foer");
		library.setIsbn("bcd");
		library.setName("Learn Appium Automation with Java");

		String reqBook = given().log().all().spec(requestLib).body(library).when().post("Library/Addbook.php").then()
				.log().all().spec(responseLib).extract().response().asString();

		JsonPath js = new JsonPath(reqBook);
		String id = js.get("ID");
//		System.out.println(id);
		String getBookByAuthor = given().log().all().spec(requestLib).queryParam("ID", id).when()
				.get("Library/GetBook.php").then().log().all().spec(responseLib).extract().response().asString();

		RequestSpecification addBook = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addQueryParam("AuthorName", "John foer").setContentType(ContentType.JSON).build();
		String getSpecificAuthor = given().log().all().spec(addBook).when().get("Library/GetBook.php").then().log()
				.all().spec(responseLib).extract().response().asString();

		given().log().all().spec(requestLib).body("{\r\n"
				+ "    \"ID\": \""+id+"\"\r\n"
				+ "}").when().delete("Library/DeleteBook.php").then().log().all()
				.spec(responseLib).extract().response().asString();

	}

}
