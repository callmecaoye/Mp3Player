package com.caoye.model;

import java.io.Serializable;

/**
 * Created by admin on 7/12/16.
 */
public class Mp3Info implements Serializable{
    private String id;
    private String mp3Name;
    private String mp3Size;
    private String lrcName;
    private String lrcSize;

    public Mp3Info() {
        super();
    }

    public Mp3Info(String id, String mp3Name, String mp3Size, String lrcName,
                   String lrcSize) {
        super();
        this.id = id;
        this.mp3Name = mp3Name;
        this.mp3Size = mp3Size;
        this.lrcName = lrcName;
        this.lrcSize = lrcSize;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMp3Name() { return mp3Name; }
    public void setMp3Name(String name) { this.mp3Name = name; }

    public String getMp3Size() { return mp3Size; }
    public void setMp3Size(String size) { this.mp3Size = size; }

    public String getLrcName() { return lrcName; }
    public void setLrcName(String lrcName) { this.lrcName = lrcName; }

    public String getLrcSize() { return lrcSize; }
    public void setLrcSize(String lrcSize) { this.lrcSize = lrcSize; }

    @Override
    public String toString() {
        return "Mp3Info [id=" + id + ", lrcName=" + lrcName + ", lrcSize="
                + lrcSize + ", mp3Name=" + mp3Name + ", mp3Size=" + mp3Size
                + "]";
    }
}
