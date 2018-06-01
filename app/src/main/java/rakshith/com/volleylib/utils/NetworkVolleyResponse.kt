package rakshith.com.volleylib.utils

import com.android.volley.Response
import com.android.volley.VolleyError

/**
 * Created NetworkVolleyResponse by rakshith on 10/1/18.
 */
class NetworkVolleyResponse<T>(private val mCallBack: NetworkVolleyRequest.Callback<T>?) : Response.Listener<T>, Response.ErrorListener {

    override fun onResponse(t: T) {
        mCallBack?.onSuccess(t)
    }

    override fun onErrorResponse(volleyError: VolleyError?) {
        if (mCallBack != null) {
            if (volleyError != null && volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                mCallBack.onError(volleyError.networkResponse.statusCode, volleyError.networkResponse.data.toString())
            } else {
                mCallBack.onError(0, "")
            }
        }
    }
}