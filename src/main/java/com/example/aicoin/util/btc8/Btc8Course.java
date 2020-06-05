package com.example.aicoin.util.btc8;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aicoin.entity.zet.CommunityNotice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2020/6/1
 * 8btc教程
 */
public class Btc8Course {

//    public static final String COURSE_URL = "https://webapi.8btc.com/forum/portal-article-title/list?page_size=560&page=1&order_by=new";
    public static final String COURSE_URL = "https://webapi.8btc.com/forum/portal-article-title/list?page_size=560&page=1&order_by=new";
    public static final String PIC_URL = "https://bbtcdn.8btc.com/data/attachment/";

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void zscb(List<CommunityNotice> list) throws ParseException {
        String result = HttpUtil.doGet(COURSE_URL);
        JSONObject resultObject = JSONObject.parseObject(result);
        JSONObject data = resultObject.getJSONObject("data");
        JSONArray array = data.getJSONArray("list");
        for (int i=array.size()-1;i>0;i--){
            JSONObject jsonObject = array.getJSONObject(i);
            String content = getContent(jsonObject.getString("id"));
            //没内容，跳过 , 内容长度小于50，不保存
            if(content==null || content.length()<50){
                continue;
            }
            String title = jsonObject.getString("title");
            String logo = PIC_URL+jsonObject.getString("pic");
            String datetime = jsonObject.getString("date");
            String username = jsonObject.getString("username");

            CommunityNotice notice = new CommunityNotice();
            notice.setSj(format.parse(datetime));
            notice.setTitle(title);
            notice.setType(5);
            notice.setCount(0);
            notice.setZt(1);
            notice.setContents(content);
            notice.setLogo(logo);
            notice.setRecorder(username);
            list.add(notice);
        }
    }

    //单篇文章url前缀
    public static final String ARTICLE_URL_PREFIX = "https://www.8btc.com/course/";
    public static String getContent(String id) {
        //完整url 前缀+id
        String artUrl = ARTICLE_URL_PREFIX+id;
        System.out.println(artUrl);
        String s1 = HttpUtil.doGet(artUrl);
        Document document = Jsoup.parse(s1);
        Elements elements = document.getElementsByClass("bbt-html");
        //没内容跳过
        if(elements==null || elements.size()==0){
            return null;
        }
        Element element = elements.get(0);
        //没内容跳过
        if(element==null){
            return null;
        }
        Elements elements1 = element.children();
        return elements1.toString();
    }

    public static void main(String[] args) throws ParseException {
        List<CommunityNotice> list = new ArrayList<>();
        Btc8Course.zscb(list);
        list.forEach(item->{
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(item));
            System.out.println(jsonObject.toString());
        });
    }
}
