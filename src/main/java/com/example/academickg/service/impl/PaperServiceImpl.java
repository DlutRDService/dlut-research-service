package com.example.academickg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.academickg.component.RedisComponent;
import com.example.academickg.entity.constants.Regex;
import com.example.academickg.entity.dao.Paper;
import com.example.academickg.mapper.PaperMapper;
import com.example.academickg.service.IPaperService;
import com.example.academickg.utils.BM25;
import com.example.academickg.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.example.academickg.entity.dto.PaperDto;

import java.util.*;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements IPaperService {
    @Resource
    private PaperMapper paperMapper;

    /**
     * 传入检索式字段，已经过格式检查
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
            if (queryField.matches(Regex.MATCH_MULTI_CONDITION_WITHOUT_BRACKET)){
                map = StringUtils.singleSetRegexQueryMatch(queryField, Regex.TRUE_BOOLEAN_FORMAT);
                return map;
            }
        }
        return null;
    }
    public HashMap<Object, Object> multiSetQueryFieldProcess(String queryField){
        // 情况4,多个检索式有()，但()不表示并交关系
        // 情况5,多个检索式有(),并且()表示并交关系
        return null;
    }
    /**
     *
     */
    public List<PaperDto> singleSetQueriesProcess(HashMap<String, Integer> hashMap) {
        Set<String> queryFields = hashMap.keySet();
        for (String field : queryFields) {
            // 将“AF = ”等格式统一为“AF=”
            field = field.replaceAll(" *= *", "=");
            // 去掉检索式中的()
            field = field.replaceAll(Regex.MATCH_BRACKET, "");
            // 判断字段是否为主题检索，主题检索采用BM25算法实现
            if (field.substring(0, 3).equalsIgnoreCase("ts=")) {
                field = field.replace("ts=", "");
                BM25 bm25 = new BM25(1.2, 0);
                //bm25.similarity();
            }
            // 判断字段是否为期刊检索
            if (field.substring(0, 3).equalsIgnoreCase("so=")) {
                field = field.replace("so=", "").toUpperCase();
                paperMapper.selectByJournal(field);
            }
            // 判断字段是否为作者检索
            if (field.substring(0, 3).equalsIgnoreCase("au=")) {
                field = field.replace("au=", "");
                paperMapper.selectByAuthor(field);
            }
            // 判断字段是否为年份检索
            if (field.substring(0, 3).equalsIgnoreCase("py=")) {
                field = field.replace("py=", "");
                paperMapper.selectByYear(Integer.valueOf(field));
            }
            // 判断字段是否为WC类别检索
            if (field.substring(0, 3).equalsIgnoreCase("wc=")) {
                field = field.replace("wc=", "");
                paperMapper.selectByWC(field);
            }
            // 判断字段是否为ESI类别检索
            if (field.substring(0, 4).equalsIgnoreCase("esi=")) {
                field = field.replace("esi=", "");
                paperMapper.selectByESI(field);
            }
            // 判断字段是否为

            return null;
        }
        return null;
    }
    public List<PaperDto> multiSetQueriesProcess(){

    }



}
