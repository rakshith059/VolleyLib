package quintype.com.volleylib.utils

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.net.URLEncoder
import java.util.*


/**
 * Created NetworkVolleyRequest by rakshith on 10/1/18.
 */
class NetworkVolleyRequest<T>(protected var method: Int, protected var url: String, protected var type: Type, protected var header: Map<String, String>?, protected var params: Map<String, Any>, protected var callBack: Callback<T>, protected var contentType: String?) {

    internal var request: Request<T>? = null
        private set
    private val multipartBody: ByteArray? = null

    private fun getUrl(method: Int, url: String, params: Map<String, *>): String {
        var appendedURL = ""
        when (method) {
            RequestMethod.GET -> appendedURL = finalUrl(url, params, false)
            RequestMethod.POST -> appendedURL = finalUrl(url, params, true)
            else -> {
            }
        }
        Log.d("Rakshith", "appendedUrl " + appendedURL)
        return appendedURL
    }

    private fun finalUrl(url: String, params: Map<String, *>, isPost: Boolean): String {
        val basicUrl = StringBuilder(url)
        var isFirst = basicUrl.indexOf("?") < 0

        if (!isPost) {
            for (key in params.keys) {
                if (params[key] !is String) {
                    throw IllegalArgumentException("Only key value pair is allowed in GET method")
                }
                val value = params[key] as String
                if (params[key] != null) {
                    try {
                        basicUrl.append(if (isFirst) "?" else "&").append(key + "=" + URLEncoder.encode(value, "UTF-8"))
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }

                    if (isFirst)
                        isFirst = false
                }
            }

            //            basicUrl.append((isFirst?"?":"&") + "appVersion=" + AppController.APP_VERSION );
        }
        Log.d("Rakshith", "basic url " + basicUrl.toString())
        return basicUrl.toString()
    }

    interface Callback<T> {
        fun onSuccess(response: T)

        fun onError(errorCode: Int, errorMessage: String)
    }

    interface RequestMethod {
        companion object {
            val GET = 0
            val POST = 1
            val PUT = 2
            val DELETE = 3
        }
    }

    interface ContentType {
        companion object {
            val JSON = String.format("application/json; charset=%s", *arrayOf<Any>("utf-8"))
            val MULTIPART_FORMDATA = "multipart/form-data; boundary=-------------------------acebdf13572468"
            val TEXT_PLAIN = "text/plain"
            val X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        }
    }

    fun execute() {
        createRequest()
        AppController.instance?.addToRequestQueue(request as Request<T>)
    }

    protected var requestCreated = false

    private fun createRequest() {
        requestCreated = true
        var body: String? = null

        if (method == RequestMethod.POST) {
            contentType = if (contentType == null) ContentType.JSON else contentType
            body = getBody(contentType as String, params)
        }
        url = getUrl(method, url, params)

        if (header == null) {
            header = HashMap<String, String>()
        }

        val networkResponse = NetworkVolleyResponse(callBack)

        if (type is Class<*> && type === String::class.java) {
            if (body == null) {
                request = VolleyStringRequest(method, url, contentType, "", header, networkResponse as Response.Listener<String>, networkResponse) as Request<T>
            } else
                request = VolleyStringRequest(method, url, contentType, body, header, networkResponse as Response.Listener<String>, networkResponse) as Request<T>
        }
//        else if (type is Class<*> && type == VolleyMultipartRequest::class.java) {
//            request = VolleyMultipartRequest(method , url , contentType , body , header , networkResponse as Response.Listener)
//        }
        request?.tag = this
        //        PriorityAwareRequest.class.cast(request).setPriority(priority);
    }

    protected var priority: Request.Priority = Request.Priority.NORMAL

    interface PriorityAwareRequest {
        var priority: Request.Priority
    }

    private fun createMultiPartPostBody(params: Map<String, Any>?): String {
        val sbPost = StringBuilder()
        if (params != null) {
            for (key in params.keys) {
                if (params[key] != null && params[key] is String) {
                    sbPost.append("\r\n" + "--" + "-------------------------acebdf13572468" + "\r\n")
                    sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n")
                    sbPost.append(params[key].toString())
                }
            }
        }
        return sbPost.toString()
    }

    private fun getBody(contentType: String, params: Map<String, Any>): String? {
        if (contentType.equals(ContentType.JSON, ignoreCase = true)) {
            if (params.containsKey("body")) {
                return params["body"].toString()
            }
            return params.toString()
        } else if (contentType == ContentType.MULTIPART_FORMDATA) {
            return createPostBody(params)
        } else if (contentType.equals(ContentType.TEXT_PLAIN, ignoreCase = true)) {
            if (params.containsKey("body")) {
                return params["body"].toString()
            }
            return null
        } else if (contentType.equals(ContentType.X_WWW_FORM_URLENCODED, ignoreCase = true)) {
            return createPostBody(params)
        }
        return null
    }

    private fun createPostBody(params: Map<String, Any>): String {
        val param = HashMap<String, String>()
        param.put("Content-Type", "application/x-www-form-urlencoded")
        return param.toString()
    }
}