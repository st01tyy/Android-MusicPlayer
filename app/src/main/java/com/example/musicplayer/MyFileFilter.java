package com.example.musicplayer;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter
{
    @Override
    public boolean accept(File file)
    {
        if(file.getName().endsWith(".mp3"))
            return true;
        else
            return false;
    }
}
