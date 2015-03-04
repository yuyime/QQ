package com.yuyi.logSample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogSampleAnalysis {

    private static String filePath;
    private static File file;
    private static String error_Begin_Pattern = "\\[[0-9]{4}-[0-9]{2}-[0-9]{2}\\s^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?\\]";
    private static String error = ".*\\[ERROR\\].*";
    private static String warn = ".*\\[WARN\\].*";
    private static Pattern pattern_error = Pattern.compile(error);
    private static Pattern pattern_warn = Pattern.compile(warn);
    private static Pattern pattern_error_split = Pattern.compile("\\[(.*)\\]\\s\\[(.*)\\]\\s\\[(.*)\\].*");
    private List<ErrorIfno> errorList;
    private Map<String, ErrorIfno> errorMap = new HashMap<String, ErrorIfno>();

    public LogSampleAnalysis() {
    }

    public LogSampleAnalysis(String name) {
        filePath = name;
        file = new File(name);
    }

    public LogSampleAnalysis(File f) {
        file = f;
        filePath = file.getAbsolutePath();
    }

    public static String getValue(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public long analyLineNum() {
        return analyLineNum(null);
    }

    public static long analyLineNum(String fileName) {
        File _file;
        if (fileName == null) {
            _file = file;
            if (file == null) {
                return 0;
            }
        } else {
            _file = new File(fileName);
        }

        long fileLength = _file.length();
        LineNumberReader lr = null;
        try {
            lr = new LineNumberReader(new FileReader(_file));
            if (lr != null) {
                lr.skip(fileLength);
                long lines = lr.getLineNumber();
                lines++;
                lr.close();
                return lines;
            }
        } catch (IOException e) {
            if (lr != null) {
                try {
                    lr.close();
                } catch (IOException ee) {
                }
            }
        }
        return 0;
    }

    public boolean isErrorBegin(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sf.format(date);

        Pattern p = Pattern.compile(this.error_Begin_Pattern);
        Matcher m = p.matcher(dateStr);
        boolean dateFlag = m.matches();
        if (!dateFlag) {
            System.out.println("格式错误");
        }
        System.out.println("格式正确");

        return false;
    }

    private boolean Mymatcher(Pattern p, String str) {
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public long analyErrorNum() {
        long count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            long progress = 0;
            while ((line = br.readLine()) != null) {
                if (Mymatcher(pattern_error, line)) {
                    count++;
                }

                progress++;
                if (progress % 1000 == 0) {
                    System.out.println("进度" + (progress / 1000) + "K line");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void readErrorToList() {
        errorList = new ArrayList<ErrorIfno>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            long progress = 0;
            StringBuffer buf = new StringBuffer();
            ErrorIfno curError = null;
            boolean flag = false;
            while ((line = br.readLine()) != null) {
                // 匹配到新的occure开始
                if (Mymatcher(pattern_error, line)) {
                    // 处理上一个
                    if (flag && curError != null) {
                        curError.setContent(buf.toString());
                    }

                    ErrorIfno errOne = new ErrorIfno();
                    // 置空
                    buf.setLength(0);
                    curError = errOne;

                    // 获得信息头
                    Matcher m = pattern_error_split.matcher(line);
                    if (m.find()) {
                        errOne.setOccurTime(m.group(1));
                        errOne.seteType(m.group(2));
                        errOne.setOccurFileName(m.group(3));
                    }
                    errOne.setTitle(line);
                    errorList.add(errOne);

                    // 判断是否是第一个此类型的，记录到map中
                    if (errorMap.get(line) == null) {
                        errorMap.put(line, errOne);
                        flag = true;
                    } else {
                        flag = false;
                    }

                }

                // 记录完成occur信息
                if (flag && curError != null) {
                    buf.append(line);
                }

                progress++;
                if (progress % 10000 == 0) {
                    System.out.println("进度" + (progress / 10000) + "W line");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean outPutTofileForTile(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (ErrorIfno errorIfno : errorList) {
                // XXX
                bw.write(errorIfno.titleToString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean outPutTofileForSampleTile(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Entry<String, ErrorIfno> entry : errorMap.entrySet()) {
                // XXX
                bw.write(entry.getKey());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean outPutTofileForSample(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Entry<String, ErrorIfno> entry : errorMap.entrySet()) {
                // XXX
                bw.write(entry.getValue().getContent());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
