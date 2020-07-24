package com.salt.batch.jobs;

import com.salt.domain.user.User;
import com.salt.domain.user.UserRepository;
import com.salt.domain.user.UserStatus;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j  // log 사용을 위한 lombok Annotation
@AllArgsConstructor  // 생성자 DI를 위한 lombok Annotation
@Configuration
public class unPaidUserConfig {

    private final UserRepository userRepository;

    @Bean
    public Job unPaidUserJob(JobBuilderFactory jobBuilderFactory, Step unPaidUserJobStep) {
        log.info("********** This is unPaidUserJob");
        return jobBuilderFactory.get("unPaidUserJob")
                .preventRestart() //중복된걸 허용하지 않는다.
                .start(unPaidUserJobStep)
                .build();
    }

    @Bean
    public Step unPaidUserJobStep(StepBuilderFactory stepBuilderFactory) {
        log.info("********** This is unPaidUserJobStep");
        return stepBuilderFactory.get("unPaidUserJobStep")
                .<User, User>chunk(10)
                .reader(unPaidUserReader())
                .processor(this.unPaidUserProcessor())
                .writer(this.unPaidUserWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<User> unPaidUserReader() {
        log.info("********** This is unPaidUserReader");
        List<User> activeUsers = userRepository.findByStatusEquals(UserStatus.ACTIVE);
        log.info("          - activeUser SIZE : " + activeUsers.size());
        List<User> unPaidUsers = new ArrayList<>();
        for (User user : activeUsers) {
            if (user.isUnpaid()) {
                unPaidUsers.add(user);
            }
        }
        log.info("          - unPaidUser SIZE : " + unPaidUsers.size());
        return new ListItemReader<>(unPaidUsers);
    }

    public ItemProcessor<User, User> unPaidUserProcessor() {
//        return User::setStatusByunPaid;
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User user) throws Exception {
                log.info("********** This is unPaidUserProcessor");
                return user.setStatusByUnPaid();
            }
        };
    }

    public ItemWriter<User> unPaidUserWriter() {
        log.info("********** This is unPaidUserWriter");
        return ((List<? extends User> userList) ->
                userRepository.saveAll(userList));
    }
}