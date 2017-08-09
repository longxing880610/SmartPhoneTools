package com.longxing.database.datamodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhang Long on 2017/8/7.
 * Media file model
 */
@Entity
public class ProfileModel {

    @Id(autoincrement = true)
    @Property(nameInDb = "Id")
    private long id;
    @Property(nameInDb = "UpdateTime")
    private String updateTime;
    @Index(unique = true)
    @Property(nameInDb = "ProfileName")
    private String profileName;
    @Property(nameInDb = "ProfileValue")
    private String profileValue;
    @Property(nameInDb = "ProfileRemark")
    private String profileRemark;
    @Property(nameInDb = "ProfileTag")
    private String profileTag;

    @Keep
    public ProfileModel(String profileName, String profileValue) {
        //this.id = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        this.updateTime = df.format(new Date());
        this.profileName = profileName;
        this.profileValue = profileValue;
        this.profileRemark = null;
        this.profileTag = "";
    }

    @Generated(hash = 838994705)
    public ProfileModel(long id, String updateTime, String profileName,
            String profileValue, String profileRemark, String profileTag) {
        this.id = id;
        this.updateTime = updateTime;
        this.profileName = profileName;
        this.profileValue = profileValue;
        this.profileRemark = profileRemark;
        this.profileTag = profileTag;
    }

    @Generated(hash = 607837135)
    public ProfileModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
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

    public void setId(long id) {
        this.id = id;
    }
}
