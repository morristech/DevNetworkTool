package app.deadmc.devnetworktool.helpers;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by adanilov on 20.03.2017.
 */

public class ImageHelpers {

    public static void rotateImage(ImageView imageView, boolean collapsed, int duration) {
        RotateAnimation rotate;
        if (collapsed) {
            rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rotate.setDuration(duration);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        imageView.startAnimation(rotate);

    }

}
