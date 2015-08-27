package com.example.androiddemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.example.androiddemo.utils.Constants;
import com.example.androiddemo.utils.LocalCredentialStore;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;


/**
 * Created by kapiljain on 11/07/15.
 */
public class BaseActivity extends FragmentActivity {
    public final static String TAG = "OAuthMagentoRetrofit";
    public static SharedPreferences prefs;
    LocalCredentialStore localCredentialStore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);
    }

    //Setup the AuthParameters for API Call
    protected OAuthParameters getConsumer() {
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = Constants.CONSUMER_SECRET;
        signer.tokenSharedSecret = localCredentialStore.getToken().getAuthTokenSecret();
        OAuthParameters authorizer = new OAuthParameters();
        authorizer.consumerKey = Constants.CONSUMER_KEY;
        authorizer.signer = signer;
        authorizer.token = localCredentialStore.getToken().getAuthToken();

        return authorizer;
    }
}
