package com.example.academickg.controller;

import com.example.academickg.annotation.log;
import com.example.academickg.entity.constants.Result;
import com.example.academickg.entity.dto.AuthorDto;
import com.example.academickg.service.impl.AuthorServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsl
 * @since 2023-06-03
 */
@RestController
@RequestMapping("author")
public class AuthorController {
    /*
    * 构建作者画像：
    点开作者信息后。首先是作者的基本信息，姓名、机构、国家、主要研究内容、给出近些年发表的文章以及被引高的的文章，
    给出一个十年内的发文数量变化图，点开作者的连接，跳转到一个作者界面，以及作者的合作关系图。
    * 每年发文如何解决：👍
    每年发文可以用一个Json结构存进去，直接处理Json即可，然后生成发文变化图，
    * 高被引文章如何解决：👍
    高被引也可以给个Json实现吧，每次给三个，多了再查，不过也不如直接查论文，论文属性带一个被引，然后直接从作者这里引过去
    通过paper-author。TC字段表示被引，统计下TC
    * 关键字如何解决：👍
    Mysql中没有列表，可以考虑json，不然使用中间表多对多，但是那也太多了，上亿数据量
    * 作者表设计：👍
    作者id，作者、机构、国家、发文量、每年发文变化。。。。补充：h指数
    * 关键词表设计：👍
    关键词id，关键词、频次、所属领域，所属领域比较难处理
    * 关键词表与作者表关联：👍
    使用中间表设计，关联id
    * DAO层如何设计来处理Json：👍
    直接String，然后用阿里的fastjson强转
     */
    @Resource
    private AuthorServiceImpl authorService;

    @PostMapping("authorInfo")
    public Result authorInfo1(@RequestParam AuthorDto author){
        return authorService.getAuthorInfo(author);
    }

    @PostMapping("text")
    @log
    public Result authorInfo(@RequestParam Integer id){
        return authorService.getAuthorInfoById(id);
    }
}
