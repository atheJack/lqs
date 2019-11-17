package com.example.constraint_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button get_noval_resource;
    private TextView noval_content;
    private Button last_chapter;
    private Button next_chapter;
    private String websites;
    private int count=0;
    private boolean flag=false;
    private TextView noval_chapter;
    private String chapter_next_address;  //记录下一章的网址
    private String chapter_prev_address; //记录上一章的网址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        get_noval_resource=(Button)findViewById(R.id.header_btn1);
        noval_content=(TextView)findViewById(R.id.noval_content);
        last_chapter=(Button)findViewById(R.id.tail_btn1);
        next_chapter=(Button)findViewById(R.id.tail_btn2);
        noval_chapter=(TextView)findViewById(R.id.noval_chapter);
        websites="https://read.qidian.com/chapter/GRXKztRHlME1/pSrkQ4m7qec1";
        get_noval_resource.setOnClickListener(this);
        last_chapter.setOnClickListener(this);
        next_chapter.setOnClickListener(this);
    }

    private void beginGetResource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(websites)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    showResponse(responseData);
                    noval_chapter.setText("第"+(count+1)+"章");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    private void showResponse(final String responseData) {
        final String resp;
        StringBuilder stringBuilder=new StringBuilder();
        Document doc=Jsoup.parse(responseData); //开始解析html
        //以下操作与js神似，具体操作看网页结构
        Element chapterElement=doc.getElementById("j_chapterBox");
        Elements element1=chapterElement.getElementsByClass("read-content");
        Elements contents=element1.first().getElementsByTag("p");
        if(count>0){
            Element chapterPrev=doc.getElementById("j_chapterPrev");
            chapter_prev_address="https:"+chapterPrev.attr("href"); //获取href中的网址
        }
        Element chapterNext=doc.getElementById("j_chapterNext");
        chapter_next_address="https:"+chapterNext.attr("href");

        for(Element content:contents){
            stringBuilder.append(content.text()+'\n'); //text()方法直接提取文本
        }
        resp=stringBuilder.toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noval_content.setText(resp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_btn1:
                flag=true;
                Toast.makeText(Main2Activity.this,"正在获取小说，请稍等",Toast.LENGTH_SHORT).show();
                beginGetResource();
                break;
            case R.id.tail_btn1:
                if(count>0){
                    count--;
                    websites=chapter_prev_address;
                    beginGetResource();
                }else{
                    Toast.makeText(Main2Activity.this,"已经是第一章了哦！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tail_btn2:
                if(flag){
                    count++;
                    websites=chapter_next_address;
                    beginGetResource();
                }else{
                    Toast.makeText(Main2Activity.this,"请先点击左上方按钮以获取小说！",Toast.LENGTH_SHORT).show();
                }

                break;
                default:break;
        }
    }
}
