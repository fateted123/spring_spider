package com.gxy.atm.service;


import com.gxy.atm.entity.Movie;
import com.gxy.atm.mapper.MovieMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BussService {

    private static Logger log = LoggerFactory.getLogger(BussService.class);

    @Resource
    private MovieMapper movieMapper;


    @Resource
    private HttpClientService httpClientService;

    public void spiderMovie() throws IOException {

        log.info("正在分析。。。");

        //获取南京所有正在热映的电影html文档
        String html = httpClientService.doGet("https://movie.douban.com/cinema/nowplaying/nanjing/");
        log.info("南京正在热映电影：{}", html);
        //jsoup解析
        Document doc = Jsoup.parse(html);

        Elements moviesEles = doc.select("#nowplaying > div.mod-bd > ul > li");
        log.info("查询到电影总数：" + moviesEles.size());

        for (Element movieEle : moviesEles) {
            Movie movie = new Movie();

            log.info(">>>>>" + movieEle.html());

            //电影名称
            String movieName = movieEle.select("li.stitle > a").text();
            movie.setMoviename(movieName);


            //图片
            String img = movieEle.select("li.poster > a > img").attr("src");
            String imgName = img.substring(img.lastIndexOf("/"));
            String dirPath = "I:\\movies-pic\\";
            String imgUrl = httpClientService.downloadPicture(img, dirPath, imgName);
            movie.setPicpath(imgUrl);


            //再请求详情
            String movieInfoUrl = movieEle.select("li.poster > a").attr("href");

            String movieInfoHtml = httpClientService.doGet(movieInfoUrl);
            //jsoup解析
            Document movidInfoDoc = Jsoup.parse(movieInfoHtml);

            //爬取简介
            Element element = movidInfoDoc.select("#link-report").first();
            Elements linkSpans = element.select("> span");
            for (Element linkSpan : linkSpans) {
                if (linkSpan.childNodeSize() > 0) {
                    movie.setDesc(linkSpan.getElementsByAttributeValue("property", "v:summary").text());
                }
            }

            //爬取主要信息
            Element info = movidInfoDoc.select("#info").first();
            Elements spans = info.select("> span");
            for (Element span : spans) {
                if (span.childNodeSize() > 0) {
                    String key = span.getElementsByAttributeValue("class", "pl").text();
                    if ("导演".equals(key)) {
                        movie.setDirector(span.getElementsByAttributeValue("class", "attrs").text());
                    } else if ("编剧".equals(key)) {
                        movie.setWriters(span.getElementsByAttributeValue("class", "attrs").text());

                    } else if ("主演".equals(key)) {
                        movie.setStarring(span.getElementsByAttributeValue("class", "attrs").text());

                    } else if ("类型:".equals(key)) {
                        movie.setType(movidInfoDoc.getElementsByAttributeValue("property", "v:genre").text());

                    } else if ("制片国家/地区:".equals(key)) {
                        Pattern patternCountry = Pattern.compile(".制片国家/地区:</span>(.+\\s+)<br>");
                        Matcher matcherCountry = patternCountry.matcher(movidInfoDoc.html());
                        if (matcherCountry.find()) {
                            movie.setProducercountries(matcherCountry.group(1));// for example: >制片国家/地区:</span> 中国大陆 / 香港     <br>

                        }
                    } else if ("语言:".equals(key)) {
                        Pattern patternLanguage = Pattern.compile(".语言:</span>(.+\\s+)<br>");
                        Matcher matcherLanguage = patternLanguage.matcher(movidInfoDoc.html());
                        if (matcherLanguage.find()) {
//                            movie.setLanguage(matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());
                            movie.setLanguage(matcherLanguage.group(1));

                        }
                    } else if ("上映日期:".equals(key)) {
                        movie.setReleasedate(movidInfoDoc.getElementsByAttributeValue("property", "v:initialReleaseDate").text());

                    } else if ("片长:".equals(key)) {
                        movie.setFilmlength(movidInfoDoc.getElementsByAttributeValue("property", "v:runtime").text());

                    }
                }
            }

            log.info(">>>>>>>>{}", movie);
            movieMapper.insert(movie);

        }


    }

}
