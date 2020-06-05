package com.example.aicoin.util.aicoin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aicoin.entity.fam.FamNotice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;

/**
 * creator  admin002
 * date 2020/3/19
 */
public class AiCionReptileUtil1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(AiCionReptileUtil.class);
    //https://www.aicoin.cn/api/data/moreFlash 参数可以有 firstid 开始id 和 lastid 结束id       数据排序是按照时间倒叙
    //热点资讯
    public static void rdzx(long currentTimeMillis, List<FamNotice> list) {
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
                    FamNotice notice = new FamNotice();
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
    public static void xw(long currentTimeMillis, List<FamNotice> list) {
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
                FamNotice notice = convert(jsonObject, details[h], timeLong, subTypes[h]);
                list.add(notice);
            }
        }
    }

    //转换对象
    public static FamNotice convert(JSONObject jsonObject, String subTitle, long timeLong, int subType) {
        String title = jsonObject.getString("title");
        String id = jsonObject.getString("id");
        String[] contentEdit = getContent(id);
        FamNotice notice = new FamNotice();
        notice.setSj(new Date(timeLong));
        notice.setSubtitle(subTitle+id);
        notice.setTitle(title);
        notice.setType(3);
        notice.setCount(0);
        //如果接口请求频繁，获取不到数据，将contents字段赋值id，后面操作失败列表时使用
        if(contentEdit == null){
            notice.setZt(0);
            notice.setContents(id);
        }else{
            notice.setZt(1);
            notice.setContents(contentEdit[0]);
            notice.setLogo(contentEdit[1]);
        }
        notice.setSubType(subType);
        return notice;
    }
    //单篇文章url前缀
    public static final String ARTICLE_URL_PREFIX = "https://www.aicoin.cn/article/";
    public static final String ARTICLE_URL_SUFFIX = ".html";
    //根据aicion网站文章id获取网页文本信息和logo，返回结果是一个string数组，第一个值是文本信息，第二个值是logo
    public static String[] getContent(String id) {
        String [] strs = new String[2];
        Elements elements = null;
        Elements elements1 = null;
        Element element = null;
        String s1 = null;
        Document document = null;
        String logo = null;
        try{
            //完整url 前缀+id+后缀
            String artUrl = ARTICLE_URL_PREFIX+id+ARTICLE_URL_SUFFIX;
            s1 = HttpUtil.doGet(artUrl);
            document = Jsoup.parse(s1);

            elements = document.getElementsByClass("aicoin-editor");
            if(elements == null || elements.size() == 0) {
                elements = document.getElementsByClass("article-main");
            }
            //请求频繁加入失败列表，补偿请求
            if(elements == null || elements.size() ==0){
                return null;
            }
            //图片图存本地，替换网络图片地址
            element = elements.get(0);
            /*Elements img = element.getElementsByTag("img");
            if(img!=null && img.size()>0){
                for(int i =0;i<img.size();i++){
                    Element item = img.get(i);
                    String url = item.attr("src");
                    //文件存储到article/id 目录下，id是文章网站获取到的
                    String filePath = FileUtil.downloadAndSaveFile(url,"article/"+id, "");
                    item.attr("src",filePath);
                    if(i==0){
                        logo = img.get(0).attr("src");
                    }
                }
            }*/
            elements1 = element.children();
            strs[0]=elements1.toString();
            strs[1]=logo;
        }catch (Exception e){
            LOGGER.info("爬取数据异常，可能是请求过于频繁，id："+id);
            e.printStackTrace();
        }
        return strs;
    }

}
