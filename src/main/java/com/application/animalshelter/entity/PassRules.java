package com.application.animalshelter.entity;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class PassRules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String filePath;
    private long fileSize;
    private String MediaType;
    // @Lob
    private byte[] data;
    //аватар уменьшенный в размере

    public PassRules() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassRules passRules = (PassRules) o;
        return Objects.equals(Id, passRules.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return "PassRules{" +
                "Id=" + Id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", MediaType='" + MediaType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
