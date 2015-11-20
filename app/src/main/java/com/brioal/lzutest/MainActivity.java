package com.brioal.lzutest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etName ,etWord ;
    private Button btnLogo;
    private LinearLayout layout ;
    private Handler handler =null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("兰大教务处");
        setSupportActionBar(toolbar);

        initViews();
        setViews();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                TextView textView = new TextView(MainActivity.this);
                Bundle bundle = msg.getData();
                textView.setText(bundle.getString("text"));
                System.out.println(bundle.getString("text"));
                layout.addView(textView);
            }
        } ;

    }

    public void initViews() {
        etName = (EditText) findViewById(R.id.etUsername);
        etWord = (EditText) findViewById(R.id.etPassword);
        btnLogo = (Button) findViewById(R.id.btnLogo);
        layout = (LinearLayout) findViewById(R.id.layout);
    }

    public void setViews() {
        etName.setText("320130937631");
        etWord.setText("142536");
        btnLogo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogo:
                System.out.println("start");
                new MyThread().start();

                break;
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            String logo_params = "j_username="+etName.getText().toString()+"&"+ "j_password="+etWord.getText().toString();
            String grade_params =  "year=33&term2=&para=0&sortColumn=&Submit=查询";
            String URL_Logo = "http://jwc.lzu.cn/academic/j_acegi_security_check";
            String URL_QueryGrade = "http://jwc.lzu.cn/academic/manager/score/studentOwnScore.do?groupId=&moduleId=2020";
            Info from_logo = GetLzu.Post(new Info(URL_Logo, null, logo_params));
//            System.out.println(from_logo.getData());
            from_logo.setUrl(URL_QueryGrade);
            from_logo.setData(grade_params);
            Info from_grade = GetLzu.Post(from_logo);
            String data = from_grade.getData();
//            System.out.println(data);
            Document doc = Jsoup.parse(data);
            Elements tables = doc.select("table");
            Elements trs = null ;

                trs = tables.get(1).select("tr");

            Elements ths = trs.select("th");
            String text1 = ths.text();
            String s1 = text1.replaceAll("<th> ", "");
//            System.out.print(s1 + "\t\t\t");
//            System.out.println();
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("text", s1);
            msg.setData(bundle);
            handler.sendMessage(msg);
            String [] [] datas = new String[trs.size()][15];
            for (int i = 0; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                String text = tds.text();
                String s = text.replaceAll("<th> ", "");
                datas[i]=s.split(" +");

                Message msg1 = new Message();
                Bundle bundle1 = new Bundle();
//                bundle1.putString("text", s1);
                bundle1.putString("text", s);
                msg1.setData(bundle1);
                handler.sendMessage(msg1);
            }
        }
        }
    }

