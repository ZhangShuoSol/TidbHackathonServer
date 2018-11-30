package com.tidbhack.backend.utils;

import org.springframework.util.StringUtils;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class FormatStringUtils {
    public static String formatJsonName(String name){
        if(StringUtils.isEmpty(name)){
            return null;
        }
        return name.replace("  └─","")
                .replace(" └─","")
                .replace(" ├─","")
                .replace(" │","")
                .replace("└─","")
                .replace("│","")
                .replace("├─","");
    }
}
