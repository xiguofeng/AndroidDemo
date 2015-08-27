package com.example.androiddemo;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.androiddemo.utils.Constants;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class Main2Activity extends BaseActivity {

	private ProgressBar progress;
	TextView serverRespText;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button startedBtn = (Button) findViewById(R.id.login_btn_get_started);
		Button catalogBtn = (Button) findViewById(R.id.catalog_btn);
		serverRespText = (TextView) findViewById(R.id.server_response);

		// Hide the button based on authtoken
		if (localCredentialStore.getToken().getAuthToken().isEmpty()) {
			startedBtn.setVisibility(View.VISIBLE);
			catalogBtn.setVisibility(View.GONE);
		} else {
			startedBtn.setVisibility(View.GONE);
			catalogBtn.setVisibility(View.VISIBLE);
		}
		// Open the webview to allow user to access the login page
		startedBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(v.getContext(),
						WebActivity.class));
			}
		});

		// Fetch the json objects after authorization
		catalogBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				progress.setVisibility(View.VISIBLE);
				serverRespText.setText("");
				new DownloadJson(Constants.PRODUCT_API_REQUEST, getConsumer())
						.execute();
			}
		});
		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setVisibility(View.GONE);
		progress.setIndeterminate(true);
		progress.setProgress(0);
	}

	// DownloadJSON AsyncTask
	private class DownloadJson extends AsyncTask {
		private String url;
		private OAuthParameters consumer;
		public HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

		public DownloadJson(String apiRequest, OAuthParameters consumer) {
			this.url = apiRequest;
			this.consumer = consumer;
		}

		@Override
		protected Object doInBackground(Object[] params) {
			return sendGoogleApiRequest();
		}

		private Object sendGoogleApiRequest() {
			try {

				GenericUrl requestUrl = new GenericUrl(url);

				HttpRequestFactory requestFactory = HTTP_TRANSPORT
						.createRequestFactory(new HttpRequestInitializer() {
							@Override
							public void initialize(HttpRequest request) {

								request.getHeaders().setAccept(
										"application/xml");
							}
						});

				HttpRequest request = requestFactory
						.buildGetRequest(requestUrl);

				consumer.initialize(request);

				Log.d(TAG, "Calling server with url:" + url);
				HttpResponse response = request.execute();
				Log.d(TAG, request.getHeaders().getAuthorization());
				if (response.isSuccessStatusCode()) {
					return response.parseAsString();
				} else {
					Log.w(TAG,
							"Issue with the server call: "
									+ response.getStatusMessage());
				}

			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			progress.setProgress(100);
			progress.setVisibility(View.GONE);
			Log.d(TAG, String.valueOf(result));
			serverRespText.setText(String.valueOf(result));
		}
	}
}
