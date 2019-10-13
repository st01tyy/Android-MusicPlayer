package com.example.musicplayer;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class Background
{
    public static CustomMusicPlayer musicPlayer;

    public static TextView mainActivityMusicName;

    public static Button mainActivityPlayOrPause;

    public static MainActivity mainActivity;

    public static MusicActivity musicActivity;

    public static PlayListActivity playListActivity;


    public static boolean playMusicNow(Music music)
    {
        if(musicPlayer == null)
        {
            Log.e("Background", "for some reason, musicPlayer is null");
            return false;
        }
        if(!musicPlayer.setMusic(music))
            return false;
        musicPlayer.play();
        return true;
    }

    public static boolean resume()
    {
        if(musicPlayer.isReady())
        {
            musicPlayer.play();
            return true;
        }
        else
            return false;
    }

    public static void pause()
    {
        musicPlayer.pause();
    }

    public static void updateMainActivityUI(String musicName, boolean isPlaying)
    {
        if(musicName != null)
            mainActivityMusicName.setText(musicName);
        if(isPlaying)
            mainActivityPlayOrPause.setBackgroundResource(R.mipmap.pic_pause);
        else
            mainActivityPlayOrPause.setBackgroundResource(R.mipmap.pic_play);
    }

    public static String getTime(int duration)
    {
        Date date = new Date(duration);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }



}
