package com.aws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
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
@EnableDynamoDBRepositories(basePackages = "com.aws.dynamodb.repository")
@Slf4j
public class DynamoDBConfig {

    @Value("${aws.dynamo.endpoint}")
    private String endpoint;

    @Value("${aws.credential.profile}")
    private String profile;

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

        // Alternatively, you can scan your model package for the DynamoDBTable annotation
        List<Class> modelClasses = new ArrayList<>();
        modelClasses.add(Temp.class);

        for (Class cls : modelClasses) {
            log.info("Creating DynamoDB table for " + cls.getSimpleName());
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(cls);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

            boolean created = TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);

            if (created) {
                log.info("Created DynamoDB table for " + cls.getSimpleName());
            } else {
                log.info("Table already exists for " + cls.getSimpleName());

            }
        }

        ListTablesResult tablesResult = amazonDynamoDB.listTables();
        log.info("Current DynamoDB tables are: ");
        for (String name : tablesResult.getTableNames()) {
            log.info("\t" + name);
        }

    }
}