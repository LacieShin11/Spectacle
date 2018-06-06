package org.androidtown.spectacle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    ActionBar actionBar;
    private FragmentManager fm;
    private ArrayList<Fragment> fList;
    public final String PREFERENCE = "org.androidtown.spectacle";
    private long time= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom);

        // 어플 잠금 스위치 값 받아 올 preference
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        Boolean result = pref.getBoolean("key", false);

        // 스위치 상태가 ON일 경우 어플 시작할 때 암호 입력 activity 띄우기
        if (result == true) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }


        // 스와이프할 뷰페이저를 정의
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // fragment 매니져 객체 정의
        fm = getSupportFragmentManager();

        actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setIcon(R.drawable.app_label);

        // 액션바 모드 설정
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 액션바에 탭 생성 및 추가
        ActionBar.Tab tab1 = actionBar.newTab().setText("날짜").setTabListener(tabListener);
        ActionBar.Tab tab2 = actionBar.newTab().setText("카테고리").setTabListener(tabListener);
        ActionBar.Tab tab3 = actionBar.newTab().setText("통계").setTabListener(tabListener);
        ActionBar.Tab tab4 = actionBar.newTab().setText("채용 정보").setTabListener(tabListener);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
        actionBar.addTab(tab4);

        // fragment 생성 및 추가
        fList = new ArrayList<Fragment>();
        fList.add(FragmentTab1.newInstance());
        fList.add(FragmentTab2.newInstance());
        fList.add(FragmentTab3.newInstance());
        fList.add(FragmentTab4.newInstance());

        // 스와이프로 탭간 이동할 뷰페이저의 리스너 설정
        mViewPager.setOnPageChangeListener(viewPagerListener);

        // 뷰페이져의 Adapter 생성 및 연결
        FragmentPagerAdapterClass adapter = new FragmentPagerAdapterClass(fm, fList);
        mViewPager.setAdapter(adapter);


    }

    // 뒤로 가기 버튼 두번 누르면 종료 시키기
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로가기 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // 뷰페이저 이동시 해당 탭으로 이동
            actionBar.setSelectedNavigationItem(position);
        }
    };

    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭에서 벗어났을때 처리
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭을 선택시 뷰페이저도 이동
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭이 다시 선택됐을때 처리
        }
    };



/*    @Override
    protected void onPause() {
        if (flagKey == true) {
            finish();
            flagKey = false;
            Log.d("ghhan","finish activity!!");
        }
        super.onPause();
    }

    @Override
    protected void onUserLeaveHint() {
        flagKey = true;
        super.onUserLeaveHint();
    }*/
}
