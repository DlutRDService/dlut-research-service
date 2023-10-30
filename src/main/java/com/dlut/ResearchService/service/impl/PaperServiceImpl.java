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

import static com.dlut.ResearchService.entity.constants.Query.EXPRESSION_ERROR;

@Slf4j
@Service
public class PaperServiceImpl implements IPaperService {
    @Resource
    private PaperMapper paperMapper;
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
    public Result advancedQuery(String queryField) {
        // 判断是否含有字母字符,以及含有=。
        if (!StringUtils.containLetter(queryField) || !queryField.contains("=")) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        try {
            queryField = queryField.toLowerCase();
            queryField = StringUtils.subChineseChars(queryField);
            queryField = QueryUtils.matchAndUpper(queryField, Regex.MATCH_OPERATOR);
        } catch (Exception e) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }
        // TODO 需要重新校验逻辑
        // 去掉"("前后的多余的空格，改为" ( "，去掉"="前后多余的空格，改为"="
        if (queryField.matches("\\(") || queryField.matches("\\)")){
            // queryField = StringUtils.
        }
        // 检查索引式是否符合要求,例如"au="，索引字段前不允许含有字母与数字
        if (!queryField.matches(Regex.FORMAT_START_QUERY)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, EXPRESSION_ERROR);
        }

        TreeNode node;
        if (queryField.matches("")) {
            // TODO 这两行需要修改源代码
            // 将字符串转化为中缀字符串
            List<String> infixString = QueryUtils.queryToInfixString(queryField);
            // 将中缀字符串转化为解析树
            node = QueryUtils.infixStringToTreeNode(infixString);
            // TODO 考虑分表
            // 将解析树节点的子查询，转化为查询列表
            node = changeNodeValueToSubResultSet(node);
            // 进行集合运算
            Set<Integer> searchResult = QueryUtils.getEvaluateNode(node);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "", searchResult);
        }

        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", null);
    }
    public TreeNode changeNodeValueToSubResultSet(TreeNode node) {
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
                    //执行向量查询
                    node.value = (node.value.toString());
                } else {
                    //执行sql查询
                    node.value = selectByQuery((String) node.value);
                }
        }
        return node;
    }

    /**
     * 按照id列表返回论文信息
     */
    @Override
    public List<Paper> selectPapersByIdList(List<Integer> idList){
        return paperMapper.selectPaperByIdList(idList);
    }

    @Override
    public Result paperInformation(Integer paperId) {
        Paper paperInfo = paperMapper.selectPaperById(paperId);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", paperInfo);
    }

}
