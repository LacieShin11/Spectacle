package org.androidtown.spectacle;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SlidingDrawer;
import android.widget.Toast;


public class FragmentTab4 extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private SlidingDrawer slidingDrawer;
    private Button slidingBtn;
    private Drawable upIcon, downIcon;
    private CheckBox[] checkBoxes = new CheckBox[14];
    private Integer[] checkID = {R.id.check1, R.id.check2, R.id.check3, R.id.check4, R.id.check5, R.id.check6,
            R.id.check7, R.id.check8, R.id.check9, R.id.check10, R.id.check11, R.id.check12, R.id.check13,
            R.id.check14};

    public static FragmentTab4 newInstance() {
        FragmentTab4 fragment = new FragmentTab4();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true); //메뉴 버튼 활성화
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4, container, false);

        slidingBtn = (Button) view.findViewById(R.id.handle);
        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.slide_drawer);
        upIcon = getContext().getResources().getDrawable(R.drawable.up_icon_custom);
        downIcon = getContext().getResources().getDrawable(R.drawable.down_icon_custom);

        //체크박스 초기화
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = (CheckBox) view.findViewById(checkID[i]);
        }


        //전체선택 클릭시
        checkBoxes[13].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < checkBoxes.length - 1; i++) {
                    checkBoxes[i].setChecked(isChecked);
                }
            }
        });

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slidingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, downIcon);
                slidingBtn.setPadding(0, 0, 0, 5);
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingBtn.setCompoundDrawablesWithIntrinsicBounds(null, upIcon, null, null);
                slidingBtn.setPadding(0, 5, 0, 0);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag4, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {

        } else if (id == R.id.login) { //로그인
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) { //SD카드에 엑셀 파일 생성

        }


        return true;
    }

    //체크박스 변경시의 이벤트
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*
        for (int i = 0; i < checkBoxes.length - 1; i++) {
            if(checkBoxes[i].isChecked() && !checkBoxes[13].isChecked())
                checkBoxes[13].setChecked(true);
        }*/

        if(buttonView.getId() == R.id.check1)
            Log.d("Check1", ((CheckBox)buttonView).isChecked() + "");
    }
}
