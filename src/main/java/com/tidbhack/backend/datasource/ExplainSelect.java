package com.tidbhack.backend.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class ExplainSelect {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Explain> getExplain(String sql) {
        System.out.println("xxx");
        System.out.println(jdbcTemplate);
        return jdbcTemplate.query(sql, new ExplainRowMapper());
    }
}
