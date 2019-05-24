package com.aws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.aws.dynamodb.model.Temp;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableDynamoDBRepositories(basePackages = "com.aws.dynamodb.repository")
@EnableDynamoDBRepositories(basePackages = "com.aws")
@Slf4j
public class DynamoDBConfig {

    private final String endpoint;
    private final String profile;

    public DynamoDBConfig(@Value("${aws.dynamo.endpoint}") String endpoint
            , @Value("${aws.credential.profile}")String profile) {

        this.endpoint = endpoint;
        this.profile = profile;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
        if (!StringUtils.isEmpty(endpoint)) {
            amazonDynamoDB.setEndpoint(endpoint);
        }
        /*
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(endpoint, "test"))
                .build();
        */

        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new ProfileCredentialsProvider(profile).getCredentials();
    }

    @PostConstruct
    public void initDanamoDB(){

        AmazonDynamoDB amazonDynamoDB = amazonDynamoDB();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        List<Class> modelClasses = new ArrayList<>();

        modelClasses.add(Temp.class);

        //deleteTables(amazonDynamoDB, dynamoDBMapper, modelClasses);
        //createTables(amazonDynamoDB, dynamoDBMapper, modelClasses);
        //nowTables(amazonDynamoDB);

    }

    private void deleteTables(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper, List<Class> modelClasses){
        for (Class cls : modelClasses) {
            log.info("Delete DynamoDB table for " + cls.getSimpleName());
            DeleteTableRequest tableRequest = dynamoDBMapper.generateDeleteTableRequest(cls);
            boolean deleted = TableUtils.deleteTableIfExists(amazonDynamoDB, tableRequest);

            if (deleted) {
                log.info("Deleted DynamoDB table for " + cls.getSimpleName());
            } else {
                log.info("Table not exists for " + cls.getSimpleName());

            }
        }
    }

    private void createTables(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper, List<Class> modelClasses){
        // Alternatively, you can scan your model package for the DynamoDBTable annotation
        for (Class cls : modelClasses) {
            log.info("Creating DynamoDB table for " + cls.getSimpleName());
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(cls);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

            List<GlobalSecondaryIndex> globalIndexs = tableRequest.getGlobalSecondaryIndexes();

            if(globalIndexs != null){
                globalIndexs.forEach(globalSecondaryIndex ->{
                    //글로벌 인덱스에 동일한 throughput을 넣는다.
                    globalSecondaryIndex
                            .setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

                    //인덱스 키에 entity의 모든 attribute를 담는다.(안하면 정렬 검색 시, 키값만 검색됨)
                    globalSecondaryIndex
                            .setProjection(new Projection().withProjectionType(ProjectionType.ALL));
                });
            }

            boolean created = TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);

            if (created) {
                log.info("Created DynamoDB table for " + cls.getSimpleName());
            } else {
                log.info("Table already exists for " + cls.getSimpleName());

            }
        }
    }

    private void nowTables(AmazonDynamoDB amazonDynamoDB) {
        ListTablesResult tablesResult = amazonDynamoDB.listTables();
        log.info("Current DynamoDB tables are: ");
        for (String name : tablesResult.getTableNames()) {
            log.info("\t" + name);
        }
    }
}