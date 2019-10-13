package com.example.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MusicScanner implements Runnable
{
    private Cursor cursor;
    private List<Music> musicList;

    public MusicScanner(Cursor cursor) {
        this.cursor = cursor;
        this.musicList = new ArrayList<Music>();
    }

    @Override
    public void run()
    {
        if(cursor.moveToFirst())
        {
            for(int i = 0; i < cursor.getCount(); i++)
            {
                musicList.add(new Music(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))));
                cursor.moveToNext();
            }
        }
        Log.d("MusicScanner", Integer.toString(musicList.size()));
    }

    public List<Music> getMusicList() {
        return musicList;
    }
}
