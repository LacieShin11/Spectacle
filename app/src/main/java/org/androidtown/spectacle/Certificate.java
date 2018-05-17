package org.androidtown.spectacle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * certificate 카테고리
 */

public class Certificate extends AppCompatActivity {
    private ListView listView;
    private ListViewAdapter adapter;

    //데이터 값은 임시로 넣어두었음
    private String[] category = {"a","b", "c", "d"};
    private String[] projectName = {"aaa", "bbb", "ccc","ddd"};
    private String[] date = {"123","456","789","101"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate);

        //변수 초기화
        adapter = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.listView_certificate);

        //어뎁터 할당
        listView.setAdapter(adapter);

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
