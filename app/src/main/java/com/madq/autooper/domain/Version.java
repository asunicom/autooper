package com.madq.autooper.domain;

/**
 * @author madeqiang
 * @version 1.0
 * @date 2019/11/15 13:29
 */
public class Version {
    private String apkDesc;
    private int apkVersion;
    private String apkUrl;
    private String updateTitle;
    private int needForceUpdate;

    public int getNeedForceUpdate() {
        return needForceUpdate;
    }

    public void setNeedForceUpdate(int needForceUpdate) {
        this.needForceUpdate = needForceUpdate;
    }


    public String getApkDesc() {
        return apkDesc;
    }

    public void setApkDesc(String apkDesc) {
        this.apkDesc = apkDesc;
    }

    public int getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(int apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }
}
