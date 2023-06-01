package com.example.miraiappv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.miraiappv2.R;

import java.util.Random;

public class BubbleAnimation extends FrameLayout {
    public BubbleAnimation(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bubble_animation_layout, this, true);
    }

    public void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bubble_rise);
        startAnimation(animation);
    }
}
