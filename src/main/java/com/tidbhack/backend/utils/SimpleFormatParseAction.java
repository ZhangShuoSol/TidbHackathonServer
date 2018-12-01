package com.tidbhack.backend.utils;

/**
 * Created by wenbinsong on 2018/12/1.
 */
public class SimpleFormatParseAction implements FormatParseAction {
    @Override
    public String FormatStr(String str) {
        String[] temp = str.split(" ");
        String result = "";

        for(int i = 0; i < temp.length; i ++) {
            result = temp[i] + "\n";
        }

        return result;
    }
}
