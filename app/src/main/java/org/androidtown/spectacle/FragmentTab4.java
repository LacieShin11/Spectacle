package org.androidtown.spectacle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class FragmentTab4 extends Fragment {

    private ListView listView;
    private boolean lastItemFlag;
    private EmploymentListAdapter adapter;
    private SlidingDrawer slidingDrawer;
    private Button slidingBtn;
    private Drawable upIcon, downIcon;
    private CheckBox[] checkBoxes = new CheckBox[14];
    private boolean[] isCheckedArray = new boolean[14];
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

        listView = (ListView) view.findViewById(R.id.employment_list);
        slidingBtn = (Button) view.findViewById(R.id.handle);
        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.slide_drawer);
        upIcon = getContext().getResources().getDrawable(R.drawable.up_icon_custom);
        downIcon = getContext().getResources().getDrawable(R.drawable.down_icon_custom);

        //체크박스 초기화
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = (CheckBox) view.findViewById(checkID[i]);
        }

        //어댑터 생성 후 리스트뷰에 어댑터 달기
        adapter = new EmploymentListAdapter();
        listView.setAdapter(adapter);

        adapter.addItem("LG전자", "~5/15(토)", "[IT/개발]", "솔루션 개발", "서울 강남구");

        lastItemFlag = false;

        final Animation moveDown = AnimationUtils.loadAnimation(getContext(), R.anim.move_down);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemFlag) {
                    //스크롤이 바닥에 닿았을 때 처리
                    slidingDrawer.startAnimation(moveDown);
                    slidingDrawer.setVisibility(View.INVISIBLE);
                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    slidingDrawer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemFlag = ((totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount));
            }
        });

        //전체선택 클릭시
        checkBoxes[13].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < checkBoxes.length - 1; i++) {
                    checkBoxes[i].setChecked(isChecked);
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getJobData();
                }
            }).start();
            Toast.makeText(getContext(), "네트워크 연결", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }

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
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

                if (netInfo != null && netInfo.isConnected()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getJobData();

                        }
                    }).start();
                    Toast.makeText(getContext(), "네트워크 연결", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {

        }

        return true;
    }

    public void getJobData() {
        adapter.addItem("LG전자", "~5/15(토)", "[IT/개발]", "솔루션 개발", "서울 강남구");
        adapter.addItem("LG전자", "~5/15(토)", "[IT/개발]", "솔루션 개발", "서울 강남구");

        String queryUrl = "http://api.saramin.co.kr/job-search?job_category=101&fields=expiration-date&count=20";
        String companyName = "", endDate = "", location = "", jobCategory = "", jobDetail = "";

        try {
            URL url = new URL(queryUrl); //문자열로 된 url 객체 생성
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //파싱 시작
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("job")) ;
                        else if (tag.equals("expiration-date")) {
                            endDate = xpp.getText();
                            xpp.next();
                        } else if (tag.equals("name")) {
                            companyName = "회사이름 " + xpp.getText();
                            xpp.next();
                        } else if (tag.equals("title")) {
                            jobDetail = xpp.getText();
                            xpp.next();
                        } else if (tag.equals("location")) {
                            location = xpp.getText();
                            xpp.next();
                        } else if (tag.equals("industry")) {
                            jobCategory = xpp.getText();
                            xpp.next();
                        }

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("job")) {
                            adapter.addItem(companyName, endDate, jobCategory, jobDetail, location);
            Log.i("배열 요소 추가", companyName + endDate + jobCategory + jobDetail + location);
        }
        break;
    }
    eventType = xpp.next();
}
        } catch (Exception e) {
            Log.e("파싱 에러", e.toString());
        }
    }

}
