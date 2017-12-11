package app.deadmc.devnetworktool.helpers

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView

fun rotateImage(imageView: ImageView, collapsed: Boolean, duration: Int) {
    val rotate: RotateAnimation
    if (collapsed) {
        rotate = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    } else {
        rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    }
    rotate.duration = duration.toLong()
    rotate.interpolator = LinearInterpolator()
    rotate.isFillEnabled = true
    rotate.fillAfter = true
    imageView.startAnimation(rotate)

}


