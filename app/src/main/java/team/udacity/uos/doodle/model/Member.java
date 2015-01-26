package team.udacity.uos.doodle.model;

public class Member {
    private int memNo;
    private String memName;
    private String memFbNo;
    private String memFbUrl;
    private String memId;

    public Member(){

    }

    public Member(int memNo, String memName, String memFbNo, String memFbUrl, String memId) {
        this.memNo = memNo;
        this.memName = memName;
        this.memFbNo = memFbNo;
        this.memFbUrl = memFbUrl;
        this.memId = memId;
    }

    public int getMemNo() {
        return memNo;
    }

    public void setMemNo(int memNo) {
        this.memNo = memNo;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getMemFbNo() {
        return memFbNo;
    }

    public void setMemFbNo(String memFbNo) {
        this.memFbNo = memFbNo;
    }

    public String getMemFbUrl() {
        return memFbUrl;
    }

    public void setMemFbUrl(String memFbUrl) {
        this.memFbUrl = memFbUrl;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }
}
