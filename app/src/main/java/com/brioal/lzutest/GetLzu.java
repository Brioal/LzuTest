package com.brioal.lzutest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by null on 15-11-4.
 *
 * 此类提供三个公共方法，用于Post操作，Get操作，和获取网页源代码
 */
public class GetLzu {



    //Post操作，传入url ， cookie ， 表单数据
    //返回网页的cookie ，网页源代码
    public static Info Post(Info info) {
        Info result = new Info(); //新建返回值对象
        HttpURLConnection connection = null ;
        URL url;
        try {
//            System.out.println(info.getUrl());
            url = new URL(info.getUrl());
            connection = (HttpURLConnection) url.openConnection();//创建Connection对象
            connection.setDoInput(true);//表示从服务器获取数据
            connection.setDoOutput(true);//表示向服务器写数据
            connection.setRequestMethod("POST");//设置post请求方式
            connection.setUseCaches(false); //设置不使用缓存
            if (info.getCookie() != null) { // 如果cookie为空，说明次网页是登陆网页不需要设置cookie，则会跳过此项
                connection.setRequestProperty("Cookie", "JSESSIONID="+info.getCookie());
            }
            connection.getOutputStream().write(info.getData().getBytes());   //将用户名和密码写入到流
//            Thread.sleep(3000);
            connection.connect(); //连接
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 如果网页的返回值正确，则进行数据操作
                String s =  GetLzu.getHtml(connection.getInputStream(), "UTF-8"); //获取网页源代码
//                System.out.println(s);
                result.setData(s);  // 返回值对象设置data数据
                int a = s.lastIndexOf("jsessionid="); //获取cookie的开头的下标
                // 获取cookie之后的第一个标识符的下标
                if (info.getCookie() == null) {
                    result.setCookie(s.substring(a+11, a+47));  // 通过对网页格式的判断，
                                                                    // 此项可以获取到正确返回网页的cookie
                    //当连接次数过多。或者网络不稳定时此处会出现字符串越界问题
                    // ，下文针对Test方法添加了第二次使用post之后对connecttion的关闭操作，
                    // 减少了此处出错的概率，但未彻底解决

                }
//                System.out.println(info.getCookie());
                connection.disconnect(); // 关闭连接
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
         //Get操作。传入cookie，返回网页源代码
    //  此Test未使用该方法
    public static String Get(Info info) {
        String result = null;
        try {
            URL url = new URL(info.getUrl());
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Cookie", "JSESSIONID="+info.getCookie());
                httpURLConnection.connect();
                result = GetLzu.getHtml(httpURLConnection.getInputStream(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  result;
    }

    //通过输入流来获取网页的源代码
    //传入输入流 ，网页编码格式
    //返回网页源代码
    public static String getHtml(InputStream inputStream, String encode) {
        InputStream is = inputStream; // 传入网页的输入流
        String code = encode; // 网页的编码方式
        BufferedReader reader = null;
        StringBuffer sb = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, encode));
            sb = new StringBuffer();
            String str = null;
            while ((str = reader.readLine()) != null) {
                if (str.isEmpty()) {
                    //如果读取到空行则不添加到字符串
                } else {
                    sb.append(str);
                    sb.append("\n");
                }
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
