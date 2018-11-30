package com.tidbhack.backend.datasource;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class ExplainRowMapper implements RowMapper<Explain> {
    @Override
    public Explain mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Explain explain = new Explain();
        explain.setId(rs.getString("id"));
        explain.setCount(rs.getString("count"));
        explain.setTask(rs.getString("task"));
        explain.setOperator_info(rs.getString("operator info"));
        explain.setExecution_info(rs.getString("execution info"));
        return explain;
    }

}
