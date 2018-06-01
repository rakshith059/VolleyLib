package rakshith.com.volleylib.utils

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

/**
 * Created AppController by rakshith on 10/1/18.
 */

class AppController : Application() {

    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null
    var cookieManager: CookieManager? = null
        private set

    val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)
                CookieHandler.setDefault(cookieManager)
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }

            return mRequestQueue as RequestQueue
        }

    val imageLoader: ImageLoader?
        get() {
            requestQueue
            if (mImageLoader == null) {
                mImageLoader = ImageLoader(this.mRequestQueue,
                        LruBitmapCache(this))
            }
            return this.mImageLoader
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
        getDeviceId()
        getAppVersion()
        getIpAddress()
    }

    private fun getIpAddress() {
        val wifiMan = getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInf = wifiMan.connectionInfo
        val ipAddress = wifiInf.ipAddress
        val ip = String.format("%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
        Log.d("Rakshith", "ip address $ip")
    }

    private fun getDeviceId() {
        DEVICE_ID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("Rakshith", "device Id $DEVICE_ID")
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        requestQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    private fun getAppVersion() {
        try {
            val packageManager = applicationContext.packageManager
            val info = packageManager.getPackageInfo(applicationContext.packageName, 0)
            APP_VERSION = java.lang.Float.parseFloat(info.versionName)

            Log.d("Rakshith", "app version $APP_VERSION")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        var APP_VERSION: Float = 0.toFloat()
        var DEVICE_ID: String = ""

        val TAG = AppController::class.java
                .simpleName

        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}
