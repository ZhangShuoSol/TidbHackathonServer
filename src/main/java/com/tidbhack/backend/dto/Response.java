package com.tidbhack.backend.dto;

import com.tidbhack.backend.domain.Node;
import com.tidbhack.backend.domain.QueryResult;

import java.util.List;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class Response {
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
