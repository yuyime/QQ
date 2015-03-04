package com.yuyi.logSample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorIfno {
    private Date occurTime;
    private String occurFileName;
    private String occurFileLineNum;
    private ErrorType eType;
    private String content;
    private String title;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String titleToString() {

        return "[" + occurTime + "] " + "[" + eType + "] " + "[" + title + "]";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public void setOccurTime(String occurTimestr) {
        try {
            this.occurTime = sdf.parse(occurTimestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getOccurFileName() {
        return occurFileName;
    }

    public void setOccurFileName(String occurFileName) {
        this.occurFileName = occurFileName;
    }

    public String getOccurFileLineNum() {
        return occurFileLineNum;
    }

    public void setOccurFileLineNum(String occurFileLineNum) {
        this.occurFileLineNum = occurFileLineNum;
    }

    public ErrorType geteType() {
        return eType;
    }

    public void seteType(ErrorType eType) {
        this.eType = eType;
    }

    public void seteType(String eType) {

        this.eType = Enum.valueOf(ErrorType.class, eType);
    }

}
