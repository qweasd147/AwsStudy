package com.aws.dynamodb.repository;

import com.aws.dynamodb.model.Article;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, String> {

    @EnableScan
    @EnableScanCount
    Page<Article> findAll(Pageable pageable);

    @EnableScan
    @EnableScanCount
    Page<Article> findAllByCodeOrderByCreateDateDesc(String code, Pageable pageable);
}
