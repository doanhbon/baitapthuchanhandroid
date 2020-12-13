package vn.edu.baitapthuchanhandroid.entities;

import java.util.Objects;

public class Music {
    private int musicResource;
    private String singerName;
    private String name;
    private int avatar;

    public Music(int musicResource, String singerName, String name, int avatar) {
        this.musicResource = musicResource;
        this.singerName = singerName;
        this.name = name;
        this.avatar = avatar;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getMusicResource() {
        return musicResource;
    }

    public void setMusicResource(int musicResource) {
        this.musicResource = musicResource;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Music)) return false;
        Music music = (Music) o;
        return getMusicResource() == music.getMusicResource();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMusicResource());
    }
}
