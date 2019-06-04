package com.aws.dynamodb.service;

import com.aws.dynamodb.model.Article;
import com.aws.dynamodb.model.ArticleDto;
import com.aws.dynamodb.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private ArticleRepository articleRepository;

    @Override
    public Article register(ArticleDto.CreateReq createReq) {

        Article article = createReq.toEntity();
        article.initBeforeCreate();

        return articleRepository.save(article);
    }

    @Override
    public Page<Article> listAll(Pageable pageable) {
        return articleRepository.findAllByCodeOrderByCreateDateDesc("temp", pageable);
    }

    @Override
    public Article findOne(String articleID, Long createTimeID) {
        return articleRepository.findById(articleID)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없음 : " + articleID));
    }

    @Override
    public void deleteOne(String articleID, Long createTimeID) {
        articleRepository.deleteById(articleID);
    }

    @Override
    public Article update(String articleID, Long createTimeID, ArticleDto.UpdateReq updateReq) {

        Article article = articleRepository.findById(articleID)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없음 : " + articleID));

        article.update(updateReq.getSubject(), updateReq.getContents());

        return articleRepository.save(article);
    }
}
