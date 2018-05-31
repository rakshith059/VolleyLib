# VolleyLib

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.rakshith059:VolleyLib:v1.0'
	}
	
Usage: For Get Request

	private var SAMPLE_URL: String = ""				// Pass the Url here
	private val headerMap = HashMap<String, String>()		// Pass the header if you have any
	private val paramMap = HashMap<String, String>()		// Pass body params for POST method
    
	var sampleApi = NetworkVolleyRequest(
		NetworkVolleyRequest.RequestMethod.GET,			// GET for get request methods and POST for post request methods
		SAMPLE_URL,
		String::class.java,					// Response will be in String format
		headerMap,
		paramMap,
		object : NetworkVolleyRequest.Callback<Any> {
			override fun onError(errorCode: Int, errorMessage: String) {
				TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
			}
			override fun onSuccess(response: Any) {
				TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
			}
		}, NetworkVolleyRequest.ContentType.JSON		// content-type = application/json 
	).execute()
	
For POST Request:

	private var SAMPLE_URL: String = ""				// Pass the Url here
	private val headerMap = HashMap<String, String>()		// Pass the header if you have any
	private val paramMap = HashMap<String, String>()		// Pass body params for POST method
    
	var sampleApi = NetworkVolleyRequest(
		NetworkVolleyRequest.RequestMethod.POST,			// GET for get request methods and POST for post request methods
		SAMPLE_URL,
		String::class.java,					// Response will be in String format
		headerMap,
		paramMap,
		object : NetworkVolleyRequest.Callback<Any> {
			override fun onError(errorCode: Int, errorMessage: String) {
				TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
			}
			override fun onSuccess(response: Any) {
				TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
			}
		}, NetworkVolleyRequest.ContentType.JSON		// content-type = application/json 
	).execute()
