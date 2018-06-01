package rakshith.com.volleylib.utils

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.util.LruCache
import com.android.volley.toolbox.ImageLoader

/**
 * Created LruBitmapCache by rakshith on 10/1/18.
 */
class LruBitmapCache(maxSize: Int) : LruCache<String, Bitmap>(maxSize), ImageLoader.ImageCache {

    constructor(ctx: Context) : this(getCacheSize(ctx)) {}

    override fun sizeOf(key: String?, value: Bitmap?): Int {
        return value!!.rowBytes * value.height
    }

    override fun getBitmap(url: String): Bitmap {
        return get(url)
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }

    companion object {

        // Returns a cache size equal to approximately three screens worth of images.
        fun getCacheSize(ctx: Context): Int {
            val displayMetrics = ctx.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels
            // 4 bytes per pixel
            val screenBytes = screenWidth * screenHeight * 4

            return screenBytes * 3
        }
    }
}