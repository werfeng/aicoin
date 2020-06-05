package com.example.aicoin.job;

import com.example.aicoin.dao.fam.FAMNoticeDao;
import com.example.aicoin.dao.zet.CommunityNoticeDao;
import com.example.aicoin.dao.zet.NoticeDao;
import com.example.aicoin.entity.fam.FamNotice;
import com.example.aicoin.entity.zet.CommunityNotice;
import com.example.aicoin.entity.zet.Notice;
import com.example.aicoin.util.btc8.Btc8Course;
import com.example.aicoin.util.btc8.Btc8ReptileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2020/5/30
 */
@Component
public class Btc8Job {

    @Autowired
    private CommunityNoticeDao communityNoticeDao;
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private FAMNoticeDao famNoticeDao;

    @Value("${domain.zetLink}")
    private String zetLink;

    @Value("${domain.famLink}")
    private String famLink;

    @Value("${domain.zetCommunityLink}")
    private String zetCommunityLink;

//    @Scheduled(cron = "0 45 21 2 6 ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void job(){
        try{
            List<CommunityNotice> list = new ArrayList<>();
            Btc8Course.zscb(list);
            list.forEach(item->{
                communityNoticeDao.save(item);
                item.setLink(zetCommunityLink+item.getId());
                communityNoticeDao.save(item);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "* 0/11 * * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void zetrdkxjob(){
        try{
            //获取最大id
            String maxId = noticeDao.getMaxId(2);
            List<Notice> list = new ArrayList<>();
            Btc8ReptileUtil.rdzx1(list,maxId);
            //列表倒着遍历
            for(int i =list.size()-1;i>=0;i--){
                Notice notice = list.get(i);
                noticeDao.save(notice);
                notice.setLink(zetLink+notice.getId());
                noticeDao.save(notice);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "* 0/11 * * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void famrdkxjob(){
        try{
            //获取最大id
            String maxId = noticeDao.getMaxId(2);
            List<FamNotice> list = new ArrayList<>();
            Btc8ReptileUtil.rdzx2(list,maxId);
            //列表倒着遍历
            for(int i =list.size()-1;i>=0;i--){
                FamNotice notice = list.get(i);
                famNoticeDao.save(notice);
                notice.setLink(zetLink+notice.getId());
                famNoticeDao.save(notice);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "* 0/11 * * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void zetxwjob(){
        int[] subTypes = {0, 1, 3, 4};
        String [] maxIds = new String[4];
        List<Notice> list = new ArrayList<>();
        for (int h = 0; h < subTypes.length; h++) {
            //获取最大id
            String maxId = noticeDao.getMaxId(subTypes[h]);
            maxIds[h] = maxId;
        }
        try{
            Btc8ReptileUtil.xw1(list, maxIds);
            //列表倒着遍历
            for(int i =list.size()-1;i>=0;i--){
                Notice notice = list.get(i);
                noticeDao.save(notice);
                notice.setLink(zetLink+notice.getId());
                noticeDao.save(notice);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "* 0/11 * * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void famxwjob(){
        int[] subTypes = {0, 1, 3, 4};
        String [] maxIds = new String[4];
        List<FamNotice> list = new ArrayList<>();
        for (int h = 0; h < subTypes.length; h++) {
            //获取最大id
            String maxId = famNoticeDao.getMaxId(subTypes[h]);
            maxIds[h] = maxId;
        }
        try{
            Btc8ReptileUtil.xw2(list, maxIds);
            //列表倒着遍历
            for(int i =list.size()-1;i>=0;i--){
                FamNotice notice = list.get(i);
                famNoticeDao.save(notice);
                notice.setLink(zetLink+notice.getId());
                famNoticeDao.save(notice);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
