package com.brioal.lzutest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.Scanner;

/**
 * Created by null on 15-11-4.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        //输入个人信息，来获取登陆和成绩查询所需要的表单数据

        Scanner keyin = new Scanner(System.in);
        String username = null ;  //存储用户名  ，网页表单为j_username
        String password = null ; // 存储密码    , 网页表单为j_password
        String year = null; // 查询时传入的年份的数据  ，格式为2位整数 ，例如34表示 13年入学，查询14年的成绩
        int  term = 0 ; // 存储要查询的学期 ， 此处此数据下文进行处理 ， 春季为1 ，秋季为2 ，但秋季为上学期，春季为下学期
        System.out.println("请输入用户名");
        username = keyin.nextLine();
        int y = Integer.valueOf(username.charAt(4)+"") ;  // 获取到入学年份 ， 13年则获取3
        int y_ = 0 ; // 存储当前要查询的年份 ， 14则4
        System.out.println("请输入密码");
        password = keyin.nextLine();
        System.out.println("请选择学年： 1 . 大一 ， 2 . 大二  3 . 大三   4.. 大四");
        switch(keyin.nextInt()){
            case 1 :
               y_ = y+0;
                break ;
            case 2:
                y_ = y+1;
                break ;
            case 3 :
                y_ = y+2;
                break ;
            case 4 :
                y_ = y+3;
                break ;
        }
        System.out.println("请选择学期： 1. 上学期   2. 下学期");
        switch(keyin.nextInt()){
            case 1 :
                term = 2 ;
                break ;
            case 2 :
                y_+=1; // 如大一下学期，则与上学期不同的地方在于年份往后一年
                        // 大一上 为33 秋 ，大一下为34 春
                term = 1 ;
                break ;
        }
        year = y+""+y_; // 将入学年份和要查询的年份拼接
        //登陆信息拼接
        String logo_params = "j_username="+username+"&"+ "j_password="+password;
        //查询成绩信息拼接
        String grade_params =  "year="+year+"&term="+term+"&para=0&sortColumn=&Submit=查询";

        //所访问的连接
        //登陆连接
        String URL_Logo = "http://jwc.lzu.cn/academic/j_acegi_security_check";
        //查询成绩连接
        String URL_QueryGrade = "http://jwc.lzu.cn/academic/manager/score/studentOwnScore.do?groupId=&moduleId=2020";
//     查询课表连接 ，此处未使用
// String URL_QueryClass = "http://jwc.lzu.cn/academic/student/currcourse/currcourse.jsdo?groupId=&moduleId=2000";
//        封装传入类
        Info from_logo = GetLzu.Post(new Info(URL_Logo, null, logo_params)); // 此处已进行登陆操作
//        System.out.println(from_logo.getData());
        from_logo.setUrl(URL_QueryGrade); // 更改连接
        from_logo.setData(grade_params); // 更改传入表单
        Info from_grade = GetLzu.Post(from_logo);  // 此处已进行查询成绩操作
        String data = from_grade.getData(); // 获取到查询成绩网页的源代码
        Document doc = Jsoup.parse(data); // 用三方jar包进行解析，获取文档


        Elements tables = doc.select("table"); // 获取到table ，
            // 此处有两个table ，第一个为查询的表头，无用，直接使用第二个table
        Elements trs = null ;
        //此处使用了指定下标的方法，则如果网页访问出错则代码会数组越界错误从而终端，
        // 此操作是为了防止网络连接所带来的程序中断
        if (tables.size()>1) {
            trs = tables.get(1).select("tr"); // 获取到表头为tr所有元素 。tr存储的是课程的信息
        }
        Elements ths = trs.select("th"); // th存储的是标题的信息
        String text1 = ths.text();
        String s1 = text1.replaceAll("<th> ", "");  //除去th开头的字符串，从而直接获得中文
//        System.out.print(s1 + "\t\t\t");
        System.out.println();
//        此处原理一样，解析课程信息的数据
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String text = tds.text();
            String s = text.replaceAll("<th> ", "");
//            System.out.print(s + "\t\t\t");
            System.out.println();
        }
    }
}



