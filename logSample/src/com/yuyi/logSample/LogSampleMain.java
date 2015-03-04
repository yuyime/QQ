package com.yuyi.logSample;

public class LogSampleMain {

    public static void main(String args[]) {
        String filepath = "G:\\log\\warn_slowsql.log.2014-11-05";
        String outTitleFilepath = "G:\\log\\warn_slowsql.log.2014-11-05_title";
        String outSampleTitleFilepath = "G:\\log\\warn_slowsql.log.2014-11-05_title_sample";
        String outSampleFilepath = "G:\\log\\warn_slowsql.log.2014-11-05_sample";
        // String filepath = "G:\\log\\test.txt";
        LogSampleAnalysis lsa = new LogSampleAnalysis(filepath);
        // long lineNum = LogSampleAnalysis.analyLineNum(filepath);
        // System.out.println("total Lines：" + lineNum);
        // long errorNum = lsa.analyErrorNum();
        // System.out.println("total errorNum：" + errorNum);
        lsa.readErrorToList();
        lsa.outPutTofileForTile(outTitleFilepath);
        lsa.outPutTofileForSampleTile(outSampleTitleFilepath);
        lsa.outPutTofileForSample(outSampleFilepath);
    }
}
