package com.example.androiddemo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	// private static final String NAMESPACE = "urn:Magento";
	// private static final String URL =
	// "http://yourdomain.com/index.php/api/v2_soap";
	private static final String METHOD_NAME = "shoppingCartProductAdd";

	String NAMESPACE = "urn:Magento";
	String URL = "http://120.55.116.206:8080/api/soap/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new AccessTask().execute();

	}

	class AccessTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// pBar.setVisibility(View.VISIBLE);
			Log.e("onPreExecute",
					"----------------onPreExecute-----------------");
		}

		@Override
		protected Void doInBackground(Void... params) {
			readingData();
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			// pBar.setVisibility(View.INVISIBLE);

		}
	}

	public void readingData() {
		try {
			SoapSerializationEnvelope env = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			env.dotNet = false;
			env.xsd = SoapSerializationEnvelope.XSD;
			env.enc = SoapSerializationEnvelope.ENC;
			SoapObject request = new SoapObject(NAMESPACE, "login");

			request.addProperty("username", "app");
			request.addProperty("apiKey", "wpgapp");

			env.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			androidHttpTransport.call("", env);
			Object result = env.getResponse();
			Log.e("sessionId", result.toString());

			String sessionId = result.toString();

			SoapObject requestCart = new SoapObject(NAMESPACE,
					"catalogCategoryAssignedProducts");
			requestCart.addProperty("sessionId", sessionId);
			requestCart.addProperty("categoryId", 321);
			env.setOutputSoapObject(requestCart);
			androidHttpTransport.call("", env);
			result = env.getResponse();
			Log.e("List of Products", result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
