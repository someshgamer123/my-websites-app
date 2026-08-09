package com.yourapp.mywebsites.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.yourapp.mywebsites.R;

public class SplashActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Play sound
        mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound);
        mediaPlayer.start();

        TextView tvTitle = findViewById(R.id.tvSplashTitle);
        
        // Animation: Scale + Rotation + Alpha
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvTitle, "scaleX", 0.5f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvTitle, "scaleY", 0.5f, 1.2f, 1f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(tvTitle, "rotation", 0f, 360f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotation, alpha);
        animatorSet.setDuration(3000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                // Navigate to Login
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}