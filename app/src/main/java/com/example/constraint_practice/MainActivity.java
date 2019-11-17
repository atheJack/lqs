package com.example.constraint_practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private ViewPager viewPager;
    private ArrayList<ImageView> imaList;
    private int[] imas;
    private Timer timer;
    private TextView noval_id;
    private ImageView noval_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.viewpage_id);
        noval_id=(TextView)findViewById(R.id.tail_text2);
        noval_img=(ImageView)findViewById(R.id.tail_img2);

        initImag();
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);
        viewPager.addOnPageChangeListener(this);
        noval_id.setOnClickListener(this);
        noval_img.setOnClickListener(this);

        timer=new Timer();
        //startPlay();
        startThread();

    }

    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }).start();
    }

    private void startPlay() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        },0,1000);
    }

    private void initImag() {
        imaList = new ArrayList<ImageView>();
        imas = new int[]{R.drawable.leaves, R.drawable.hotel, R.drawable.malatang};
        for(int i=0;i<imas.length;i++){
            ImageView iv;
            iv=new ImageView(MainActivity.this);
            iv.setImageResource(imas[i]);
            imaList.add(iv);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tail_text2:
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                break;
            case R.id.tail_img2:
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                break;
                default:break;

        }
    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object==view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int newposition =position%imaList.size();
            ImageView imageView=imaList.get(newposition);
            container.removeView(imageView);
            container.addView(imageView);
            return imageView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
