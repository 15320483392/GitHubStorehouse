package com.wt.ssmTest.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/27.
 */
public class WdxcQywFiles implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    private long userid;

    private String filename;

    private String filepreview;

    private String filepath;

    private int deleted;

    private Date deletedtime;

    private Date createtime;

    private String filedownurl;

    private BigDecimal filesize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepreview() {
        return filepreview;
    }

    public void setFilepreview(String filepreview) {
        this.filepreview = filepreview;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedtime() {
        return deletedtime;
    }

    public void setDeletedtime(Date deletedtime) {
        this.deletedtime = deletedtime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getFiledownurl() {
        return filedownurl;
    }

    public void setFiledownurl(String filedownurl) {
        this.filedownurl = filedownurl;
    }

    public BigDecimal getFilesize() {
        return filesize;
    }

    public void setFilesize(BigDecimal filesize) {
        this.filesize = filesize;
    }
}
