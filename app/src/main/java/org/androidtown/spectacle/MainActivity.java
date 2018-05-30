package org.androidtown.spectacle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    ActionBar actionBar;
    private FragmentManager fm;
    private ArrayList<Fragment> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom);

        // 스와이프할 뷰페이저를 정의
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // fragment 매니져 객체 정의
        fm = getSupportFragmentManager();

        actionBar = getSupportActionBar();

        if(actionBar != null)
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

}
