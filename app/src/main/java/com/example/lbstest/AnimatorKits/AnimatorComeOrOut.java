package com.example.lbstest.AnimatorKits;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 此类为动画控制类
 * Created by 天道 北云 on 2017/8/26.
 * @author BeiYun
 */

public class AnimatorComeOrOut {

    public static void setTopTranslateComeAnimator(View view, int duration,float comeFromWhere){
        float height=(float)view.getTop();
        ObjectAnimator comeAlphaAnimator = ObjectAnimator.ofFloat(view,"alpha",0f,1f);
        ObjectAnimator comeInYTranslate = ObjectAnimator.ofFloat(view,"y",comeFromWhere,height);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(comeAlphaAnimator).with(comeInYTranslate);
        animSet.setDuration(duration);
        animSet.start();
    }
    public static void setBottomTranslateComeAnimator(View view, int duration,float comeFromWhere){
        float height=(float)view.getTop();
        ObjectAnimator comeAlphaAnimator = ObjectAnimator.ofFloat(view,"alpha",0f,1f);
        ObjectAnimator comeInYTranslate = ObjectAnimator.ofFloat(view,"y",comeFromWhere,height);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(comeAlphaAnimator).with(comeInYTranslate);
        animSet.setDuration(duration);
        animSet.start();
    }
    public static void setTopTranslateOutAnimator(View view,int duration,float outToWhere) {
        float height = (float) view.getTop();
        ObjectAnimator comeAlphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator comeInYTranslate = ObjectAnimator.ofFloat(view, "y", height, outToWhere);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(comeAlphaAnimator).with(comeInYTranslate);
        animSet.setDuration(duration);
        animSet.start();
    }
    public static void setBottomTranslateOutAnimator(View view,int duration,float outToWhere) {
        float height = (float) view.getTop();
        ObjectAnimator comeAlphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator comeInYTranslate = ObjectAnimator.ofFloat(view, "y", height, outToWhere);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(comeAlphaAnimator).with(comeInYTranslate);
        animSet.setDuration(duration);
        animSet.start();
    }
    public static void setCenterComeAnimator(View view){
        ObjectAnimator comeAlphaAnimator = ObjectAnimator.ofFloat(view,"alpha",0f,1f);
        ObjectAnimator comeInXAnimator = ObjectAnimator.ofFloat(view,"ScaleX",0f,1f);
        ObjectAnimator comeInYAnimator = ObjectAnimator.ofFloat(view,"ScaleY",0f,1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(comeAlphaAnimator).with(comeInXAnimator).with(comeInYAnimator);
        animSet.setDuration(1000);
        animSet.start();
    }
}
