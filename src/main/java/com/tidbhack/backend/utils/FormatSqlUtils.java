package com.tidbhack.backend.utils;


import com.alibaba.druid.sql.SQLUtils;
import org.springframework.util.StringUtils;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class FormatSqlUtils {

    public static String getFormatSql(String sql) {
        if(StringUtils.isEmpty(sql)){
            return null;
        }
        return SQLUtils.formatMySql(sql);
    }
}
