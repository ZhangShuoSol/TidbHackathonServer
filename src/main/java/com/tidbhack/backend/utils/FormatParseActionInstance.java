package com.tidbhack.backend.utils;

import com.tidbhack.backend.domain.ExplainContextInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenbinsong on 2018/12/1.
 */
public class FormatParseActionInstance {
    Map<String, FormatParseAction> formatParseActions;

    private static FormatParseActionInstance formatParseActionInstance = new FormatParseActionInstance();

    private FormatParseActionInstance() {
        formatParseActions = new HashMap<String, FormatParseAction>();
        putFormatParseActionList("Simple", new SimpleFormatParseAction());
    }

    public static FormatParseActionInstance getInstance() {
        return formatParseActionInstance;
    }

    public void putFormatParseActionList(String sym, FormatParseAction formatParseAction) {
        formatParseActions.put(sym, formatParseAction);
    }

    public String FormatRow(String key, String str) {
        FormatParseAction formatParseAction = formatParseActions.get(key);
        return formatParseAction.FormatStr(str);
    }

}
