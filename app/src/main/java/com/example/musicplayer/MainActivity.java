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

public class MainActivity extends AppCompatActivity
{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        setContentView(R.layout.real_activity_main);

        mainActivity = this;
        if(musicPlayer == null)
            musicPlayer = new CustomMusicPlayer();
        mainActivityMusicName = (TextView) findViewById(R.id.text_music_name);
        mainActivityPlayOrPause = (Button) findViewById(R.id.btn_play_or_pause);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_listOfAllMusic);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshRecyclerView();

        Button btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                refreshRecyclerView();
            }
        });

        TextView playAll = (TextView) findViewById(R.id.text_playAll) ;
        playAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MusicAdapter musicAdapter = (MusicAdapter) recyclerView.getAdapter();
                List<Music> list = musicAdapter.getMusicList();
                for(int i = 0; i < list.size(); i++)
                    musicPlayer.addToList(list.get(i));
                musicPlayer.play();
                updateMainActivityUI(musicPlayer.getName(), musicPlayer.isPlaying());
            }
        });

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

        Button showPlayList = (Button) findViewById(R.id.btn_show_play_list);
        showPlayList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
                startActivity(intent);
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
                if(new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))).exists())
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                //initMediaPlayer();
            else
            {
                Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        musicPlayer.destroy();
    }
}
