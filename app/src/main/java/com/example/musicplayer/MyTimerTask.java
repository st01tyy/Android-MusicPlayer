package com.example.musicplayer;

import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask
{
    private CustomMusicPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView textView;

    public MyTimerTask(CustomMusicPlayer mediaPlayer, SeekBar seekBar, TextView textView)
    {
        this.mediaPlayer = mediaPlayer;
        this.seekBar = seekBar;
        this.textView = textView;
        seekBar.setMax(mediaPlayer.getDuration());
    }

    @Override
    public void run()
    {
        seekBar.setProgress(mediaPlayer.getCurrentProgress(), true);
        //textView.setText(Background.getTime(mediaPlayer.getCurrentProgress()));
    }
}
