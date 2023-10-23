package com.example.academickg.service.impl;

import com.example.academickg.entity.constants.Regex;
import com.example.academickg.mapper.PaperMapper;
import com.example.academickg.service.IPaperService;
import com.example.academickg.utils.BM25;
import com.example.academickg.utils.StringQueryToListAlgorithm;
import com.example.academickg.utils.SetOperationsAlgorithm;
import com.example.academickg.utils.StringQueryToListAlgorithm;
import com.example.academickg.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.example.academickg.entity.dto.PaperDto;

import java.util.*;


@Service
public class PaperServiceImpl implements IPaperService {
    @Resource
    private PaperMapper paperMapper;

    public PaperDto selectPapersById(Integer id){
        return paperMapper.selectPaperById(id);
    }
    /**
     * 按照id列表返回论文信息
     */
    public List<PaperDto> selectPapersByIdList(List<Integer> idList){
        return paperMapper.selectPaperByIdList(idList);
    }

    /**
     * 查询论文标题
     */
    public HashMap<Integer, String> selectTitleAndId(){
        return paperMapper.selectTitleAndId();
    }
    /**
     * 传入已经过格式检查的检索式字段
     * @param queryField 索引字符串
     * @return 处理结果
     */
    public HashMap<String, Integer> singleSetQueryFieldProcess(String queryField) {
        HashMap<String, Integer> map;
        // 情况1，只有一个检索式的情况下且没有布尔表达式
        if (queryField.matches(Regex.MATCH_ONLY_ONE_CONDITION_WITHOUT_BOOLEAN)){
            map = StringUtils.singleSetRegexQueryMatch(queryField, Regex.JUDGE_ONLY_ONE_CONDITION_WITHOUT_BOOLEAN);
            return map;
        }
        // 情况2，只有一个检索式的情况下有布尔表达式
        if (queryField.matches(Regex.MATCH_ONLY_ONE_CONDITION_WITH_BOOLEAN)) {
            map = StringUtils.singleSetRegexQueryMatch(queryField, Regex.JUDGE_ONLY_ONE_CONDITION_WITH_BOOLEAN);
            return map;
        }
        // 情况3，多个检索式但是，没有()的前提下
        if (! queryField.matches(Regex.MATCH_BRACKET)) {
            if (queryField.matches(Regex.MATCH_MULTI_CONDITION)){
                map = StringUtils.singleSetRegexQueryMatch(queryField, Regex.TRUE_BOOLEAN_FORMAT);
                return map;
            }
        }
        return null;
    }
    public Set<Integer> multiSetQueryFieldProcess(String queryField){
        Map<String, Set<Integer>> setsMap = new HashMap<>();
        List<String> expressionList = StringQueryToListAlgorithm.extractFieldQualifiers(queryField);
        System.out.println(queryField);
        System.out.println(expressionList);
        assert expressionList != null;
        for (String expression : expressionList) {
            System.out.println(expression);
            List<Integer> ids = paperMapper.selectIds(expression);
            setsMap.put(expression, new HashSet<>(ids));
        }
        Set<Integer> result = SetOperationsAlgorithm.mixedOperation(setsMap, queryField);
        System.out.println(result);
        return result;
    }

    /**
     *
     */
    public List<PaperDto> singleSetQueriesProcess(HashMap<String, Integer> hashMap) {
        List<PaperDto> paperDto;
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
    public List<PaperDto> queryProcess(HashMap<String, List<String>> hashMap) {
        return null;
    }

    public List<PaperDto> selectOneQueryField(String queryField){
        // 判断字段是否为主题检索，主题检索推荐算法实现。
        if (queryField.substring(0, 3).equalsIgnoreCase("ts=")) {
            queryField = queryField.replace("ts=", "");
            BM25 bm25 = new BM25(1.2, 0);
            //bm25.similarity();
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
