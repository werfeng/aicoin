package com.example.aicoin.util.aicoin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aicoin.entity.zet.Notice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * creator  xwf
 * date 2020/3/19
 */
public class AiCionReptileUtil {
    //https://www.aicoin.cn/api/data/moreFlash 参数可以有 firstid 开始id 和 lastid 结束id       数据排序是按照时间倒叙
    //热点资讯
    public static void rdzx(long currentTimeMillis, List<Notice> list) {
        String URL = "https://www.aicoin.cn/api/data/moreFlash?pagesize=100";
        String s = HttpUtil.doGet(URL);
        if(s!=null){
            JSONObject returnObj = JSONObject.parseObject(s);
            JSONObject data = returnObj.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("content");
            int size = jsonArray.size();
            String subTitle = "aicoin资讯";
            for (int i=0;i<size;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("type");
                if(type.equals("1")){
                    long timeLong = jsonObject.getLong("time")*1000;
                    if(timeLong<currentTimeMillis){
                        break;
                    }
                    String body = jsonObject.getString("title");
                    int i1 = body.lastIndexOf("【");
                    int i2 = body.lastIndexOf("】");
                    String title = body.substring(0,i1)+body.substring(i1+1, i2);
                    String content = body.substring(i2+1);
                    String contentEdit = content;
                    Notice notice = new Notice();
                    notice.setContents(contentEdit);
                    notice.setSj(new Date(timeLong));
                    notice.setSubtitle(subTitle+jsonObject.getString("id"));
                    notice.setTitle(title);
                    notice.setType(3);
                    notice.setCount(0);
                    notice.setZt(1);
                    notice.setSubType(2);
                    list.add(notice);
                }
            }
        }
    }

    //新闻
    //类型对应 前面是aicion类型 后面是本地类型
    //精选-头条  分析-金融  区块链-技术  减产-矿业
    //头条
    public static final String TT_URL = "https://www.aicoin.cn/api/data/news?cat=all";
    //金融
    public static final String JR_URL = "https://www.aicoin.cn/api/data/news?cat=market-analysis";
    //技术
    public static final String JS_URL = "https://www.aicoin.cn/api/data/news?cat=block-chain";
    //矿业
    public static final String KY_URL = "https://www.aicoin.cn/api/data/news?cat=halving";
    public static void xw(long currentTimeMillis, List<Notice> list) {
        String[] urls = {TT_URL,JR_URL,JS_URL,KY_URL};
        String[] details = {"aicoin头条","aicoin金融","aicoin技术","aicoin矿业"};
        int[] subTypes = {0,3,5,4};
        for(int h=0;h<urls.length;h++){
            String s = HttpUtil.doGet(urls[h]);
            JSONObject returnObj = JSONObject.parseObject(s);
            JSONArray news = returnObj.getJSONArray("news");
            for (int i =0; i<news.size();i++){
                JSONObject jsonObject = news.getJSONObject(i);
                long timeLong = jsonObject.getLong("createtime")*1000;
                if(timeLong<currentTimeMillis){
                    break;
                }
                Notice notice = convert(jsonObject, details[h], timeLong, subTypes[h]);
                list.add(notice);
            }
        }
    }

    //转换对象
    public static Notice convert(JSONObject jsonObject, String subTitle, long timeLong, int subType) {
        String title = jsonObject.getString("title");
        String logo = jsonObject.getString("cover");
        String id = jsonObject.getString("id");
        String contentEdit = getContent(id);
        Notice notice = new Notice();
        notice.setSj(new Date(timeLong));
        notice.setSubtitle(subTitle+id);
        notice.setTitle(title);
        notice.setType(3);
        notice.setCount(0);
        if(contentEdit == null){
            notice.setZt(0);
        }else{
            notice.setZt(1);
            notice.setContents(contentEdit);
        }
        notice.setSubType(subType);
        notice.setLogo(logo);
        return notice;
    }
    //单篇文章url前缀
    public static final String ARTICLE_URL_PREFIX = "https://www.aicoin.cn/article/";
    public static final String ARTICLE_URL_SUFFIX = ".html";
    //根据aicion网站文章id获取网页文本信息
    public static String getContent(String id) {
        //完整url 前缀+id+后缀
        String artUrl = ARTICLE_URL_PREFIX+id+ARTICLE_URL_SUFFIX;
        String s1 = HttpUtil.doGet(artUrl);
        Document document = Jsoup.parse(s1);
        Elements elements = document.getElementsByClass("aicoin-editor");
        if(elements == null || elements.size() == 0) {
            elements = document.getElementsByClass("article-main");
        }
        //接口请求频繁，可能会获取到错误提示页面，fam已修改
        if(elements==null || elements.size()==0){
            return null;
        }
        Element element = elements.get(0);
        Elements elements1 = element.children();
        return elements1.toString();
    }

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis()-1000*60*600;
        List<Notice> list = new ArrayList<>();
        xw(currentTimeMillis, list);

        String s = JSONArray.toJSONString(list);
        System.out.println(s);
    }
}
