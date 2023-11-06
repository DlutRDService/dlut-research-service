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

@Slf4j
@Service
public class PaperServiceImpl implements IPaperService {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private WebClientServiceImpl webClientService;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 根据单一检索式进行查询
     * @param query 子检索式
     * @return 检索id列表
     */
    @Override
    public Set<Integer> selectByQuery(String query){
        return paperMapper.selectIds(query);
    }

    /**
     * 高级检索
     * @param queryField 检索字段
     * @return 查询结果列表Set
     */
    @Override
    public Result advancedQuery(HttpSession session, String queryField) {
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
        // 检查索引式是否符合要求,例如"au="，索引字段前不允许含有字母与数字
        if (!queryField.matches(Regex.FORMAT_START_QUERY)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        TreeNode node;
        if (queryField.matches("")) {
            // 将字符串转化为中缀字符串
            List<String> infixString = QueryUtils.queryToInfixString(queryField);
            // 将中缀字符串转化为解析树
            node = QueryUtils.infixStringToTreeNode(infixString);
            // 将解析树节点的子查询，转化为查询列表
            node = changeNodeValueToSubResultSet(node);
            // 进行集合运算
            Set<Integer> searchResult = QueryUtils.getEvaluateNode(node);
            List<Integer> ids = new ArrayList<>(searchResult);
            String idString = ids.toString();
            String resultListKey = "resultList:" + session.getId();
            redisService.set(resultListKey, idString);
            // TODO Mapper层的逻辑还没写
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_200, "", paperMapper.selectPaperByIdList(ids, 0, 20)
            );
        }
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", null);
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
                if (node.value.toString().matches(Regex.TS_FORMAT)){
                    //TODO 执行向量查询
                    node.value = (node.value.toString());
                } else {
                    node.value = paperMapper.selectIds((String) node.value);
                }
        }
        return node;
    }

    @Override
    public Result paperInformation(Integer paperId) {
        Paper paperInfo = paperMapper.selectPaperById(paperId);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", paperInfo);
    }

    @Override
    public Result advancedQueryLimit(@NotNull HttpSession session, Integer pageNum, Integer pageSize) {
        String resultListKey = "resultList:" + session.getId();
        String[] strings = redisService.get(resultListKey).split(",");
        List<String> stringList = Arrays.asList(strings);
        List<Integer> idList = StringUtils.stringListToIntegerList(stringList);
        return resultBuilder.build(
                StatusCode.STATUS_CODE_200,
                "",
                paperMapper.selectPaperByIdList(idList, pageNum, pageSize)
        );
    }

}
