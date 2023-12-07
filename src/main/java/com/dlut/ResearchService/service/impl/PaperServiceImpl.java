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
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dlut.ResearchService.entity.constants.Query.EXPRESSION_ERROR;
import static com.dlut.ResearchService.entity.constants.redis.RedisTimePolicy.SEARCH_EXPIRATION_TIME;

@Slf4j
@Service
public class PaperServiceImpl implements IPaperService {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private TextAnalysisServiceImpl textAnalysisService;

    /**
     * 根据单一检索式进行查询
     * @param query 子检索式
     * @return 检索id列表
     */
    @Override
    public Set<Integer> selectByQuery(@NotNull String query){
        String subQuery = query.substring(2);
        return switch (query.substring(0, 2)) {
            case "au" -> paperMapper.selectIdsByAuthor(subQuery);
            case "de" -> paperMapper.selectIdsByKeyword(subQuery);
            case "py" -> paperMapper.selectIdsByPublishYear(Integer.parseInt(subQuery));
            case "tl" -> paperMapper.selectIdByTitle(subQuery);
            case "so" -> paperMapper.selectIdsByJournal(subQuery);
            case "wc" -> paperMapper.selectIdsByWOSCategory(subQuery);
            default -> null;
        };
    }

    /**
     * 根据id列表查找文献
     * @param ids 文献id列表
     */
    @Override
    public List<Paper> selectPapersByIdList(List<Integer> ids){
        return paperMapper.selectPaperByIdList(ids);
    }

    /**
     * 高级检索
     * @param queryField 检索字段
     * @return 查询结果列表Set
     */
    @Override
    public Result advancedResearch(HttpSession session, String queryField) {
        if (!StringUtils.containLetter(queryField) || !queryField.contains("=")) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        try {
            queryField = queryField.toLowerCase();
            queryField = StringUtils.replaceChineseChars(queryField);
            queryField = QueryUtils.matchAndUpper(queryField, Regex.MATCH_OPERATOR);
            queryField = QueryUtils.trimWhitespace(queryField);
        } catch (Exception e) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        if (!queryField.matches(Regex.FORMAT_START_QUERY)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        List<Integer> ids;
        String idString;
        String resultListKey = "resultList:" + session.getId();
        TreeNode node;
        // 单一检索式直接检索
        if (StringUtils.getCharCount(queryField, '=') == 1){
            if(queryField.startsWith("ts")) {
                ids = new ArrayList<>(textAnalysisService.searchByStringVector(queryField));
            }else {
                ids = new ArrayList<>(selectByQuery(queryField));
            }
            idString = ids.toString();
            redisService.set(resultListKey, idString, SEARCH_EXPIRATION_TIME);
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_200,
                    "",
                    paperMapper.selectPaperByIdListPage(ids, 0, 20)
            );
        }
        if (queryField.matches("AND|OR|NOT")) {
            // 将字符串转化为中缀字符串
            List<String> infixString = QueryUtils.queryToInfixString(queryField);
            advanceResearchByQueryList(session, infixString);
        }
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", null);
    }

    /**
     * 查询中缀字符表达式结果
     * @param session 会话
     * @param queries 查询列表
     */
    @Override
    public Result advanceResearchByQueryList(@NotNull HttpSession session, List<String> queries){
        String resultListKey = "resultList:" + session.getId();
        // 将中缀字符串转化为解析树
        TreeNode node = QueryUtils.infixStringToTreeNode(queries);
        // 将解析树节点的子查询，转化为查询列表
        node = changeNodeValueToSubResultSet(node);
        // 进行集合运算
        Set<Integer> searchResult = QueryUtils.getEvaluateNode(node);
        List<Integer> ids = new ArrayList<>(searchResult);
        String idString = ids.toString();
        redisService.set(resultListKey, idString, SEARCH_EXPIRATION_TIME);
        return resultBuilder.build(
                StatusCode.STATUS_CODE_200, "", paperMapper.selectPaperByIdListPage(ids, 0, 20)
        );
    }

    public TreeNode changeNodeValueToSubResultSet(@NotNull TreeNode node) {
        switch (node.value.toString()) {
            case "AND":
                changeNodeValueToSubResultSet(node.left);
                changeNodeValueToSubResultSet(node.right);
            case "OR":
                changeNodeValueToSubResultSet(node.left);
                changeNodeValueToSubResultSet(node.right);
            case "NOT":
                changeNodeValueToSubResultSet(node.left);
                changeNodeValueToSubResultSet(node.right);
            default:
                if (node.value.toString().startsWith("ts")){
                    node.value = textAnalysisService.searchByStringVector(node.value.toString());
                } else {
                    node.value = selectByQuery((String) node.value);
                }
        }
        return node;
    }

    @Override
    public Result paperInformation(Integer paperId) {
        HashMap<String, Object> map = new HashMap<>();
        Paper paperInfo = paperMapper.selectPaperById(paperId);
        List<Integer> ids = textAnalysisService.searchByIdVector(paperId);
        List<Paper> recommendationPaper = paperMapper.selectPaperByIdList(ids);
        map.put("Paper", paperInfo);
        map.put("recommendation", recommendationPaper);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "查询完毕", map);
    }

    @Override
    public Result advancedQueryLimit(@NotNull HttpSession session, Integer pageNum, Integer pageSize) {
        String resultListKey = "resultList:" + session.getId();
        String[] strings = redisService.get(resultListKey).split(",");
        List<String> stringList = Arrays.asList(strings);
        List<Integer> idList = StringUtils.stringListToIntegerList(stringList);
        return resultBuilder.build(
                StatusCode.STATUS_CODE_200,
                "查询完毕",
                paperMapper.selectPaperByIdListPage(idList, pageNum, pageSize)
        );
    }
}
