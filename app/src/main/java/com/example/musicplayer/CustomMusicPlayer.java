package com.example.musicplayer;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CustomMusicPlayer
{
    private MediaPlayer mediaPlayer;
    private Music currentMusic;
    private Music headOfPlayList;
    private int mode;
    private int length;
    private boolean tag;

    public CustomMusicPlayer()
    {
        mediaPlayer = new MediaPlayer();
        currentMusic = headOfPlayList = null;
        mode = 0;
        length = 0;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                Log.d("CustomMusicPlayer", "OnCompletionListener triggered");
                Log.d(currentMusic.getName(), Background.getTime(mediaPlayer.getCurrentPosition()));
                int temp = mode % 3;
                if(temp == 0)
                {
                    setMusic(currentMusic.next);
                    play();
                    Background.updateMainActivityUI(currentMusic.getName(), true);
                    if(Background.musicActivity != null)
                        Background.musicActivity.updateUI(currentMusic.getName(), currentMusic.getArtist());
                }
                else if(temp == 1)
                {
                    setMusic(currentMusic);
                    play();
                    Background.updateMainActivityUI(currentMusic.getName(), true);
                    if(Background.musicActivity != null)
                        Background.musicActivity.updateUI(currentMusic.getName(), currentMusic.getArtist());
                }
                else
                {
                    setMusic(random());
                    play();
                    Background.updateMainActivityUI(currentMusic.getName(), true);
                    if(Background.musicActivity != null)
                        Background.musicActivity.updateUI(currentMusic.getName(), currentMusic.getArtist());
                }
            }
        });
    }

    public boolean setMusic(Music music)
    {
        if(music == null)
            return false;
        try
        {
            if(headOfPlayList == null)
                addToList(music);
            if(currentMusic != null && music.next == null && music.previous == null)
            {
                Log.d("CustomMusicPlayer", "fix the chain list");
                music.previous = currentMusic;
                music.next = currentMusic.next;
                currentMusic.next.previous = music;
                currentMusic.next = music;
            }
            currentMusic = music;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentMusic.getPath());
            mediaPlayer.prepare();
            return true;
        }
        catch(Exception e)
        {
            Log.e("CustomMusicPlayer", "error occurred when set music");
            e.printStackTrace();
            return false;
        }
    }

    public String[] playPrevious()
    {
        if(mode % 3 == 2)
            setMusic(random());
        else
            setMusic(currentMusic.previous);
        String[] arr = new String[]{currentMusic.getName(), currentMusic.getArtist()};
        play();
        Background.updateMainActivityUI(currentMusic.getName(), true);
        return arr;
    }

    public String[] playNext()
    {
        if(mode % 3 == 2)
            setMusic(random());
        else
            setMusic(currentMusic.next);
        String[] arr = new String[]{currentMusic.getName(), currentMusic.getArtist()};
        play();
        Background.updateMainActivityUI(currentMusic.getName(), true);
        return arr;
    }


    public void play()
    {
        try
        {
            mediaPlayer.start();
        }
        catch(Exception e)
        {
            Log.e("CustomMusicPlayer", "error occurred when start music");
            e.printStackTrace();
        }
    }

    public void pause()
    {
        try
        {
            if(mediaPlayer.isPlaying())
                mediaPlayer.pause();
        }
        catch(Exception e)
        {
            Log.e("CustomMusicPlayer", "error occurred when pause music");
            e.printStackTrace();
        }
    }

    public void destroy()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public boolean isReady()
    {
        if(currentMusic == null)
            return false;
        else
            return true;
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public String getName()
    {
        if(currentMusic != null)
            return currentMusic.getName();
        else
            return null;
    }

    public String getArtist()
    {
        if(currentMusic != null)
            return currentMusic.getArtist();
        else
            return null;
    }

    public int getDuration()
    {
        if(currentMusic != null)
            return mediaPlayer.getDuration();
        else
            return 0;
    }

    public int getCurrentProgress()
    {
        if(currentMusic != null)
            return mediaPlayer.getCurrentPosition();
        else
            return 0;
    }

    public void seekTo(int location)
    {
        if(currentMusic != null)
            mediaPlayer.seekTo(location);
    }

    public void switchMode()
    {
        mode++;
    }

    public int getMode()
    {
        return mode % 3;
    }

    public void addToList(Music music)
    {
        Log.d("CustomMusicPlayer", "before add to list");
        if(headOfPlayList == null)
        {
            Log.d("CustomMusicPlayer", "headOfPlayList is null");
            music.next = music;
            music.previous = music;
            headOfPlayList = music;
            setMusic(music);
            Background.updateMainActivityUI(currentMusic.getName(), isPlaying());
        }
        else
        {
            Log.d("CustomMusicPlayer", "headOfPlayList is not null");
            Music m = headOfPlayList;
            while(m.next != headOfPlayList)
                m = m.next;
            music.next = m.next;
            music.previous = m;
            m.next.previous = music;
            m.next = music;
        }
        Log.d("CustomMusicPlayer", "after add to list");
        length++;
    }

    public void addToNext(Music music)
    {
        Log.d("CustomMusicPlayer", "before add to next");
        if(headOfPlayList == null)
        {
            Log.d("CustomMusicPlayer", "headOfPlayList is null");
            music.next = music;
            music.previous = music;
            headOfPlayList = music;
            setMusic(music);
            Background.updateMainActivityUI(currentMusic.getName(), isPlaying());
        }
        else
        {
            Log.d("CustomMusicPlayer", "headOfPlayList is not null");
            music.next = currentMusic.next;
            music.previous = currentMusic;
            currentMusic.next.previous = music;
            currentMusic.next = music;
        }
        Log.d("CustomMusicPlayer", "after add to next");
        length++;
    }

    private Music random()
    {
        int index = (int) (Math.random() * length);
        Music m = currentMusic;
        for(int i = 0; i < index; i++)
        {
            m = m.next;
        }
        return m;
    }

    public List<Music> getPlayList()
    {
        List<Music> list = new ArrayList<>();
        if(headOfPlayList == null)
            return list;
        Music m = headOfPlayList;
        while(m.next != headOfPlayList)
        {
            list.add(m);
            m = m.next;
        }
        list.add(m);
        return list;
    }










}
