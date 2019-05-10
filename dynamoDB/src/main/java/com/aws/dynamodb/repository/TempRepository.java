package com.aws.dynamodb.repository;

import com.aws.dynamodb.model.Temp;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableScan
public interface TempRepository extends PagingAndSortingRepository<Temp, String> {
}
