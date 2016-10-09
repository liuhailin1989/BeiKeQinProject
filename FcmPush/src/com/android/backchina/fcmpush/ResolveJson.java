package com.android.backchina.fcmpush;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;

/**
 * google-services.json文件读取与解析
 * @author xiaonan
 *
 */
public class ResolveJson {
	private static HashMap<String, String> hashMap = new HashMap<String, String>();
	
//	public static String getValue(String key){
//		return hashMap.get(key);
//	}
	
	//Json文件读取
	protected static HashMap<String, String> resolve(Context context) {
		try {
			InputStream is = context.getResources().getAssets().open("google-services.json");
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1){
				outStream.write(buffer, 0, len);
			}
			outStream.flush();
			String json = new String(outStream.toByteArray(), "ISO-8859-1");
			createHashMap(json);
			//关闭输入输出流
			outStream.close();
			is.close();
			buffer = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashMap;
	}
	//Json文件解析
	private static void createHashMap(String str) throws Exception{
		JSONObject json = new JSONObject(str);
		JSONObject info = json.getJSONObject("project_info");
		JSONObject client = json.getJSONArray("client").getJSONObject(0);
		JSONObject apiKey = client.getJSONArray("api_key").getJSONObject(0);
		hashMap.put("ProjectId", info.getString("project_id"));
		hashMap.put("ApiKey", apiKey.getString("current_key"));
		hashMap.put("ApplicationId", client.getJSONObject("client_info").getString("mobilesdk_app_id"));
		hashMap.put("DatabaseUrl", info.getString("firebase_url"));
		hashMap.put("GcmSenderId", info.getString("project_number"));
	}
	
}
