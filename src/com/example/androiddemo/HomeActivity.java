package com.example.androiddemo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.androiddemo.utils.HttpUtils;

public class HomeActivity extends Activity {

	// private static final String NAMESPACE = "urn:Magento";
	// private static final String URL =
	// "http://yourdomain.com/index.php/api/v2_soap";
	private static final String METHOD_NAME = "shoppingCartProductAdd";

	String NAMESPACE = "urn:Magento";
	String URL = "http://120.55.116.206:8080/api/soap/";
	String RESTURL = "http://120.55.116.206:8080/api/rest/products";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new HttpAsyncTask()
				.execute("http://120.55.116.206:8080/api/rest/products");
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
		
            String resultStr;
            ArrayList<NameValuePair> params = new
                    ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("gid", "app"));

            try {

                resultStr = HttpUtils.sendHttpRequestByHttpClientGet(
                		RESTURL, params);

            } catch (Exception e) {
            }
        

			String host = "120.55.116.206:8080";

			HttpClient client = new DefaultHttpClient();

			BasicHttpContext localContext = new BasicHttpContext();
			HttpHost targetHost = new HttpHost(host, 443, "https");
			Log.d("url ", urls[0]);
			HttpGet httpget = new HttpGet(urls[0]);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader("Accept", "application/json");
			HttpResponse response;
			Object content = null;
			JSONObject json = null;
			try {
				response = client.execute(targetHost, httpget, localContext);
				HttpEntity entity = response.getEntity();

				content = EntityUtils.toString(entity);

				json = new JSONObject(content.toString());

				Log.d("result", "OK: " + json.toString(1));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
	}
}
