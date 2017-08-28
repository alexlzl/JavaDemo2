package com.test;

public enum NewsRSSFeedEnum {
	// 雅虎头条新闻 RSS 种子
    YAHOO_TOP_STORIES("http://rss.news.yahoo.com/rss/topstories"), 
    
    //CBS 头条新闻 RSS 种子
    CBS_TOP_STORIES("http://feeds.cbsnews.com/CBSNewsMain?format=xml"), 
    
    // 洛杉矶时报头条新闻 RSS 种子
    LATIMES_TOP_STORIES("http://feeds.latimes.com/latimes/news?format=xml"); 
        
    // 枚举对象的 RSS 地址的属性
    private String rss_url; 
        
    // 枚举对象构造函数
    private NewsRSSFeedEnum(String rss) { 
        this.rss_url = rss; 
        System.out.println("NewsRSSFeedEnum");
    } 
        
    // 枚举对象获取 RSS 地址的方法
    public String getRssURL() { 
        return this.rss_url; 
    } 
    
    public static void main(String[] str){
    	System.out.println(NewsRSSFeedEnum.CBS_TOP_STORIES);
    }
}
