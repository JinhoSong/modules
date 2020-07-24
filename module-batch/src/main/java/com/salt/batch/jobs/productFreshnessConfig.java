package com.salt.batch.jobs;


import com.salt.domain.product.dairy.Dairy;
import com.salt.domain.product.dairy.DairyRepository;
import com.salt.domain.product.dairy.DairyStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j  // log 사용을 위한 lombok Annotation
@AllArgsConstructor  // 생성자 DI를 위한 lombok Annotation
@Configuration
public class productFreshnessConfig {


    private final DairyRepository dairyRepository;

    @Bean
    public Job dairyJob(JobBuilderFactory jobBuilderFactory, Step dairyJobStep) {
        log.info("********** This is dairyJob");
        return jobBuilderFactory.get("dairyJob")
                //.preventRestart() //중복된걸 허용하지 않는다.
                .start(dairyJobStep)
                .build();
    }

    @Bean
    public Step dairyJobStep(StepBuilderFactory stepBuilderFactory) {
        log.info("********** This is dairyJobStep");
        return stepBuilderFactory.get("dairyJobStep")
                .<Dairy, Dairy>chunk(10)
                .reader(dairyStatusReader())
                .processor(this.dairyStatusProcess())
                .writer(this.dairyStatusWriter())
                .build();
    }



    @Bean
    @StepScope
    public ListItemReader<Dairy> dairyStatusReader() {
        log.info("********** This is dairyStatusReader");
        List<Dairy> freshDairy = dairyRepository.findByStatusEquals(DairyStatus.FRESH);
        log.info("          - freshDairyReader SIZE : " + freshDairy.size());
        List<Dairy> deadDairy = new ArrayList<>();
        for (Dairy dairy : freshDairy) {
            if (dairy.checking()) {
                deadDairy.add(dairy);
            }
        }
        log.info("          - deadDairy SIZE : " + deadDairy.size());
        return new ListItemReader<>(deadDairy);
    }

    public ItemProcessor<Dairy, Dairy> dairyStatusProcess() {
        return new ItemProcessor<Dairy, Dairy>() {
            @Override
            public Dairy process(Dairy dairy) throws Exception {
                log.info("********** This is dairyStatusProcess");
                return dairy.setStatusByDate();
            }
        };
    }

    public ItemWriter<Dairy> dairyStatusWriter() {
        log.info("********** This is dairyStatusWriter");
        return ((List<? extends Dairy> dairyList) ->
                dairyRepository.saveAll(dairyList));
    }

}
