package org.liufeng.course.service;
import java.io.BufferedReader;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;  
import java.net.URL;  
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;

public class Today_weather_Service {
	
	/** 
     * 发起http get请求获取网页源代码 
     *  
     * @param requestUrl 
     * @return 
     */  
    private static String httpRequest(String requestUrl) {  
        StringBuffer buffer = null;  
  
        try {  
            // 建立连接  
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
  
            // 获取输入流  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            // 读取返回结果  
            buffer = new StringBuffer();  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
          
            // 释放资源  
            bufferedReader.close();  
            inputStreamReader.close();  
            inputStream.close();  
            httpUrlConn.disconnect();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return buffer.toString();  
    }  
  
    /** 
     * 从html中抽取出历史上的今天信息 
     *  
     * @param html 
     * @return 
     */  
    private static String extract(String html) {  
        StringBuffer buffer = null;  
        Pattern p = Pattern.compile("(.*)(<ul class=\"t clearfix\">)(.*?)(</ul>)(.*)");  
        Matcher m = p.matcher(html);
        System.out.println("m="+m);
        if (m.matches()) {  
            buffer = new StringBuffer();  
//            if (m.group(3).contains(getMonthDay(-1)))  
//                dateTag = getMonthDay(-1);  
  
            // 拼装标题  
            buffer.append("≡≡ ").append("最近7天的天气").append(" ≡≡").append("\n\n");  
  
            // 抽取需要的数据  
            for (String info : m.group(3).split("  ")) {  
           info = info.replace("png40", "").replace("win", "").replaceAll("</?[^>]+>", "").trim();  
                // 在每行末尾追加2个换行符  
                if (!"".equals(info)) {  
                    buffer.append(info).append("\n\n");  
                }  
            }  
        }  
        // 将buffer最后两个换行符移除并返回  
        return (null == buffer) ? null : buffer.substring(0, buffer.lastIndexOf("\n\n"));  
    } 
    /** 
     * 封装历史上的今天查询方法，供外部调用 
     *  
     * @return 
     */  
    public static String getTodayInHistoryInfo() {  
        // 获取网页源代码  
        String html = httpRequest("http://www.weather.com.cn/weather/101210401.shtml");  
        // 从网页中抽取信息  
        String result = extract(html);  
  
        return result;  
        
        
    }
//    /** 
//     * 通过main在本地测试 
//     *  
//     * @param args 
//     */  
//    public static void main(String[] args) {  
//        String info = getTodayInHistoryInfo();  
//        System.out.println("info="+info);  
//    }  
//    
}
