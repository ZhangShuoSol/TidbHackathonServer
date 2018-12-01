package com.tidbhack.backend.service;

import com.google.gson.Gson;
import com.tidbhack.backend.datasource.Explain;
import com.tidbhack.backend.datasource.ExplainRowMapper;
import com.tidbhack.backend.domain.*;
import com.tidbhack.backend.dto.Response;
import com.tidbhack.backend.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        Node root = explainParser.getRoot();
        //设置用时最长的数据链
        Map map = new HashMap<>();
        Map<String,Node> treeMap = new HashMap();
        buildParentList(map,treeMap,root);
        String maxId = (String) map.get("maxId");
        findParent(treeMap,root,maxId);

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
        Map<String,String> fieldMap = new HashMap<String,String>();
        for (int i = 1; i <= columnCount; i++) {
            fieldMap.put("ColumnName", metaData.getColumnName(i));
            fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
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

}
