package com.example.androiddemo.utils;


public class Constants {

	//You Custom Consumer Key
	public static final String CONSUMER_KEY = "dde6e2f7f3d1cd37e0bc419acb5d2e38";
	//You Custom Consumer SECRET
	public static final String CONSUMER_SECRET = "a0be49c3cdd913fb8d56187f376a9cd8";
	//Your Base URL for the site
	public static final String BASE_URL = "http://120.55.116.206:8080/";

	public static final String REQUEST_URL 		= BASE_URL + "oauth/initiate";
	public static final String ACCESS_URL 		= BASE_URL + "oauth/token";
	public static final String AUTHORIZE_URL 	= BASE_URL + "oauth/authorize";
    public static final String API_REQUEST 		= BASE_URL + "api/rest/";

	public static final String PRODUCT_API_REQUEST 		=   API_REQUEST+"products";
	
	public static final String ENCODING 		= "UTF-8";

    public static final String OAUTH_CALLBACK_URL = "http://120.55.116.206:8080/oauth_admin.php";

}
