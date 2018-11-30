package com.tidbhack.backend.datasource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by wenbinsong on 2018/11/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExplainTest {
    @Test
    public void TestExplain() {
        ExplainSelect explainSelect = new ExplainSelect();
        List<Explain> explainList = explainSelect.getExplain("explain ANALYZE select * from testlib join t1 on testlib.id = t1.id;");
        for (Explain explain: explainList) {
            System.out.println(explain.getId());
            System.out.println(explain.getCount());
            System.out.println(explain.getTask());
            System.out.println(explain.getOperator_info());
            System.out.println(explain.getExecution_info());
        }
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void Test123() {
        List<Explain> explainList = jdbcTemplate.query("explain ANALYZE select * from testlib join t1 on testlib.id = t1.id;", new ExplainRowMapper());
        for (Explain explain: explainList) {
            System.out.println(explain.getId());
            System.out.println(explain.getCount());
            System.out.println(explain.getTask());
            System.out.println(explain.getOperator_info());
            System.out.println(explain.getExecution_info());
        }
    }

}
