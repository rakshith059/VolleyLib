package rakshith.com.volleylib.utils

import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

/**
 * Created VolleyStringRequest by rakshith on 10/1/18.
 */
class VolleyStringRequest(method: Int, url: String, private val contentType: String?, private val body: String, private val headers: Map<String, String>?, listener: Response.Listener<String>, errorListener: Response.ErrorListener) : StringRequest(method, url, listener, errorListener) {
    protected var mPriority: Request.Priority = Request.Priority.NORMAL

    init {
        retryPolicy = DefaultRetryPolicy(INITIAL_TIMEOUT, MAX_RETRY, BACK_OFF_MULTIPLIER)
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return headers ?: super.getHeaders()
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        return body.toByteArray()
    }

    override fun getBodyContentType(): String {
        return contentType ?: super.getBodyContentType()
    }

    fun setPriority(priority: Request.Priority) {
        mPriority = priority
    }

    override fun getPriority(): Request.Priority {
        return mPriority
    }

    companion object {
        private val MAX_RETRY = 2 //Total Attempts is 3.
        private val INITIAL_TIMEOUT = 7500 //7.5secs.
        private val BACK_OFF_MULTIPLIER = 1.0f
    }
}