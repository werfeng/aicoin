package com.example.aicoin.job;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * date 2020/5/30
 */
public class FJob {

    @Scheduled(cron = "0/1 * * * * ?")
    public void job(){
        System.out.println(11111);
    }
}
