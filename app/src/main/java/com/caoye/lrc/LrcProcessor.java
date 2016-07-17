package com.caoye.lrc;

import com.caoye.utils.FileUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 7/15/16.
 */
public class LrcProcessor {
    private long curTime = 0;
    private String curContent = null;

    Queue<Long> times = new LinkedList();
    Queue<String> messages = new LinkedList<>();

    public ArrayList<Queue> process(InputStream stream) {
        try {
            InputStreamReader inputReader = new InputStreamReader(stream, "GBK");
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Queue> list = new ArrayList<>();
        list.add(times);
        list.add(messages);

        return list;
    }

    private void parseLine(String str) {
        String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()){
            int groupCount = matcher.groupCount();
            for (int i = 0; i <= groupCount; i++) {
                String timeStr = matcher.group(i);
                if (i == 1) {
                    curTime = str2Long(timeStr);
                }
            }

            String[] contents = pattern.split(str);
            for (int i = 0; i < contents.length; i++) {
                if (i == contents.length - 1) {
                    curContent = contents[i];
                }
            }

            times.offer(curTime);
            messages.offer(curContent);
        }
    }

    /**
     * transfer timeline to ms
     * @param timeStr
     * @return
     */
    public Long str2Long(String timeStr) {
        String[] s = timeStr.split(":");
        int min = Integer.parseInt(s[0]);
        String[] ss = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min * 60 * 1000 + sec * 1000 + mill * 10L;
    }


}
