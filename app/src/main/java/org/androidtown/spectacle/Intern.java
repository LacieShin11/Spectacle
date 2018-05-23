package org.androidtown.spectacle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Intern 카테고리
 */

public class Intern extends AppCompatActivity {
    private ListView listView;
    private ListViewAdapter adapter;
    private DbOpenHelper mDbOpenHelper;
    private static final String selectFolder = "인턴";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intern);

        //변수 초기화
        adapter = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.listView_intern);

        //어뎁터 할당
        listView.setAdapter(adapter);

        //DB Create and Open
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        String[] category = mDbOpenHelper.getListViewcategory(selectFolder);
        String[] projectName = mDbOpenHelper.getListViewTitle(selectFolder);
        String[] date = mDbOpenHelper.getListViewDate(selectFolder);


        //adapter를 통한 값 전달
        for(int i = 0; i < category.length; i++) {
            adapter.addVO(category[i], projectName[i], date[i]);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //리스트뷰 클릭 이벤트
            }
        });
    }

}
