package com.aws.dynamodb.service;

import com.amazonaws.util.ImmutableMapParameter;
import com.aws.dynamodb.model.Temp;
import com.aws.dynamodb.repository.TempRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional
public class TempService {

    private TempRepository tempRepository;

    public void createTemp(){

        Temp temp = Temp.builder()
                .data1("temp data1")
                .data2("temp data2")
                .data3(ImmutableMapParameter.of("key1","data2"))
                .build();

        tempRepository.save(temp);

        System.out.println("create temp : " + temp);
    }

    public void findAll(){
        Iterable<Temp> tempAll = tempRepository.findAll();

        tempAll.forEach(System.out::println);
    }

    public void updateTemp(String idx){

        Temp temp = tempRepository.findById(idx).get();
        temp.changeData1("change data1");

        temp.changeData2("change data2");

        tempRepository.save(temp);
    }
}
