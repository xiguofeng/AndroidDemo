package com.example.androiddemo;

import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.androiddemo.utils.AuthToken;
import com.example.androiddemo.utils.Constants;
import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.apache.ApacheHttpTransport;



public class WebActivity extends BaseActivity {

    private boolean handled;
    private WebView webView;
    private ProgressBar progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVisibility(View.VISIBLE);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
        progress.setIndeterminate(true);
        progress.setProgress(0);

    }


    @Override
    protected void onResume() {
        super.onResume();
        handled = false;
        new PreProcessToken().execute();

    }

    private class PreProcessToken extends AsyncTask<Uri, Void, Void> {

        final OAuthHmacSigner signer = new OAuthHmacSigner();
        private String authorizationUrl;

        @Override
        protected Void doInBackground(Uri... params) {

            try {

                signer.clientSharedSecret = Constants.CONSUMER_SECRET;

                OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(Constants.REQUEST_URL);
                temporaryToken.transport = new ApacheHttpTransport();
                temporaryToken.signer = signer;
                temporaryToken.consumerKey = Constants.CONSUMER_KEY;
                temporaryToken.callback = Constants.OAUTH_CALLBACK_URL;

                OAuthCredentialsResponse tempCredentials = temporaryToken.execute();
                signer.tokenSharedSecret = tempCredentials.tokenSecret;

                OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(Constants.AUTHORIZE_URL);
                authorizeUrl.temporaryToken = tempCredentials.token;
                authorizationUrl = authorizeUrl.build();

                handled = false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }


        /**
         * When we're done and we've retrieved either a valid token or an error from the server,
         * we'll return to our original activity
         */
        @Override
        protected void onPostExecute(Void result) {

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progress.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(final WebView view, final String url) {

                    if (url.startsWith(Constants.OAUTH_CALLBACK_URL)) {
                        if (url.indexOf("oauth_token=") != -1) {
                            webView.setVisibility(View.INVISIBLE);

                            if (!handled) {
                                new ProcessToken(url, signer).execute();
                            }
                        } else {
                            webView.setVisibility(View.VISIBLE);
                        }
                    }
                    progress.setProgress(100);
                    progress.setVisibility(View.GONE);
                }

            });

            webView.loadUrl(authorizationUrl);

        }

    }

    private class ProcessToken extends AsyncTask<Uri, Void, Void> {

        String url;
        private OAuthHmacSigner signer;

        public ProcessToken(String url, OAuthHmacSigner signer) {
            this.url = url;
            this.signer = signer;
        }

        @Override
        protected Void doInBackground(Uri... params) {

            Log.i(TAG, "doInbackground called with url " + url);
            if (url.startsWith(Constants.OAUTH_CALLBACK_URL) && !handled) {
                try {

                    if (url.indexOf("oauth_token=") != -1) {
                        handled = true;
                        String requestToken = extractParamFromUrl(url, "oauth_token");
                        String verifier = extractParamFromUrl(url, "oauth_verifier");

                        OAuthGetAccessToken accessToken = getOAuthAccessToken(requestToken);
                        accessToken.verifier = verifier;

                        OAuthCredentialsResponse credentials = accessToken.execute();
                        signer.tokenSharedSecret = credentials.tokenSecret;
                        localCredentialStore.store(new AuthToken(credentials.token, credentials.tokenSecret));

                    } else if (url.indexOf("error=") != -1) {
                        localCredentialStore.clear();
                    }

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            }
            return null;
        }

        public OAuthGetAccessToken getOAuthAccessToken(String requestToken) {
            signer.clientSharedSecret = Constants.CONSUMER_SECRET;
            OAuthGetAccessToken accessToken = new OAuthGetAccessToken(Constants.ACCESS_URL);
            accessToken.transport = new ApacheHttpTransport();
            accessToken.temporaryToken = requestToken;
            accessToken.signer = signer;
            accessToken.consumerKey = Constants.CONSUMER_KEY;
            return accessToken;
        }

        private String extractParamFromUrl(String url, String paramName) {

            Uri uri = Uri.parse(url);

            return uri.getQueryParameter(paramName);
        }

        @Override
        protected void onPreExecute() {

        }

        /**
         * When we're done and we've retrieved either a valid token or an error from the server,
         * we'll return to our original activity
         */
        @Override
        protected void onPostExecute(Void result) {
            startActivity(new Intent(WebActivity.this, Main2Activity.class));
            finish();
        }

    }

}
