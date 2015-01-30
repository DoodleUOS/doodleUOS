package team.udacity.uos.doodle.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Doodle {
    @DatabaseField(id=true)
    private int dooNo;
    @DatabaseField
    private int dooMemNo;
    @DatabaseField
    private String dooLoca;
    @DatabaseField
    private String dooCon;
    @DatabaseField
    private String dooUrl;
    @DatabaseField
    private String dooDate;
    @DatabaseField
    private Double dooLat;
    @DatabaseField
    private Double dooLong;
    @DatabaseField
    private String memName;
    @DatabaseField
    private String memFbNo;
    @DatabaseField
    private int tagTar;
    @DatabaseField
    private String memNameT;
    @DatabaseField
    private String memFbNoT;

    public Doodle(){

    }

    public int getDooNo() {
        return dooNo;
    }

    public void setDooNo(int dooNo) {
        this.dooNo = dooNo;
    }

    public int getDooMemNo() {
        return dooMemNo;
    }

    public void setDooMemNo(int dooMemNo) {
        this.dooMemNo = dooMemNo;
    }

    public String getDooLoca() {
        return dooLoca;
    }

    public void setDooLoca(String dooLoca) {
        this.dooLoca = dooLoca;
    }

    public String getDooCon() {
        return dooCon;
    }

    public void setDooCon(String dooCon) {
        this.dooCon = dooCon;
    }

    public String getDooUrl() {
        return dooUrl;
    }

    public void setDooUrl(String dooUrl) {
        this.dooUrl = dooUrl;
    }

    public String getDooDate() {
        return dooDate;
    }

    public void setDooDate(String dooDate) {
        this.dooDate = dooDate;
    }

    public Double getDooLat() {
        return dooLat;
    }

    public void setDooLat(Double dooLat) {
        this.dooLat = dooLat;
    }

    public Double getDooLong() {
        return dooLong;
    }

    public void setDooLong(Double dooLong) {
        this.dooLong = dooLong;
    }

    public String getMemFbNo() { return memFbNo; }

    public void setMemFbNo(String memFbNo) { this.memFbNo = memFbNo; }

    public String getMemName() { return memName; }

    public void setMemName(String memName) { this.memName = memName; }

    public int getTagTar() {
        return tagTar;
    }

    public void setTagTar(int tagTar) {
        this.tagTar = tagTar;
    }

    public String getMemNameT() {
        return memNameT;
    }

    public void setMemNameT(String memNameT) {
        this.memNameT = memNameT;
    }

    public String getMemFbNoT() {
        return memFbNoT;
    }

    public void setMemFbNoT(String memFbNoT) {
        this.memFbNoT = memFbNoT;
    }
}
