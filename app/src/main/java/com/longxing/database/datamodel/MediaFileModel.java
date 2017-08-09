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
    private Long id;
    @Property(nameInDb = "UpdateTime")
    private String updateTime;
    @Property(nameInDb = "MediaPath")
    private String mediaPath;
    @Property(nameInDb = "MediaDuration")
    private String mediaDuration;
    @Property(nameInDb = "MediaName")
    private String mediaName;
    @Property(nameInDb = "MediaTag")
    private String mediaTag;
    @Generated(hash = 1672648145)
    public MediaFileModel(Long id, String updateTime, String mediaPath,
            String mediaDuration, String mediaName, String mediaTag) {
        this.id = id;
        this.updateTime = updateTime;
        this.mediaPath = mediaPath;
        this.mediaDuration = mediaDuration;
        this.mediaName = mediaName;
        this.mediaTag = mediaTag;
    }
    @Generated(hash = 1707378537)
    public MediaFileModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
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
