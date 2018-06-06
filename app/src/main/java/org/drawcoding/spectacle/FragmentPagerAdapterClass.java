package org.drawcoding.spectacle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class FragmentPagerAdapterClass extends FragmentPagerAdapter {
    private ArrayList<Fragment> fList;

    // Adapter 생성자
    public FragmentPagerAdapterClass(FragmentManager fm, ArrayList<Fragment> fList) {
        super(fm);
        this.fList = fList;
    }

    // 탭 이름 반환
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "날짜";
            case 1:
                return "카테고리";
            case 2:
                return "통계";
            case 3:
                return "채용 정보";
        }
        return null;
    }

    // 선택된 fragment를 호출
    @Override
    public Fragment getItem(int position) {
        return this.fList.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}

