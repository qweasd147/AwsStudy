package com.aws.dynamodb.service;

import com.aws.dynamodb.model.Article;
import com.aws.dynamodb.model.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Article register(ArticleDto.CreateReq createReq);

    Page<Article> listAll(Pageable pageable);

    Article findOne(String articleID, Long createTimeID);

    void deleteOne(String articleID, Long createTimeID);

    Article update(String articleID, Long createTimeID, ArticleDto.UpdateReq updateReq);
}
