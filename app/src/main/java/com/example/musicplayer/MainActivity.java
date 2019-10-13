package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.Duration;

import static android.os.Environment.getExternalStorageDirectory;
import static com.example.musicplayer.Background.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_activity_main);

        mainActivity = this;

        if(musicPlayer == null)
            musicPlayer = new CustomMusicPlayer();

        mainActivityMusicName = (TextView) findViewById(R.id.text_music_name);
        mainActivityPlayOrPause = (Button) findViewById(R.id.btn_play_or_pause);

        final RecyclerView listOfAllMusic = (RecyclerView) findViewById(R.id.recyclerView_listOfAllMusic);
        Button btn_scan = (Button) findViewById(R.id.btn_scan);

        btn_scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Log.d("MainActivity", "onClick");
                List<Music> musicList = scanMusic();
                MusicAdapter musicAdapter = new MusicAdapter(musicList);
                listOfAllMusic.setAdapter(musicAdapter);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfAllMusic.setLayoutManager(linearLayoutManager);
        //setContentView(R.layout.item_music);

        /*
        timer = new Timer();

        Button btn_pause = (Button) findViewById(R.id.btn_pause);
        Button btn_start = (Button) findViewById(R.id.btn_start);
        Button btn_end = (Button) findViewById(R.id.btn_stop);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        final TextView textView = (TextView) findViewById(R.id.textview);

        btn_pause.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_end.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        else
            initMediaPlayer();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                textView.setText("onProgressChanged");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                textView.setText("onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                textView.setText("onStopTrackingTouch");
            }
        });

        File file = new File(Environment.getExternalStorageDirectory().getPath());
        textView.setText(Integer.toString(file.listFiles(new MyFileFilter()).length));
//        File[] fileList = file.listFiles();
//        for(int i = 0; i < fileList.length; i++)
//        {
//            Log.d("MainActivity", fileList[i].getAbsolutePath());
//            String[] arr = fileList[i].getAbsolutePath().split(".");
//            if(arr.length > 0 && arr[arr.length - 1].equals("mp3"))
//            {
//                textView.setText(fileList[i].getAbsolutePath());
//            }
//        }
         */

        List<Music> musicList = scanMusic();
        final MusicAdapter musicAdapter = new MusicAdapter(musicList);
        listOfAllMusic.setAdapter(musicAdapter);

        mainActivityPlayOrPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(musicPlayer.isPlaying())
                {
                    pause();
                    updateMainActivityUI(null, false);
                }
                else
                {
                    if(resume())
                        updateMainActivityUI(null, true);
                    else
                        updateMainActivityUI(null, false);
                }
            }
        });

        LinearLayout bottom = (LinearLayout) findViewById(R.id.linear_layout_bottom);
        bottom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(musicPlayer.isReady())
                {
                    Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private List<Music> scanMusic()
    {
        List<Music> musicList = new ArrayList<Music>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor.moveToFirst())
        {
            for(int i = 0; i < cursor.getCount(); i++)
            {
                musicList.add(new Music(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))));
                cursor.moveToNext();
            }
        }
        Log.d("MainActivity", Integer.toString(musicList.size()));
        return musicList;
    }

    public void refreshRecyclerView()
    {
        MusicAdapter musicAdapter = new MusicAdapter(scanMusic());
        recyclerView.setAdapter(musicAdapter);
    }

    private void initMediaPlayer()
    {
        try
        {
            File file = new File(Environment.getExternalStorageDirectory(), "test.mp3");
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                initMediaPlayer();
            else
            {
                Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    @Override
    public void onClick(View view)
    {
//        seekBar.setProgress(seekBar.getProgress() + 5, true);
//        if(view.getId() == R.id.btn_pause)
//        {
//            mediaPlayer.pause();
//            timer.cancel();
//        }
//        else if(view.getId() == R.id.btn_start)
//        {
//            timer = new Timer();
//            timer.scheduleAtFixedRate(new MyTimerTask(mediaPlayer, seekBar), 0, 100);
//            mediaPlayer.start();
//        }
//        else
//        {
//            mediaPlayer.reset();
//            initMediaPlayer();
//            timer.cancel();
//        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        musicPlayer.destroy();
    }
}
