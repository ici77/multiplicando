package com.example.multiplicando;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;

    public static void startMusic(Context context, int resId) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(context, resId);

        mediaPlayer.setOnCompletionListener(mp -> stopMusic());
        mediaPlayer.start();
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
