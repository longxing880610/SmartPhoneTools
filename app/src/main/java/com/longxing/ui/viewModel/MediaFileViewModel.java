package com.longxing.ui.viewModel;

/**
 * Created by Zhang Long on 2017/8/7.
 * Media file model
 */
public class MediaFileViewModel {

    private long id;
    private String updateTime;
    private String mediaPath;
    private String mediaDuration;
    private String mediaName;
    private String mediaTag;

    public MediaFileViewModel(long id, String mediaPath, String mediaDuration,
                          String mediaName, String mediaTag, String updateTime) {
        this.id = id;
        this.mediaPath = mediaPath;
        this.mediaDuration = mediaDuration;
        this.mediaName = mediaName;
        this.mediaTag = mediaTag;
        this.updateTime = updateTime;
    }

    public MediaFileViewModel() {
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
