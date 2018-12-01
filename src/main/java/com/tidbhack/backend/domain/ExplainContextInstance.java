package com.tidbhack.backend.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenbinsong on 2018/12/1.
 */
public class ExplainContextInstance {

    private static ExplainContextInstance explainContextInstance = new ExplainContextInstance();

    private ExplainContextInstance() {
        explainContextMap = new HashMap<String, ExplainContext>();
    }

    public static ExplainContextInstance getInstance() {
        return explainContextInstance;
    }
    private Map<String, ExplainContext> explainContextMap;

    public void addContext(String uuid, ExplainContext explainContext) {
        explainContextMap.put(uuid, explainContext);
    }
}
