package team.udacity.uos.doodle.model;

/**
 * Created by JYPark88 on 2015-01-28.
 */
public class TimeLine {
    private int dooNo;
    private int dooMemNo;
    private String dooLoca;
    private String dooDate;
    private String memFbNo;
    private int tagTar;
    private String memNameT;
    private String memFbNoT;

    public TimeLine(int dooNo, int dooMemNo, String dooLoca, String dooDate, String memFbNo, int tagTar, String memNameT, String memFbNoT) {
        this.dooNo = dooNo;
        this.dooMemNo = dooMemNo;
        this.dooLoca = dooLoca;
        this.dooDate = dooDate;
        this.memFbNo = memFbNo;
        this.tagTar = tagTar;
        this.memNameT = memNameT;
        this.memFbNoT = memFbNoT;
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

    public String getDooDate() {
        return dooDate;
    }

    public void setDooDate(String dooDate) {
        this.dooDate = dooDate;
    }

    public String getMemFbNo() {
        return memFbNo;
    }

    public void setMemFbNo(String memFbNo) {
        this.memFbNo = memFbNo;
    }

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


