package com.example.musicplayer;

public class Music
{
    private String name;
    private String artist;
    private String path;
    public Music previous, next;

    public Music(String name, String artist, String path)
    {
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.previous = this.next = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
