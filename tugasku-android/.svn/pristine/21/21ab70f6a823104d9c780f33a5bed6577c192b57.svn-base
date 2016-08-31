package id.ac.gunadarma.tugasku.account;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.gson.Gson;

public class TaskServerAuthenticate implements ServerAuthenticate {
	
    @Override
    public String userSignUp(String name, String username, String email, String pass, String authType) throws Exception {

        String url = "http://tugasku.herokuapp.com/register";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("first_name", name));
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", username));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            CredentialsResponse registerResponse = new Gson().fromJson(responseString, CredentialsResponse.class);
            if (registerResponse.statusCode != 0) {
                throw new Exception("Error signing-in ["+registerResponse.statusCode+"] - " + registerResponse.statusMessage);
            } else {
            	authtoken = registerResponse.secretToken;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authtoken;
    }

    @Override
    public String userSignIn(String username, String password, String authType) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "http://tugasku.herokuapp.com/login/token";
        HttpPost httpPost = new HttpPost(url);
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            CredentialsResponse registerResponse = new Gson().fromJson(responseString, CredentialsResponse.class);
            if (registerResponse.statusCode != 0) {
                throw new Exception("Error signing-in ["+registerResponse.statusCode+"] - " + registerResponse.statusMessage);
            } else {
            	authtoken = registerResponse.secretToken;
            }
        Log.d("Task", "Response: "+registerResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authtoken;
    }


    private class CredentialsResponse implements Serializable {
    	
		private static final long serialVersionUID = 6238763861656226544L;
		
		int statusCode;
        String statusMessage;
        String secretToken;
		@Override
		public String toString() {
			return "CredentialsResponse [statusCode=" + statusCode
					+ ", statusMessage=" + statusMessage + ", secretToken="
					+ secretToken + "]";
		}
    }
    
}
