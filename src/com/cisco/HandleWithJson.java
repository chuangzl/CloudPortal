package com.cisco;

import org.json.JSONArray;
import org.json.JSONObject;

public class HandleWithJson {

	public void Analytic_Health (String result_json) {
		
		JSONObject jsonobject;
		jsonobject = new JSONObject(result_json);
		System.out.println("type:" + jsonobject.getString("type"));
		System.out.println("status:" + jsonobject.getString("status"));
		System.out.println("message:" + jsonobject.getString("message"));
		JSONArray data_array = jsonobject.getJSONArray("data");
		for(int i =0;i<data_array.length();i++) 
		{
			JSONObject data_jsonobject =data_array.getJSONObject(i);
			System.out.println("name:" + data_jsonobject.getString("name"));
			System.out.println("id:" + data_jsonobject.getString("id"));
			/*  get JSON arrayfrom application key  */
			JSONArray data_applications = data_jsonobject.getJSONArray("applications");
			for (int j=0;j<data_applications.length();j++) {
				JSONObject application_jsonobject =data_applications.getJSONObject(j);
				System.out.println("name:" + application_jsonobject.getString("name"));
				System.out.println("appType:" + application_jsonobject.getInt("appType"));
				JSONArray overallscores_jsonarry=application_jsonobject.getJSONArray("overAllScores");
				System.out.println(overallscores_jsonarry.toString());
			}
			
			
		}
		
	}
}
