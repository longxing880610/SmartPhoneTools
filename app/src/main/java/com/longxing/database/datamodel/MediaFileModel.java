package com.longxing.database.datamodel;

import org.greenrobot.greendao.annotation.*;

/**
 * Created by Zhang Long on 2017/8/7.
 * Media file model
 */
@Entity
public class MediaFileModel {

    @Id(autoincrement = true)
    @Property(nameInDb = "Id")
    public long id;
    @Property(nameInDb = "MediaPath")
    public String mediaPath;
    @Property(nameInDb = "MediaDuration")
    public String mediaDuration;
    @Property(nameInDb = "MediaName")
    public String mediaName;
    @Property(nameInDb = "MediaTag")
    public String mediaTag;
    @Property(nameInDb = "MediaName")
    public String updateTime;
    @Generated(hash = 1956131761)
    public MediaFileModel(long id, String mediaPath, String mediaDuration,
            String mediaName, String mediaTag, String updateTime) {
        this.id = id;
        this.mediaPath = mediaPath;
        this.mediaDuration = mediaDuration;
        this.mediaName = mediaName;
        this.mediaTag = mediaTag;
        this.updateTime = updateTime;
    }
    @Generated(hash = 1707378537)
    public MediaFileModel() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getMediaPath() {
        return this.mediaPath;
    }
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
    public String getMediaDuration() {
        return this.mediaDuration;
    }
    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }
    public String getMediaName() {
        return this.mediaName;
    }
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
    public String getMediaTag() {
        return this.mediaTag;
    }
    public void setMediaTag(String mediaTag) {
        this.mediaTag = mediaTag;
    }
    public String getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
