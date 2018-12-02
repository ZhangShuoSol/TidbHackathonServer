package com.tidbhack.backend.controller;

import com.google.gson.Gson;
import com.tidbhack.backend.domain.Node;
import com.tidbhack.backend.dto.Response;
import com.tidbhack.backend.dto.Statement;
import com.tidbhack.backend.dto.TableIndex;
import com.tidbhack.backend.service.ExplainService;
import com.tidbhack.backend.utils.FormatSqlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wenbinsong on 2018/11/30.
 */

@RestController
@Api(value = "/select", description = "Select 语句执行接口")
public class SelectController {

    @Autowired
    private ExplainService explainService;

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ApiOperation(value = "select 语句执行接口", notes = "select 语句执行接口")
    public Response select(
            @ApiParam(value = "执行语句", required = true)
            @RequestBody Statement statement

    ) {

        Response res = explainService.explain("EXPLAIN ANALYZE " + statement.getSql());

        return res;
    }

    @RequestMapping(value = "/formatsql", method = RequestMethod.POST)
    @ApiOperation(value = "获取格式化后的SQL", notes = "获取格式化后的SQL")
    public String formatSql(
            @ApiParam(value = "执行语句", required = true)
            @RequestBody Statement statement
    ) {
        return FormatSqlUtils.getFormatSql(statement.getSql());
    }

    @RequestMapping(value = "/indexadvise", method = RequestMethod.POST)
    @ApiOperation(value = "索引建议", notes = "索引建议")
    public String IndexAdvice(
            @ApiParam(value = "执行语句", required = true)
            @RequestBody Statement statement
    ) {
        return explainService.smartAdviseForIndex(statement.getSql());
    }

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    @ApiOperation(value = "查找表接口", notes = "查找表接口")

    public String getTable(
            @ApiParam(value = "表名", required = true)
            @RequestParam String tablename
    ) {
        String result = explainService.getTable(tablename);
        return result;
    }

    @RequestMapping(value = "/table/indexs", method = RequestMethod.GET)
    @ApiOperation(value = "查找表索引接口", notes = "查找表索引接口")
    public String getTableIndexs( @ApiParam(value = "表名", required = true) @RequestParam String tablename) {
        List<Map<String, Object>> result = explainService.getTableIndexs(tablename);
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @RequestMapping(value = "/table/indexs", method = RequestMethod.POST)
    @ApiOperation(value = "添加表索引接口", notes = "添加表索引接口")
    public String addTableIndexs(@ApiParam(value = "表名", required = true) @RequestBody TableIndex tableIndex) {
        String result = explainService.addTableIndexs(tableIndex);
        return result;
    }

    @RequestMapping(value = "/table/indexs", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除表索引接口", notes = "删除表索引接口")
    public String deleteTableIndexs(@ApiParam(value = "表名", required = true) @RequestParam String table,
                                    @ApiParam(value = "列名", required = true) @RequestParam String columns) {
        String result = explainService.deleteTableIndexs(table,columns);
        return result;
    }

    @RequestMapping(value = "table/names", method = RequestMethod.GET)
    @ApiOperation(value = "查找所有表名接口", notes = "查找所有表名接口")
    public String getAllTableNames() {
        List<String> result = explainService.getAllTableNames("tenant");
        Gson gson = new Gson();
        return gson.toJson(result);
    }

}
