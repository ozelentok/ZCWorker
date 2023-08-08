package ozelentok.zcworker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast


internal object Utils {
    fun showErrorToast(context: Context?, message: String, e: Exception) {
        Log.e("ZCWorker", message, e)
        Toast.makeText(context, """${message}\n${e.message}""", Toast.LENGTH_LONG).show()
    }

    fun showErrorToast(context: Context?, message: String?) {
        Log.e("ZCWorker", message!!)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(context: Context?, message: String?) {
        Log.i("ZCWorker", message!!)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun toBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}