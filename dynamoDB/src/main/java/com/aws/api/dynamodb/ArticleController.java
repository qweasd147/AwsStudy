package com.aws.api.dynamodb;

import com.aws.dynamodb.model.Article;
import com.aws.dynamodb.model.ArticleDto;
import com.aws.dynamodb.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/article")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ArticleController {

    private ArticleService articleService;

    @GetMapping
    public ResponseEntity searchAll(@PageableDefault(direction = Sort.Direction.DESC, size = 10) Pageable pageable){

        Page<Article> articlePage = articleService.listAll(pageable);
        Map<String, Object> result = new HashMap<>();

        result.put("contents", articlePage.getContent());
        result.put("count", articlePage.getTotalElements());
        result.put("page", articlePage.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{articleID}/{createTimeID}")
    public ResponseEntity searchOne(@PathVariable String articleID, @PathVariable Long createTimeID){

        Article article = articleService.findOne(articleID, createTimeID);
        ArticleDto.Resp respArticle = ArticleDto.Resp.of(article);

        return ResponseEntity.ok(respArticle);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ArticleDto.Resp register(@ModelAttribute ArticleDto.CreateReq createReq){

        Article article = articleService.register(createReq);

        return ArticleDto.Resp.of(article);
    }

    @PutMapping("/{articleID}/{createTimeID}")
    @ResponseStatus(value = HttpStatus.OK)
    public ArticleDto.Resp modify(@PathVariable String articleID
            , @PathVariable Long createTimeID
            , @ModelAttribute ArticleDto.UpdateReq updateReq){

        Article article = articleService.update(articleID, createTimeID, updateReq);

        return ArticleDto.Resp.of(article);
    }

    @DeleteMapping("/{articleID}/{createTimeID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable String articleID
            , @PathVariable Long createTimeID){

        articleService.deleteOne(articleID, createTimeID);
    }
}
