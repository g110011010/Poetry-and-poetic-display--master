package com.example.lbstest;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lbstest.Adapter.SelectMainRecyclerViewPoetAdapter;
import com.example.lbstest.Controller.FontController;
import com.example.lbstest.Entity.Poet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BeiYun on 2017/8/3.
 */
public class SelectActivity extends AppCompatActivity {
    private static final String TAG = "SelectActivity";
    private DrawerLayout selectDrawerLayout;
    private NavigationView seletNavigationView;
    private List<Poet> mPoetList=new ArrayList<>();
    private SelectMainRecyclerViewPoetAdapter selectMainRecyclerViewPoetAdapter;
    private RelativeLayout mapWelcomAndLoadingRelativeLayout;
    private TextView mapWelcomeAndLoadingTime;
    private Animation animationForMapWelcomeAndLoading;
    private int countDownNumber=5;
    private AppBarLayout appBarLayout;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mapWelcomeAndLoadingTime.setText(""+getCount()+"  "+"跳过");
                handler.sendEmptyMessageDelayed(0, 1000);
                animationForMapWelcomeAndLoading.reset();
                mapWelcomeAndLoadingTime.startAnimation(animationForMapWelcomeAndLoading);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        appBarLayout=(AppBarLayout)findViewById(R.id.id_select_appBar);
        appBarLayout.setVisibility(View.GONE);
        mapWelcomeAndLoadingTime=(TextView)findViewById(R.id.id_map_welcomeAndLoading_time) ;
        mapWelcomeAndLoadingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapWelcomAndLoadingRelativeLayout.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });

        showWelcomeAndLoadingSurface();
        Toolbar toolbar=(Toolbar)findViewById(R.id.id_select_toolBar);
        setSupportActionBar(toolbar);
        selectDrawerLayout=(DrawerLayout)findViewById(R.id.id_select_drawer_layout);
        seletNavigationView=(NavigationView)findViewById(R.id.id_select_nav_view);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menus);
        }
        seletNavigationView.setCheckedItem(R.id.id_select_nav_friends);
        seletNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch(item.getItemId()){
//
//                }
                selectDrawerLayout.closeDrawers();
                return true;
            }
        });
        initPoets();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.id_select_recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        selectMainRecyclerViewPoetAdapter=new SelectMainRecyclerViewPoetAdapter(mPoetList);
        recyclerView.setAdapter(selectMainRecyclerViewPoetAdapter);

    }
    private int getCount() {
        countDownNumber--;
        if (countDownNumber == 0) {
            mapWelcomAndLoadingRelativeLayout.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);
        }
        return countDownNumber;
    }
    private void showWelcomeAndLoadingSurface(){

        FontController.setTheRunningAndRegularForTextView(this,(TextView)findViewById(R.id.id_map_welcomeAndLoadingIntroduce1));
        FontController.setTheRunningAndRegularForTextView(this,(TextView)findViewById(R.id.id_map_welcomeAndLoadingIntroduce2));
        animationForMapWelcomeAndLoading= AnimationUtils.loadAnimation(this, R.anim.welcome_countdown);
        mapWelcomAndLoadingRelativeLayout=(RelativeLayout) findViewById(R.id.id_map_WelcomeAndLoading_Layout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCount();
                handler.sendEmptyMessageDelayed(0, 1000);
                mapWelcomeAndLoadingTime.startAnimation(animationForMapWelcomeAndLoading);

            }
        }).start();
    }
    private void initPoets(){
        mPoetList.add(new Poet("李白","li_bai",R.drawable.li_bai));
        mPoetList.add(new Poet("杜甫","DU_FU",R.drawable.du_fu));
        mPoetList.add(new Poet("苏轼","SU_SHE",R.drawable.su_shi));
        mPoetList.add(new Poet("苏洵","SU_XUN",R.drawable.su_xun));
        mPoetList.add(new Poet("苏辙","SU_ZHE",R.drawable.su_zhe));
        mPoetList.add(new Poet("李煜","LI_YU",R.drawable.li_yu));
        mPoetList.add(new Poet("欧阳修","O_YANG_XIU",R.drawable.o_yang_xiu));
        mPoetList.add(new Poet("辛弃疾","XIN_QI_JI",R.drawable.xin_qi_ji));
        mPoetList.add(new Poet("李商隐","LI_SHANG_YIN",R.drawable.li_shang_yin));
        mPoetList.add(new Poet("李清照","LI_QING_ZHAO",R.drawable.li_qing_zhao));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_select_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_select_upload:
                Toast.makeText(this,"uploading",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_select_setting:
                Toast.makeText(this,"setting",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                selectDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
//        Button createDataBase=(Button)findViewById(R.id.selectForLiBai);
//        createDataBase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG,"年份阿西吧");
//                li_bai liBai=new li_bai();
//                liBai.setYear(701);
//                liBai.setParticularYear("武周长安元年辛丑/大足元年");
//                liBai.save();
//                List<li_bai> records= DataSupport.findAll(li_bai.class);
//                for(li_bai record:records){
//                    Log.d(TAG,"年份"+record.getYear());
//                }
//            }
//        });
//        Button specialTest=(Button)findViewById(R.id.button2);
//        specialTest.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
