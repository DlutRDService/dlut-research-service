package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.constants.TreeNode;
import com.dlut.ResearchService.entity.dao.Paper;
import com.dlut.ResearchService.utils.QueryUtils;
import com.dlut.ResearchService.utils.StringUtils;
import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.mapper.PaperMapper;
import com.dlut.ResearchService.service.IPaperService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dlut.ResearchService.entity.constants.Query.BOOLEAN_OPERATORS_ERROR;
import static com.dlut.ResearchService.entity.constants.Query.EXPRESSION_ERROR;

@Slf4j
@Service
public class PaperServiceImpl implements IPaperService {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 根据单一检索式进行查询
     * @param query 子检索式
     * @return 检索id列表
     */
    public List<Integer> selectByQuery(String query){
        return paperMapper.selectIds(query);
    }
    
    public Result advancedQuery(String queryField){
        // TODO 需要验证
        // 判断是否含有字符,以及含有=。
        if (!StringUtils.containNumOrChar(queryField) || !queryField.contains("=")) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        // 检查索引式是否符合要求,例如“AF=”或者“ AF=”，索引字段前不允许含有字母与数字
        if (!queryField.matches(Regex.FORMAT_QUERY)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        //判断是否正确使用布尔操作符，检查不正确使用布尔操作符的情况
        if (queryField.matches(Regex.FALSE_BOOLEAN_FORMAT)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, BOOLEAN_OPERATORS_ERROR);
        }
        try{
            // 处理大小写
            queryField = queryField.toLowerCase();
            // 处理中文字符
            queryField = StringUtils.subChineseChars(queryField);
            // 处理and，not等转为大写
            queryField = QueryUtils.matchAndUpper(queryField, Regex.MATCH_OPERATOR);
        } catch (Exception e){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        // TODO 需要解决
        // 情况1，只有一个检索式的情况（匹配大写的AND NOT可以实现）
        if (queryField.matches(Regex.MATCH_ONLY_ONE_CONDITION_WITHOUT_BOOLEAN)){
            // 含有TS
            String ts = StringUtils.getMatchStrings(queryField, Regex.TS_FORMAT).toString();

            // 不含TS
            paperMapper.select("");
        }
        // 情况2，多个检索式但是没有优先级 （分着写）
        if (! queryField.matches(Regex.MATCH_BRACKET)) {
            // 含有TS
            List<String>  ts = StringUtils.getMatchStrings(queryField, Regex.TS_FORMAT);
            // 不含TS
        }
        // 情况3，多个检索式，有优先级 （解析树处理）
        if (queryField.matches("")) {
            List<String> prefixString = QueryUtils.stringToPrefixString(queryField);
            TreeNode node = QueryUtils.prefixStringToTreeNode(prefixString);
            // 含有TS
            List<String>  ts = StringUtils.getMatchStrings(queryField, Regex.TS_FORMAT);
            // 不含TS
        }


        // 处理TS


        // 将TS用向量数据库检索,列表类型，传给Milvus，检索结果为list，将其转换成id in (...) 格式方便Mysql进行查询
        // 处理检索结果，返回mysql查询
        // 需要分表查询

        return null;


    }

    /**
     * 按照id列表返回论文信息
     */
    public List<Paper> selectPapersByIdList(List<Integer> idList){
        return paperMapper.selectPaperByIdList(idList);
    }

//    /**
//     * 传入已经过格式检查的检索式字段
//     * @param queryField 索引字符串
//     * @return 处理结果
//     */
//    public Set<Integer> multiSetQueryFieldProcess(String queryField){
//        Map<String, Set<Integer>> setsMap = new HashMap<>();
//        List<String> expressionList = StringQueryToListAlgorithm.extractFieldQualifiers(queryField);
//        System.out.println(queryField);
//        System.out.println(expressionList);
//        assert expressionList != null;
//        for (String expression : expressionList) {
//            System.out.println(expression);
//            List<Integer> ids = paperMapper.selectIds(expression);
//            setsMap.put(expression, new HashSet<>(ids));
//        }
//        System.out.println(result);
//        return result;
//    }

    public List<Paper> singleSetQueriesProcess(HashMap<String, Integer> hashMap) {
        List<Paper> paperDto;
        Set<String> queryFields = hashMap.keySet();
        for (String field : queryFields) {
            // 将“AF = ”等格式统一为“AF=”
            field = field.replaceAll(" *= *", "=");
            // 去掉检索式中的()
            field = field.replaceAll(Regex.MATCH_BRACKET, "");
            if (queryFields.size()==1){
                paperDto = selectOneQueryField(field);
                return paperDto;
            }
        }
        return null;
    }

    @Override
    public HashMap<String, List<String>> queryFieldProcess(String queryField) {
        return null;
    }

    @Override
    public List<Paper> queryProcess(HashMap<String, List<String>> hashMap) {
        return null;
    }

    public List<Paper> selectOneQueryField(String queryField){
        // 判断字段是否为主题检索，主题检索推荐算法实现。
        if (queryField.substring(0, 3).equalsIgnoreCase("ts=")) {
            queryField = queryField.replace("ts=", "");
        }
        // 判断字段是否为期刊检索
        if (queryField.substring(0, 3).equalsIgnoreCase("so=")) {
            queryField = queryField.replace("so=", "").toUpperCase();
            return paperMapper.selectByJournal(queryField);
        }
        // 判断字段是否为作者检索
        if (queryField.substring(0, 3).equalsIgnoreCase("au=")) {
            queryField = queryField.replace("au=", "");
            return paperMapper.selectByAuthor(queryField);
        }
        // 判断字段是否为年份检索
        if (queryField.substring(0, 3).equalsIgnoreCase("py=")) {
            queryField = queryField.replace("py=", "");
            return paperMapper.selectByYear(Integer.valueOf(queryField));
        }
        // 判断字段是否为WC类别检索
        if (queryField.substring(0, 3).equalsIgnoreCase("wc=")) {
            queryField = queryField.replace("wc=", "");
            return paperMapper.selectByWC(queryField);
        }
        // 判断字段是否为ESI类别检索
        if (queryField.substring(0, 4).equalsIgnoreCase("esi=")) {
            queryField = queryField.replace("esi=", "");
            return paperMapper.selectByESI(queryField);
        }
        return null;
    }
}
