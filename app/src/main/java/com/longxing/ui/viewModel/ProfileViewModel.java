package com.longxing.ui.viewModel;

/**
 * Created by Zhang Long on 2017/8/7.
 * Media file model
 */
public class ProfileViewModel {

    private long id;
    private String updateTime;
    private String profileName;
    private String profileValue;
    private String profileRemark;
    private String profileTag;

    public ProfileViewModel(long id, String updateTime, String profileName,
                            String profileValue, String profileRemark, String profileTag) {
        this.id = id;
        this.updateTime = updateTime;
        this.profileName = profileName;
        this.profileValue = profileValue;
        this.profileRemark = profileRemark;
        this.profileTag = profileTag;
    }

    public ProfileViewModel() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProfileName() {
        return this.profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileValue() {
        return this.profileValue;
    }

    public void setProfileValue(String profileValue) {
        this.profileValue = profileValue;
    }

    public String getProfileRemark() {
        return this.profileRemark;
    }

    public void setProfileRemark(String profileRemark) {
        this.profileRemark = profileRemark;
    }

    public String getProfileTag() {
        return this.profileTag;
    }

    public void setProfileTag(String profileTag) {
        this.profileTag = profileTag;
    }
}
