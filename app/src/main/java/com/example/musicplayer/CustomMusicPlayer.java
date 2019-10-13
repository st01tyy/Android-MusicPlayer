package com.example.musicplayer;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.util.Log;

public class CustomMusicPlayer
{
    private MediaPlayer mediaPlayer;
    private Music currentMusic;
    private Music headOfPlayList;
    private int mode;

    public CustomMusicPlayer()
    {
        mediaPlayer = new MediaPlayer();
        currentMusic = headOfPlayList = null;
        mode = 0;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                int temp = mode % 3;
                if(temp == 0)
                {
                    setMusic(currentMusic.next);
                    play();
                }
                else if(temp == 1)
                {
                    setMusic(currentMusic);
                    play();
                }
                else
                {
                    Music music = headOfPlayList;
                    while((int) (Math.random() * 10) > 5)
                        music = music.next;
                    setMusic(music);
                    play();
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
            music.previous = currentMusic;
            if(currentMusic != null)
            {
                currentMusic.next = music;
                music.next = currentMusic.next;
                if(currentMusic.next != null)
                    currentMusic.next.previous = music;
            }
            else
            {
                headOfPlayList = music;
                headOfPlayList.next = music;
                music.previous = headOfPlayList;
            }
            currentMusic = music;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
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
        setMusic(currentMusic.previous);
        String[] arr = new String[]{currentMusic.getName(), currentMusic.getArtist()};
        play();
        return arr;
    }

    public String[] playNext()
    {
        setMusic(currentMusic.next);
        String[] arr = new String[]{currentMusic.getName(), currentMusic.getArtist()};
        play();
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
            headOfPlayList = music;
            headOfPlayList.next = music;
            music.previous = headOfPlayList;
        }
        else
        {
            Music m = headOfPlayList;
            while(m.next != headOfPlayList)
                m = m.next;
            m.next = music;
            music.previous = m;
            music.next = headOfPlayList;
            headOfPlayList.previous = music;
        }
        Log.d("CustomMusicPlayer", "after add to list");
    }

    public void addToNext(Music music)
    {
        Log.d("CustomMusicPlayer", "before add to next");
        if(headOfPlayList == null)
        {
            headOfPlayList = music;
            headOfPlayList.next = music;
            music.previous = headOfPlayList;
        }
        else
        {
            music.next = currentMusic.next;
            music.previous = currentMusic;
            if(currentMusic.next != null)
                currentMusic.next.previous = music;
            currentMusic.next = music;
        }
        Log.d("CustomMusicPlayer", "after add to next");
    }








}
