package com.tidbhack.backend.service;

import com.tidbhack.backend.domain.Node;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public interface ExplainService {
    Node explain(String sql);
}
