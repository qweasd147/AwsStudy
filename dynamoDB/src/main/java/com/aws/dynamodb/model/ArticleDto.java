package com.aws.dynamodb.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.util.*;

public class ArticleDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateReq {

        private String subject;
        private String contents;
        private List<String> data1 = new ArrayList<>();
        private Map<String, String> data2 = new HashMap<>();

        @Builder(toBuilder = true)
        public CreateReq(String subject, String contents, List<String> data1, Map<String, String> data2) {
            this.subject = subject;
            this.contents = contents;
            this.data1 = data1;
            this.data2 = data2;
        }

        public Article toEntity(){
            return Article.builder()
                    .subject(this.subject)
                    .contents(this.contents)
                    .data1(new HashSet<>(this.data1))
                    .data2(this.data2)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @Setter
    public static class UpdateReq {

        private String subject;
        private String contents;
        private List<String> data1 = new ArrayList<>();
        private Map<String, String> data2 = new HashMap<>();

        @Builder
        public UpdateReq(String subject, String contents) {
            this.subject = subject;
            this.contents = contents;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Resp {

        private String idx;
        private String code;
        private String createDate;
        private String subject;
        private String contents;
        private int count;
        private List<String> data1 = new ArrayList<>();
        private Map<String, String> data2 = new HashMap<>();


        @Builder
        public Resp(String idx, String code, String createDate
                , String subject, String contents, int count
                , List<String> data1, Map<String, String> data2) {

            this.idx = idx;
            this.code = code;
            this.createDate = createDate;
            this.subject = subject;
            this.contents = contents;
            this.count = count;
            this.data1 = data1;
            this.data2 = data2;
        }

        public static Resp of(Article article) {

            Long createDateToLong = article.getCreateDate().atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            return Resp.builder()
                    .idx(article.getIdx())
                    .code(article.getCode())
                    .createDate(String.valueOf(createDateToLong))
                    .subject(article.getSubject())
                    .contents(article.getContents())
                    .count(article.getCount())
                    .data1(new ArrayList<>(article.getData1()))
                    .data2(article.getData2())
                    .build();
        }
    }
}
