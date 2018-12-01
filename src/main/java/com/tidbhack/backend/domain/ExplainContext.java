package com.tidbhack.backend.domain;

import com.tidbhack.backend.dto.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by wenbinsong on 2018/12/1.
 */
public class ExplainContext {
    List<QueryResult> plan;
    Node node;

    public List<QueryResult> getPlan() {
        return plan;
    }

    public void setPlan(List<QueryResult> plan) {
        this.plan = plan;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
