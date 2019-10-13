package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.musicplayer.Background.*;

public class MusicActivity extends AppCompatActivity
{

    private Timer timer = new Timer();
    private MyTimerTask timerTask;
    private SeekBar seekBar;
    private TextView passTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        musicActivity = this;

        passTime = (TextView) findViewById(R.id.text_pass_time);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        TextView textView = (TextView) findViewById(R.id.text_title_name);
        textView.setText(musicPlayer.getName());
        textView = (TextView) findViewById(R.id.text_title_artist);
        textView.setText(musicPlayer.getArtist());
        textView = (TextView) findViewById(R.id.text_duration);
        textView.setText(getTime(musicPlayer.getDuration()));
        textView = (TextView) findViewById(R.id.text_pass_time);
        textView.setText(getTime(musicPlayer.getCurrentProgress()));

        final Button playOrPause = (Button) findViewById(R.id.btn_primary_play_or_pause);
        if(musicPlayer.isPlaying())
        {
            setTimer();
            playOrPause.setBackgroundResource(R.mipmap.pic_pause);
        }
        playOrPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(musicPlayer.isPlaying())
                {
                    pause();
                    updateMainActivityUI(null, false);
                    stopTimer();
                    playOrPause.setBackgroundResource(R.mipmap.pic_play);
                }
                else
                {
                    if(resume())
                    {
                        updateMainActivityUI(null, true);
                        setTimer();
                        playOrPause.setBackgroundResource(R.mipmap.pic_pause);
                    }
                    else
                    {
                        updateMainActivityUI(null, false);
                        playOrPause.setBackgroundResource(R.mipmap.pic_play);
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                passTime.setText(getTime(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                stopTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                musicPlayer.seekTo(seekBar.getProgress());
                if(musicPlayer.isPlaying())
                    setTimer();
            }
        });

        Button btn_play_previous = (Button) findViewById(R.id.btn_play_previous);
        btn_play_previous.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                stopTimer();
                String[] arr = musicPlayer.playPrevious();
                setTimer();
                TextView textView = (TextView) findViewById(R.id.text_title_name);
                textView.setText(arr[0]);
                textView = (TextView) findViewById(R.id.text_title_artist);
                textView.setText(arr[1]);
            }
        });

        Button btn_play_next = (Button) findViewById(R.id.btn_play_next);
        btn_play_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                stopTimer();
                String[] arr = musicPlayer.playNext();
                setTimer();
                TextView textView = (TextView) findViewById(R.id.text_title_name);
                textView.setText(arr[0]);
                textView = (TextView) findViewById(R.id.text_title_artist);
                textView.setText(arr[1]);
            }
        });

        Button btn_switch_mode = (Button) findViewById(R.id.btn_mode);
        btn_switch_mode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicPlayer.switchMode();
                if(musicPlayer.getMode() == 0)
                    view.setBackgroundResource(R.mipmap.pic_loop);
                else if(musicPlayer.getMode() == 1)
                    view.setBackgroundResource(R.mipmap.pic_single_loop);
                else
                    view.setBackgroundResource(R.mipmap.pic_random);
            }
        });

    }

    private void setTimer()
    {
        timer = new Timer();
        timerTask = new MyTimerTask(musicPlayer, seekBar, passTime);
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }

    public void updateTitle(String name, String artist)
    {
        TextView textView = (TextView) findViewById(R.id.text_title_name);
        textView.setText(name);
        textView = (TextView) findViewById(R.id.text_title_artist);
        textView.setText(artist);
    }

    private void stopTimer()
    {
        if(timer != null)
            timer.cancel();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        musicActivity = null;
        stopTimer();
        timer = null;
    }
}
