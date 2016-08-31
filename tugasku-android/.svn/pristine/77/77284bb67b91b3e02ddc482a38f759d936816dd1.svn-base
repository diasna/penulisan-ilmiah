package id.ac.gunadarma.tugasku.helper;

import id.ac.gunadarma.tugasku.helper.dao.Task;
import id.ac.gunadarma.tugasku.helper.dao.TaskResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Api {
	
	public static final String BASE_URL = "http://tugasku.herokuapp.com/";
	public static final String GET_TASK = BASE_URL + "/api/task/list";
	public static final String POST_TASK = BASE_URL + "/api/task/add";
	public static final String REMOVE_TASK = BASE_URL + "/api/task/remove";
	public static final String REGISTER = BASE_URL + "/register";
	
	public static List<Task> getTaskList(String token){
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(GET_TASK);
        httpGet.addHeader("Authorization", token);
        List<Task> taskList = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("Api", "Get Task ["+response.getStatusLine()+"] : "+ responseString);
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            taskList = new GsonBuilder()
            		.excludeFieldsWithoutExposeAnnotation()
            		.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {  
                @Override  
                public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {  
                    return new Date(json.getAsLong());  
                }  
            }).create().fromJson(responseString, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskList;
	}
	
	public static void removeTask(String id, String token){
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(GET_TASK+"?id="+id);
        httpGet.addHeader("Authorization", token);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("Api", "Remove Task ["+response.getStatusLine()+"] ["+id+"] : "+ responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void register(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(REGISTER);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("fname", "kambing"));
        nameValuePairs.add(new BasicNameValuePair("username", "kambing"));
        nameValuePairs.add(new BasicNameValuePair("password", "kambing"));
        nameValuePairs.add(new BasicNameValuePair("email", "kambing@ui.ac.id"));
        try {
	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("Api", "Register User ["+response.getStatusLine()+"] : "+ responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static String postTask(Task task, String token){
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(POST_TASK);
        httpPost.addHeader("Authorization", token);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("content", task.content));
        nameValuePairs.add(new BasicNameValuePair("deadline", new SimpleDateFormat("yyyy-MM-dd").format(task.deadline)+""));
        nameValuePairs.add(new BasicNameValuePair("done", task.done+""));
        nameValuePairs.add(new BasicNameValuePair("deleted", task.deleted+""));
        TaskResponse taskResponse;
        try {
	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("Api", "Post Task ["+response.getStatusLine()+"] ["+task.content+"] : "+ responseString);
            taskResponse = new Gson().fromJson(responseString, TaskResponse.class);
			if (response.getStatusLine().getStatusCode() == 200 || taskResponse.statusCode.equals("00")) {
				return taskResponse.id;
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
	}
}
