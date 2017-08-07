package com.longxing.ui.viewModel;

import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Zhang Long on 2017/7/23.
 */
public class FileInferViewModel {

    private long id;
    @Property(nameInDb = "Code")
    private String code;
    private String updatetime;
    private String fileName;
    private String filePath;
    private String fileSize;
    private String fileDir;
    private String fileCreatetime;
    private String isHide;
    private String isFileOrFalseDir;


    public FileInferViewModel(long id, String code, String updatetime, String fileName,
                              String filePath, String fileSize, String fileDir, String fileCreatetime,
                              String isHide, String isFileOrFalseDir) {
        this.id = id;
        this.code = code;
        this.updatetime = updatetime;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileDir = fileDir;
        this.fileCreatetime = fileCreatetime;
        this.isHide = isHide;
        this.isFileOrFalseDir = isFileOrFalseDir;
    }

    public FileInferViewModel() {
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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
}
