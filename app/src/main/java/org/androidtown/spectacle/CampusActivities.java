package org.androidtown.spectacle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * campusActivities 카테고리
 */

public class CampusActivities extends Activity {
    private ListView listView;
    private dlistListViewItemAdapter adapter;

    //데이터 값은 임시로 넣어두었음
    private String[] categoryDList = {"a","b", "c", "d"};
    private String[] titleDList = {"aaa", "bbb", "ccc","ddd"};
    private String[] dateDList = {"123","456","789","101"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_activity);

        //변수 초기화
        adapter = new dlistListViewItemAdapter();
        listView = (ListView) findViewById(R.id.listView_campus);

        //어뎁터 할당
        listView.setAdapter(adapter);

        //adapter를 통한 값 전달
        for(int i = 0; i < categoryDList.length; i++) {
            adapter.addItem(categoryDList[i], titleDList[i], dateDList[i]);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //리스트뷰 클릭 이벤트
            }
        });
    }

}
