package com.brioal.lzutest;

/**
 * Created by null on 15-11-4.
 * 封装的数据类
 数据：
 url ：存储要打开的链接
 cookies ； 存储传入的cookie
 data ： 用于从网页获取到的源代码


 此数据类同时用于Post和Get请求的传入值和返回值
 Test类中仅使用Post：
                1.登陆时传入post的Info对象中 url为登陆按钮的连接，cookie为空，data为用户名和密码
                        返回时cookie为登陆后网页的cookie ，data为网页源代码 ，url为空
                2.查询成绩时使用登陆传回的info对象，更改url为查询按钮连接，cookie不变，data为查询所需传入的数据



 */
public class Info {
    private String url ;  //连接
    private String cookie ; //网页cookie
    private String data ; // 表单数据或者网页源代码

    public Info() {
    }

    public Info(String url, String cookie, String data) {
        this.url = url;
        this.cookie = cookie;
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
