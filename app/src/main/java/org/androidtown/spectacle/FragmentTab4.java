package org.androidtown.spectacle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;


public class FragmentTab4 extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private Activity activity;
    private ListView listView;
    private Context mContext;
    private boolean lastItemFlag;
    private EmploymentListAdapter adapter;
    private SlidingDrawer slidingDrawer;
    private Button slidingBtn;
    private Drawable upIcon, downIcon;
    private CheckBox[] checkBoxes = new CheckBox[13];
    private boolean[] isCheckedArray = new boolean[13];
    private Integer[] checkID = {R.id.check1, R.id.check2, R.id.check3, R.id.check4, R.id.check5, R.id.check6,
            R.id.check7, R.id.check8, R.id.check9, R.id.check10, R.id.check11, R.id.check12, R.id.check13};
    private DbOpenHelper mDbOpenHelper;

    public static FragmentTab4 newInstance() {
        FragmentTab4 fragment = new FragmentTab4();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        mContext = context;
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
            checkBoxes[i].setOnCheckedChangeListener(this);
        }

        //어댑터 생성 후 리스트뷰에 어댑터 달기
        adapter = new EmploymentListAdapter();
        listView.setAdapter(adapter);

        lastItemFlag = false;

        final Animation moveDown = AnimationUtils.loadAnimation(getContext(), R.anim.move_down);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemFlag) {
                    //스크롤이 바닥에 닿았을 때 숨김 처리
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

        showList();

        //리스트 클릭시 해당 링크로 연결
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(adapter.getItem(position).link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected())
                Toast.makeText(getActivity(), "새로고침 완료.", Toast.LENGTH_SHORT).show();
            showList();

        } else if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {

        }
        return true;
    }

    public void showList() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        adapter.removeAll();

        if (netInfo != null && netInfo.isConnected()) { //인터넷 연결 가능
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String urlStr = "https://api.saramin.co.kr/job-search?fields=expiration-date&count=50&job_category=";

                    int first = 0;
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (first == 0 && checkBoxes[i].isChecked()) {
                            urlStr += "" + (i + 1);
                            first++;
                        } else if (checkBoxes[i].isChecked())
                            urlStr += "," + (i + 1);
                    }


                    getJobData(urlStr);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();

        } else {
            Toast.makeText(getContext(), "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getJobData(String urlStr) {
        String queryUrl = urlStr;
        String companyName = "", endDate = "", location = "", jobCategory = "", jobDetail = "", link = "", level = "";

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

                        if (tag.equals("expiration-date")) {
                            xpp.next();
                            endDate = "~" + xpp.getText().substring(0, 10);
                        } else if (tag.equals("name")) {
                            xpp.next();
                            companyName = (xpp.getText());
                        } else if (tag.equals("title")) {
                            xpp.next();
                            jobDetail = xpp.getText();
                        } else if (tag.equals("experience-level")) {
                            xpp.next();
                            level = xpp.getText();
                        } else if (tag.equals("location")) {
                            xpp.next();
                            location = xpp.getText().substring(0, 2) + " / ";
                        } else if (tag.equals("industry")) {
                            xpp.next();
                            jobCategory = xpp.getText();
                        } else if (tag.equals("url")) {
                            xpp.next();
                            link = xpp.getText();
                        }


                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("job")) {
                            adapter.addItem(companyName, endDate, jobCategory, jobDetail, location, link, level);
                            break;
                        }
                }
                eventType = xpp.next();
            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        TextView textView = getView().findViewById(R.id.none_text);
        ListView listView = getView().findViewById(R.id.employment_list);
        int count = checkBoxes.length;

        boolean noneCheck = false;

        for (int i = 0; i < count; i++) {
            if (!checkBoxes[i].isChecked())
                noneCheck = true;
            else {
                noneCheck = false;
                break;
            }
        }

        if (noneCheck) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }

        showList();

    }
}
