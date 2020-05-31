package com.example.aicoin.job;

import com.example.aicoin.dao.fam.FAMNoticeDao;
import com.example.aicoin.dao.zet.NoticeDao;
import com.example.aicoin.entity.fam.FamNotice;
import com.example.aicoin.entity.zet.Notice;
import com.example.aicoin.util.AiCionReptileUtil;
import com.example.aicoin.util.AiCionReptileUtil1;
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
public class FJob {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private FAMNoticeDao famNoticeDao;

    @Value("${domain.zetLink}")
    private String zetLink;

    @Value("${domain.famLink}")
    private String famLink;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void job(){
        try{
            //大于上半个小时时间
            long currentTimeMillis = System.currentTimeMillis()-1000*60*60;
            List<Notice> list = new ArrayList<>();
            //热点资讯
            AiCionReptileUtil.rdzx(currentTimeMillis, list);
            //新闻
            AiCionReptileUtil.xw(currentTimeMillis, list);
            //时间倒叙，需要倒着遍历插入
            if(list.size()>0){
                for(int i = list.size()-1; i>=0; i--){
                    Notice notice = list.get(i);
                    //获取文章详情不成功，不新增到数据库
                    if(notice.getZt()==0){
                        continue;
                    }
                    noticeDao.save(notice);
                    notice.setLink(zetLink+notice.getId());
                    noticeDao.save(notice);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void job1(){
        try {
            //大于上半个小时时间
            long currentTimeMillis = System.currentTimeMillis()-1000*60*60;
            List<FamNotice> list = new ArrayList<>();
            //热点资讯
            AiCionReptileUtil1.rdzx(currentTimeMillis, list);
            //新闻
            AiCionReptileUtil1.xw(currentTimeMillis, list);
            //失败列表
            List<FamNotice> failList = new ArrayList<>();
            //时间倒叙，需要倒着遍历插入
            if (list.size() > 0) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    FamNotice notice = list.get(i);
                    //补偿
                    if (notice.getZt() == 0) {
                        failList.add(notice);
                        continue;
                    }
                    famNoticeDao.save(notice);
                    notice.setLink(famLink + notice.getId());
                    famNoticeDao.save(notice);
                }
            }
            //补偿
            if (failList.size() > 0) {
                //如果补偿列表存在数据，为了防止接口请求频繁，当前线程暂停20s
                Thread.sleep(20000L);
                failList.forEach(item -> {
                    String[] strs = AiCionReptileUtil1.getContent(item.getContents());
                    item.setContents(strs[0]);
                    item.setLogo(strs[1]);
                    famNoticeDao.save(item);
                    item.setLink(famLink + item.getId());
                    famNoticeDao.save(item);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
