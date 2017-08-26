package com.hengda.hdtilemap.bean;

import android.database.Cursor;

import com.hengda.hdtilemap.app.AppConfig;
import com.hengda.zwf.hdtilemap.entity.BaseExhibit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Tailyou
 * 时间：2016/3/7 09:06
 * 邮箱：tailyou@163.com
 * 描述：展项
 */
public class ExhibitBean extends BaseExhibit implements Serializable {

    private int mapId;
    private int hallId;
    private int picNum;
    private String url360;
    private String url3d;
    private String urlKlg;
    private int isKey;
    private int road1;
    private int road2;
    private int road3;
    private int digPicNum;
    private String exhibitMp3Path;
    private String exhibitHtmlPath;
    private String exhibitIconPath;
    private List<String> exhibitPicPaths = new ArrayList<>();

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public String getUrl360() {
        return url360;
    }

    public void setUrl360(String url360) {
        this.url360 = url360;
    }

    public String getUrl3d() {
        return url3d;
    }

    public void setUrl3d(String url3d) {
        this.url3d = url3d;
    }

    public String getUrlKlg() {
        return urlKlg;
    }

    public void setUrlKlg(String urlKlg) {
        this.urlKlg = urlKlg;
    }

    public int getIsKey() {
        return isKey;
    }

    public void setIsKey(int isKey) {
        this.isKey = isKey;
    }

    public int getRoad1() {
        return road1;
    }

    public void setRoad1(int road1) {
        this.road1 = road1;
    }

    public int getRoad2() {
        return road2;
    }

    public void setRoad2(int road2) {
        this.road2 = road2;
    }

    public int getRoad3() {
        return road3;
    }

    public void setRoad3(int road3) {
        this.road3 = road3;
    }

    public int getDigPicNum() {
        return digPicNum;
    }

    public void setDigPicNum(int digPicNum) {
        this.digPicNum = digPicNum;
    }

    public String getExhibitMp3Path() {
        return exhibitMp3Path;
    }

    public void setExhibitMp3Path(String exhibitMp3Path) {
        this.exhibitMp3Path = exhibitMp3Path;
    }

    public String getExhibitHtmlPath() {
        return exhibitHtmlPath;
    }

    public void setExhibitHtmlPath(String exhibitHtmlPath) {
        this.exhibitHtmlPath = exhibitHtmlPath;
    }

    public String getExhibitIconPath() {
        return exhibitIconPath;
    }

    public void setExhibitIconPath(String exhibitIconPath) {
        this.exhibitIconPath = exhibitIconPath;
    }

    public List<String> getExhibitPicPaths() {
        return exhibitPicPaths;
    }

    public void addPicPath2List(String picPath) {
        this.exhibitPicPaths.add(picPath);
    }

    /**
     * Cursor 转 ExhibitBean
     *
     * @param cursor
     * @return
     */
    public static ExhibitBean cursor2Exhibit(Cursor cursor) {
        ExhibitBean exhibitBean = new ExhibitBean();
        String fileNo = String.format("%04d", cursor.getInt(0));
        exhibitBean.setFileNo(fileNo);
        exhibitBean.setAutoNo(cursor.getInt(1));
        exhibitBean.setMapId(cursor.getInt(2));
        exhibitBean.setHallId(cursor.getInt(3));
        exhibitBean.setExhibitName(cursor.getString(4));
        exhibitBean.setLocX(cursor.getDouble(5));
        exhibitBean.setLocY(cursor.getDouble(6));
        exhibitBean.setUrl360(cursor.getString(7));
        exhibitBean.setUrl3d(cursor.getString(8));
        exhibitBean.setUrlKlg(cursor.getString(9));
        exhibitBean.setIsKey(cursor.getInt(10));
        exhibitBean.setRoad1(cursor.getInt(11));
        exhibitBean.setRoad2(cursor.getInt(12));
        exhibitBean.setRoad3(cursor.getInt(13));
        exhibitBean.setPicNum(cursor.getInt(14));
        exhibitBean.setDigPicNum(cursor.getInt(15));
        exhibitBean.setExhibitMp3Path(AppConfig.getDefaultFileDir() + "exhibit/" + fileNo + "/CHINESE/" + fileNo + ".mp3");
        exhibitBean.setExhibitHtmlPath(AppConfig.getDefaultFileDir() + "exhibit/" + fileNo + "/CHINESE/" + fileNo + ".html");
        exhibitBean.setExhibitIconPath(AppConfig.getDefaultFileDir() + "exhibit/" + fileNo + "/image/list_img.png");
        for (int i = 0; i < exhibitBean.getPicNum(); i++) {
            exhibitBean.addPicPath2List(AppConfig.getDefaultFileDir() + "exhibit/" + fileNo + "/image/img" + (i + 1) + ".png");
        }
        exhibitBean.setExhibitMapPicLg(AppConfig.getDefaultFileDir() + "exhibit/" + fileNo + "/image/map_icon.png");
        String locSmall = cursor.getInt(10) == 1 ? "map_icon.png" : "map_icon2.png";
        exhibitBean.setExhibitMapPicSm(AppConfig.getDefaultFileDir() + "exhibit/" + fileNo + "/image/" + locSmall);
        return exhibitBean;
    }

}
