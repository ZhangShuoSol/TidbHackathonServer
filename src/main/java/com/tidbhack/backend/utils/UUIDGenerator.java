package com.tidbhack.backend.utils;

import java.util.UUID;

/**
 * Created by wenbinsong on 2018/12/1.
 */
public class UUIDGenerator {
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
