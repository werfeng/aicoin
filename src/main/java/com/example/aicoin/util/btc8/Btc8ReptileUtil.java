package com.example.aicoin.util.btc8;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aicoin.entity.fam.FamNotice;
import com.example.aicoin.entity.zet.Notice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * date 2020/3/19
 * 8btc快讯和新闻
 */
public class Btc8ReptileUtil {
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String subTitle = "8btc";;
    //热点资讯 1zet 2fam
    public static void rdzx1(List<Notice> list, String maxId) throws ParseException {
//        String URL = "https://gate.8btc.com/w1/news/list?num=20&cat_id=4481&page=1";
        String URL = "https://www.8btc.com/flash";
        String s = HttpUtil.doGet(URL);
        if (s != null) {
            Document document = Jsoup.parse(s);
            Elements titleElements = document.getElementsByClass("flash-item__title");
            Elements contentElements = document.getElementsByClass("flash-item__content");
            Elements timeElements = document.getElementsByClass("flash-item__time");
            if (titleElements == null || titleElements.size() == 0 || contentElements == null || contentElements.size() == 0) {
                return;
            }
            int size = titleElements.size();

            for (int i = 0; i < size; i++) {
                Element element = titleElements.get(i);
                //获取快讯id
                String href = element.attr("href");
                //8btc+id  过滤重复数据
                String id = subTitle+href.substring(href.lastIndexOf("/") + 1);
                if(id.equals(maxId)){
                    break;
                }
                //获取标题
                Elements span = element.getElementsByTag("span");
                String title = span.get(0).text();
                //获取内容详情
                String content = contentElements.get(i).text();
                //时间
                String time = timeElements.get(i).text();
                Notice notice = new Notice();
                notice.setContents(content);
                notice.setSj(format.parse(LocalDate.now().toString()+" "+time+":00"));
                notice.setSubtitle(id);
                notice.setTitle(title);
                notice.setType(3);
                notice.setCount(0);
                notice.setZt(1);
                notice.setSubType(2);
                list.add(notice);
            }
        }
    }

    public static void rdzx2(List<FamNotice> list, String maxId) throws ParseException {
//        String URL = "https://gate.8btc.com/w1/news/list?num=20&cat_id=4481&page=1";
        String URL = "https://www.8btc.com/flash";
        String s = HttpUtil.doGet(URL);
        if (s != null) {
            Document document = Jsoup.parse(s);
            Elements titleElements = document.getElementsByClass("flash-item__title");
            Elements contentElements = document.getElementsByClass("flash-item__content");
            Elements timeElements = document.getElementsByClass("flash-item__time");
            if (titleElements == null || titleElements.size() == 0 || contentElements == null || contentElements.size() == 0) {
                return;
            }
            int size = titleElements.size();

            for (int i = 0; i < size; i++) {
                Element element = titleElements.get(i);
                //获取快讯id
                String href = element.attr("href");
                //8btc+id  过滤重复数据
                String id = subTitle+href.substring(href.lastIndexOf("/") + 1);
                if(id.equals(maxId)){
                    break;
                }
                //获取标题
                Elements span = element.getElementsByTag("span");
                String title = span.get(0).text();
                //获取内容详情
                String content = contentElements.get(i).text();
                //时间
                String time = timeElements.get(i).text();
                FamNotice notice = new FamNotice();
                notice.setContents(content);
                notice.setSj(format.parse(LocalDate.now().toString()+" "+time+":00"));
                notice.setSubtitle(id);
                notice.setTitle(title);
                notice.setType(3);
                notice.setCount(0);
                notice.setZt(1);
                notice.setSubType(2);
                list.add(notice);
            }
        }
    }

    //新闻
    //类型对应 前面是aicion类型 后面是本地类型
    //精选-头条  分析-金融  区块链-技术  减产-矿业
//    头条—海盗号
//    专栏—独家
//    金融—创投
//    矿业—矿业
    //海盗号
//    public static final String TT_URL = "https://www.8btc.com/news?cat_id=6167";
    public static final String TT_URL = "https://webapi.8btc.com/bbt_api/news/list?num=20&page=1&cat_id=6167";
    //独家
    public static final String ZL_URL = "https://webapi.8btc.com/bbt_api/news/list?num=20&page=1&cat_id=6168";
    //创投
    public static final String JR_URL = "https://webapi.8btc.com/bbt_api/news/list?num=20&page=1&cat_id=1647";
    //矿业
    public static final String KY_URL = "https://webapi.8btc.com/bbt_api/news/list?num=20&page=1&cat_id=6";

    public static final String [] urls = {TT_URL, ZL_URL, JR_URL, KY_URL};

    public static final int[] subTypes = {0, 1, 3, 4};

    public static void xw1(List<Notice> list, String [] maxIds) {
        for (int h = 0; h < urls.length; h++) {
            String s = HttpUtil.doGet(urls[h]);
            JSONObject returnObj = JSONObject.parseObject(s);
            JSONObject data = returnObj.getJSONObject("data");
            JSONArray array = data.getJSONArray("list");
            int size = array.size();
            String maxId = maxIds[h];
            int subType = subTypes[h];
            load:for (int i = 0; i < size; i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("id");
                String noticeId = subTitle + id;
                if(noticeId.equals(maxId)){
                    break load;
                }
                long timeLong = jsonObject.getLong("post_date") * 1000;
                String title = jsonObject.getString("title");
                String logo = jsonObject.getString("image");
                String contentEdit = getContent(id);
                if(contentEdit==null){
                    continue;
                }
                Notice notice = new Notice();
                notice.setSj(new Date(timeLong));
                notice.setSubtitle(noticeId);
                notice.setTitle(title);
                notice.setType(3);
                notice.setCount(0);
                notice.setZt(1);
                notice.setContents(contentEdit);
                notice.setSubType(subType);
                notice.setLogo(logo);
                list.add(notice);
            }
        }
    }

    public static void xw2(List<FamNotice> list, String [] maxIds) {
        for (int h = 0; h < urls.length; h++) {
            String s = HttpUtil.doGet(urls[h]);
            JSONObject returnObj = JSONObject.parseObject(s);
            JSONObject data = returnObj.getJSONObject("data");
            JSONArray array = data.getJSONArray("list");
            int size = array.size();
            String maxId = maxIds[h];
            int subType = subTypes[h];
            load:for (int i = 0; i < size; i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("id");
                String noticeId = subTitle + id;
                if(noticeId.equals(maxId)){
                    break load;
                }
                long timeLong = jsonObject.getLong("post_date") * 1000;
                String title = jsonObject.getString("title");
                String logo = jsonObject.getString("image");
                String contentEdit = getContent(id);
                if(contentEdit==null){
                    continue;
                }
                FamNotice notice = new FamNotice();
                notice.setSj(new Date(timeLong));
                notice.setSubtitle(noticeId);
                notice.setTitle(title);
                notice.setType(3);
                notice.setCount(0);
                notice.setZt(1);
                notice.setContents(contentEdit);
                notice.setSubType(subType);
                notice.setLogo(logo);
                list.add(notice);
            }
        }
    }


    //单篇文章url前缀
    public static final String ARTICLE_URL_PREFIX = "https://www.8btc.com/media/";

    //根据aicion网站文章id获取网页文本信息
    public static String getContent(String id) {
        //完整url 前缀+id+后缀
        String artUrl = ARTICLE_URL_PREFIX + id;
        String s1 = HttpUtil.doGet(artUrl);
        Document document = Jsoup.parse(s1);
        Elements elements = document.getElementsByClass("bbt-html");
        if (elements == null || elements.size() == 0) {
            return null;
        }
        Element element = elements.get(0);
        Elements elements1 = element.children();
        return elements1.toString();
    }

    public static void main(String[] args) throws ParseException {
        /*String id = "605792";
        List<Notice> list = new ArrayList<>();
        rdzx(list,id);
        list.forEach(item->{
            String s = JSONObject.toJSONString(item);
            System.out.println(s);
        });*/

        String s = HttpUtil.doGet(TT_URL);
        System.out.println(s);
        /*int[] subTypes = {0, 1, 3, 4};
        String [] maxIds = new String[4];
        List<Notice> list = new ArrayList<>();
        try{
            Btc8ReptileUtil.xw(list, maxIds);
            //列表倒着遍历
            for(int i =list.size()-1;i>=0;i--){
                Notice notice = list.get(i);
                System.out.println(JSONObject.toJSONString(notice));
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }
}
