package com.yourapp.mywebsites.utils;

import android.content.Context;
import android.media.MediaPlayer;
import com.yourapp.mywebsites.R;

public class SoundUtils {
    private static MediaPlayer mediaPlayer;

    public static void playTapSound(Context context) {
        try {
            // Previous sound release karein
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            
            // Naya sound play karein
            mediaPlayer = MediaPlayer.create(context, R.raw.tap_sound);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                
                // Sound khatam hone par release
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        mediaPlayer = null;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}