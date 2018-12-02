package com.tidbhack.backend.service;

import com.google.gson.Gson;
import com.tidbhack.backend.datasource.Explain;
import com.tidbhack.backend.datasource.ExplainRowMapper;
import com.tidbhack.backend.domain.*;
import com.tidbhack.backend.dto.Response;
import com.tidbhack.backend.dto.TableIndex;
import com.tidbhack.backend.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import static java.lang.Math.toIntExact;

import java.util.*;

/**
 * Created by wenbinsong on 2018/11/30.
 */
@Service
public class ExplainServiceImpl implements ExplainService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Response explain(String sql) {
        System.out.println(sql);
        List<Explain> explainList = jdbcTemplate.query(sql, new ExplainRowMapper());

        ExplainParser explainParser = new ExplainParser();

        List<QueryResult> queryResults = new ArrayList<QueryResult>();
        for (Explain explain: explainList) {
            QueryResult queryResult = new QueryResult();
            queryResult.setId(explain.getId());
            queryResult.setCount(explain.getCount());
            queryResult.setTask(explain.getTask());
            queryResult.setOperatorinfo(explain.getOperator_info());
            queryResult.setOperatorinfo(explain.getExecution_info());
            queryResult.setUuid(explain.getUuid());
            queryResults.add(queryResult);
        }


        for (Explain explain: explainList) {
            explainParser.HandleNode(new Node(explain.getUuid(), explain.getId(), explain.getTask(), explain.getCount(), explain.getOperator_info(), explain.getExecution_info() ));
        }

        Response response = new Response();
        response.setPlan(queryResults);
        response.setNode(explainParser.getRoot());

        ExplainContextInstance explainContextInstance = ExplainContextInstance.getInstance();
        ExplainContext explainContext = new ExplainContext();
        //设置用时最长的数据链
        Node root = setNodeColor(explainParser.getRoot());
        explainContext.setNode(root);
        //explainContext.setNode(explainParser.getRoot());
        explainContext.setPlan(queryResults);
        String uuid = UUIDGenerator.getUUID32();
        explainContextInstance.addContext(uuid, explainContext);

        response.setUuid(uuid);
        return response;
    }

    @Override
    public String getTable(String name) {

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from " + name + " limit 0");
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        for (int i = 1; i <= columnCount; i++) {
            Map<String,String> fieldMap = new HashMap<String,String>();
            fieldMap.put("ColumnName", metaData.getColumnName(i));
            fieldMap.put("ColumnType", String.valueOf(getColumnBase(name, metaData.getColumnName(i))));
            fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
            fieldMap.put("CatalogName", metaData.getCatalogName(i));
            fieldMap.put("ColumnClassName", metaData.getColumnClassName(i));
            fieldMap.put("ColumnLabel", metaData.getColumnLabel(i));
            fieldMap.put("Precision", String.valueOf(metaData.getPrecision(i)));
            fieldMap.put("Scale", String.valueOf(metaData.getScale(i)));
            fieldMap.put("SchemaName", metaData.getSchemaName(i));
            fieldMap.put("TableName", metaData.getTableName(i));
            fieldMap.put("SchemaName", metaData.getSchemaName(i));
            list.add(fieldMap);
        }
        Gson gson = new Gson();
        String result = gson.toJson(list);

        return result;
    }

    public float getColumnBase(String table_name, String column_name) {
        String sql = "select count(distinct " + column_name + ") from " + table_name + ";";
        Map<String, Object> countmap = jdbcTemplate.queryForMap(sql);
        String key = "count(distinct " + column_name + ")";
        Integer count = toIntExact((Long)countmap.get(key));


        String sql1 = "select count(*) from " + table_name + ";";
        Map<String, Object> totalmap = jdbcTemplate.queryForMap(sql1);

        String key1 = "count(*)";
        Integer total = toIntExact((Long)totalmap.get(key1));

        System.out.println("===============");
        System.out.println(count);
        System.out.println(total);
        System.out.println((float)count / (float)total);
        System.out.println(String.valueOf((float)count / (float)total));
        System.out.println("===============");

        return (float) count / (float) total;
    }

    @Override
    public List<Map<String, Object>> getTableIndexs(String name) {
        return jdbcTemplate.queryForList("show index from "+name+";");
    }

    @Override
    public String addTableIndexs(TableIndex tableIndex) {
        String table = tableIndex.getTable();
        String columns = tableIndex.getColumns();
        if(!StringUtils.isEmpty(table) && !StringUtils.isEmpty(columns)){
            List<Map<String, Object>> indexs = getTableIndexs(table);
            String[] cs = columns.split(",");
            for(String column:cs){
                String indexName = "index_"+column;
                //检查索引中是否有当前要加的索引
                if(!checkHasIndex(indexs,indexName)){
                    jdbcTemplate.execute("alter table "+table+" add index "+indexName+" ("+columns+") ;");
                }
            }
            return "success";
        }
        return "failure";
    }

    private boolean checkHasIndex(List<Map<String, Object>> indexs,String indexName){
        boolean has = false;
        if(indexs.size()>0){
            for(Map map:indexs){
                if(map.get("Key_name").equals(indexName)){
                    has = true;
                    break;
                }
            }
        }
        return has;
    }

    @Override
    public String deleteTableIndexs(String table, String columns) {
        if(!StringUtils.isEmpty(table) && !StringUtils.isEmpty(columns)){
            String[] cs = columns.split(",");
            for(String column:cs){
                String indexName = "index_"+column;
                jdbcTemplate.execute("drop index "+indexName+" on "+table+" ;");
            }
            return "success";
        }
        return "failure";
    }

    @Override
    public List<String> getAllTableNames(String dbName) {
        return jdbcTemplate.queryForList("select table_name  from information_schema.tables  where table_schema='"+dbName+"';",String.class);
    }

    private List<String> getRuleBySql(String sql){
        List<String> messages = new ArrayList<>();
        //获取所有sql的表名
        boolean rule1 = true;//true无问题，false有问题
        boolean rule3 = true;
        List<String> tempTbs = getTableInSql(sql);
        for(String tempTb:tempTbs){
            //获取所有的索引
            List<Map<String, Object>> indexs = jdbcTemplate.queryForList("show index from " + tempTb + ";");
            if(indexs.size()>5){
                rule3 = false;
            }
            for(Map index:indexs){
                String columnName = (String) index.get("Column_name");
                sql = sql.replace(" ","");
                if(sql.indexOf(""+columnName+"=")<0 && sql.indexOf(""+columnName+"like")<0 && sql.indexOf(""+columnName+">")<0
                        && sql.indexOf(""+columnName+"<")<0 && sql.indexOf(""+columnName+"<=")<0 && sql.indexOf(""+columnName+">=")<0){
                    rule1 = false;
                }
            }
        }
        if(!rule1){
            messages.add("适合索引的列是出现在where子句中的列，或者连接子句中指定的列。");
        }
        if(!rule3){
            messages.add("不要过度索引。索引需要额外的磁盘空间，并降低写操作的性能。在修改表内容的时候，索引会进行更新甚至重构，索引列越多，这个时间就会越长。所以只保持需要的索引有利于查询即可。");
        }

        return messages;
    }

    private List<String> getTableInSql(String sql){
        List<String> tempTbs = new ArrayList<>();
        List<String> tables = jdbcTemplate.queryForList("select table_name  from information_schema.tables  where table_schema='tenant';",String.class);
        String[] items = sql.split(" ");
        for(String item:items){
            for(String table:tables){
                if(item.equals(table)){
                    tempTbs.add(table);
                }
            }
        }
        return tempTbs;
    }

    private Node setNodeColor(Node root){
        Map map = new HashMap<>();
        Map<String,Node> treeMap = new HashMap();
        buildParentList(map,treeMap,root);
        String maxId = (String) map.get("maxId");
        findParent(treeMap,root,maxId);
        return root;
    }
    /**
     * 获取用时最长的叶子结点
     * @param map
     * @param node
     */
    private void getMaxTimeChildNode(Map map,Node node){
        if(!StringUtils.isEmpty(node.getName()) && node.getName().indexOf("TableReader")>-1){
            if(node.getInfo()!=null && node.getInfo().getExecuteinfoMap()!=null && node.getInfo().getExecuteinfoMap().size()>0){
                String id = node.getUuid();
                String time = ((String) node.getInfo().getExecuteinfoMap().get("time"));
                double newTime = formatTime(time);
                if(map.size()>0){
                    double oldTime = (Double) map.get("maxTime");
                    if(oldTime<newTime){
                        map.put("maxTime",newTime);
                        map.put("maxId",id);
                    }
                }else{
                    map.put("maxTime",newTime);
                    map.put("maxId",id);
                }
            }
        }
    }

    /**
     * 最终转换成毫秒ms进行对比
     * @param time
     * @return
     */
    private double formatTime(String time){
        double newTime = 0;
        if(time.indexOf("ms")>-1){
            time = time.replace("ms","");
            newTime = Double.valueOf(time);
        }else if(time.indexOf("µs")>-1){
            time = time.replace("µs","");
            newTime = Double.valueOf(time)/1000;
        }else if(time.indexOf("s")>-1){
            time = time.replace("s","");
            newTime = Double.valueOf(time)*1000;
        }
        return newTime;
    }

    private void  buildParentList(Map map,Map<String,Node> treeMap,Node node){
        getMaxTimeChildNode(map,node);
        List<Node> children = node.getNodes();
        for(Node child:children){
            if(child.getParent()!=null){
                Node parent = child.getParent();
                String nodeId = child.getUuid();
                treeMap.put(nodeId,parent);
                if(child.getNodes()!=null){
                    buildParentList(map,treeMap,child);
                }
            }
        }
    }

    private void findParent(Map<String,Node> treeMap,Node node,String nodeId){
        if(!StringUtils.isEmpty(nodeId) && treeMap.get(nodeId)!=null){
            Node parent = treeMap.get(nodeId);
            getColorNodeTree(node,nodeId);
            findParent(treeMap,node,parent.getUuid());
        }
    }

    public void getColorNodeTree(Node node, String parentId) {
        node.setColor("red");
        recursionFn(node,parentId);
    }

    //递归
    private void recursionFn(Node tidbNode,String parentId){
        List<Node> children = tidbNode.getNodes();
        if(children!=null){
            for(Node child:children){
                if(child.getUuid().equals(parentId)){
                    child.setColor("red");
                }
                if(child.getName().indexOf("TableReader")>-1 && !StringUtils.isEmpty(child.getColor()) && child.getColor().equals("red")){
                    setTableReaderChildColor(child.getNodes());
                }
                recursionFn(child,parentId);
            }
        }
    }

    /**
     * 为标红TableReader子节点默认标红
     * @param children
     */
    private void setTableReaderChildColor(List<Node> children){
        if(children!=null && children.size()==1){
            children.get(0).setColor("red");
            setTableReaderChildColor(children.get(0).getNodes());
        }
    }

    @Override
    public String smartAdviseForIndex(String sql) {
        List<String> advise = new ArrayList<String>();

        if (!HandleOperatorSym(sql).equals("")) {
            advise.add(HandleOperatorSym(sql));
        }

        List<String> list = getRuleBySql(sql);

        for (String s : list) {
            advise.add(s);
        }

        String result = "索引专家建议\n";
        for (Integer i = 1; i < advise.size() + 1; i ++) {
            result += i.toString() + ". ";
            result += advise.get(i - 1);
            result += "\n";
        }

        return result;
    }

    private String HandleOperatorSym(String sql) {
        String[] result = sql.split(" ");

        for (int i = 0; i < result.length; i ++) {
            if (result[i].toLowerCase().equals("where")) {
                for (int j = i; j < result.length; j ++) {
                    if (result[j].contains("+") ||
                            result[j].contains("-") ||
                            result[i].contains("*") ||
                            result[i].contains("/")) {
                        return "where子句中包含运算符，不能命中索引。";
                    }
                }
            }
        }

        return "";
    }

}
