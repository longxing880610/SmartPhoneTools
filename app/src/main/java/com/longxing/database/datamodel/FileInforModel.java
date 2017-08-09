package com.longxing.database.datamodel;

import org.greenrobot.greendao.annotation.*;

/**
 * Created by Zhang Long on 2017/7/23.
 *
 */
@Entity
public class FileInforModel{

    @Id(autoincrement = true)
    @Property(nameInDb = "Id")
    private Long id;
    @Property(nameInDb = "UpdateTime")
    private String updatetime;
    @Property(nameInDb = "FileTag")
    private String fileTag;
    @Property(nameInDb = "Code")
    private String code;
    @Property(nameInDb = "FileName")
    private String fileName;
    @Property(nameInDb = "FilePath")
    private String filePath;
    @Property(nameInDb = "FileSize")
    private String fileSize;
    @Property(nameInDb = "FileDir")
    private String fileDir;
    @Property(nameInDb = "FileCreatetime")
    private String fileCreatetime;
    @Property(nameInDb = "IsHide")
    private String isHide;
    @Property(nameInDb = "IsFileOrFalseDir")
    private String isFileOrFalseDir;


    @Generated(hash = 536684973)
    public FileInforModel(Long id, String updatetime, String fileTag, String code,
            String fileName, String filePath, String fileSize, String fileDir,
            String fileCreatetime, String isHide, String isFileOrFalseDir) {
        this.id = id;
        this.updatetime = updatetime;
        this.fileTag = fileTag;
        this.code = code;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileDir = fileDir;
        this.fileCreatetime = fileCreatetime;
        this.isHide = isHide;
        this.isFileOrFalseDir = isFileOrFalseDir;
    }
    @Generated(hash = 1815834255)
    public FileInforModel() {
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileDir() {
        return this.fileDir;
    }
    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
    public String getFileCreatetime() {
        return this.fileCreatetime;
    }
    public void setFileCreatetime(String fileCreatetime) {
        this.fileCreatetime = fileCreatetime;
    }
    public String getIsHide() {
        return this.isHide;
    }
    public void setIsHide(String isHide) {
        this.isHide = isHide;
    }
    public String getIsFileOrFalseDir() {
        return this.isFileOrFalseDir;
    }
    public void setIsFileOrFalseDir(String isFileOrFalseDir) {
        this.isFileOrFalseDir = isFileOrFalseDir;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getUpdatetime() {
        return this.updatetime;
    }
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public String getFileTag() {
        return this.fileTag;
    }
    public void setFileTag(String fileTag) {
        this.fileTag = fileTag;
    }
}
