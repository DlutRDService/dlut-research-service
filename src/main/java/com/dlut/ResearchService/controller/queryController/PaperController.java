package com.dlut.ResearchService.controller.queryController;

import com.dlut.ResearchService.service.impl.PaperServiceImpl;
import com.dlut.ResearchService.entity.constants.Result;
import jakarta.annotation.Resource;
import com.dlut.ResearchService.annotation.log;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsl
 * @since 2023/06/23
 */
@RestController
@RequestMapping("/paper")
public class PaperController {
    @Resource
    private PaperServiceImpl paperService;

    /**
     * 高级检索功能
     * @param queryField 检索字段
     */
    @log
    @GetMapping("/advanced-search")
    public Result advancedSearch(HttpSession session, @RequestParam String queryField) {
        return paperService.advancedResearch(session, queryField);
    }

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 页面内容大小
     * @return 当前页码内信息
     */
    @log
    @GetMapping("/advanced-search/page")
    public Result advancedSearchLimit(HttpSession session,
                                      @RequestParam() Integer pageNum,
                                      @RequestParam() Integer pageSize){
        return paperService.advancedQueryLimit(session, pageNum, pageSize);
    }

    /**
     * 查看文章具体信息
     * @param paperId 论文id
     * @return 返回文章信息，包括推荐信息，关系图示信息。
     */
    @GetMapping("full-record/{paperId}")
    public Result paperInformation(@PathVariable Integer paperId) {
        return paperService.paperInformation(paperId);
    }

}
