package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.entity.chat.Article;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/9 14:32
 */
@RestController
@RequestMapping("/book")
public class TestBookController {
    @Resource
    private MongoTemplate mongoTemplate;

    //单个添加
    @GetMapping("/add")
    public void add() {
        for (int i = 0; i < 10; i++) {
            Article article = new Article();
            article.setTitle("MongoTemplate的基本使用");
            article.setAuthor("sky");
            article.setUrl("www.baidu.com/" + i);
            article.setTags(Arrays.asList("java", "mongodb", "spring"));
            article.setVisitCount(0L);
            article.setAddTime(new Date());
            mongoTemplate.save(article);
        }
    }

    // 批量添加
    @GetMapping("/adds")
    public void adds() {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Article article = new Article();
            article.setTitle("String Boot");
            article.setAuthor("sky");
            article.setUrl("www.baidu.com/" + i);
            article.setTags(Arrays.asList("java", "mongodb", "spring"));
            article.setVisitCount(0L);
            article.setAddTime(new Date());
            articles.add(article);
        }
        mongoTemplate.insert(articles, Article.class);
    }

    @GetMapping("/delete")
    public void delete() {
        Query query = Query.query(Criteria.where("author").is("sky"));
        mongoTemplate.remove(query, "article");
    }
}
