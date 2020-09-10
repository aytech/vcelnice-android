package cz.vcelnicerudna

import android.graphics.*
import com.squareup.picasso.Transformation
import kotlin.math.min

class RoundedCornersTransformation : Transformation {
    override fun transform(source: Bitmap?): Bitmap {
        val size = min(source!!.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squaredBitmap: Bitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }
        val bitmap: Bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        val round: Float = size / 8f
        canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), round, round, paint)
        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "Rounded corners"
    }
}