package org.androidtown.spectacle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * volunteer 카테고리
 */

public class VolunteerActivities extends AppCompatActivity {
    private ListView listView;
    private ListViewAdapter adapter;
    private DbOpenHelper mDbOpenHelper;
    private static final String selectFolder = "봉사";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_activities);

        //뒤로가기 아이콘 추가
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //변수 초기화
        adapter = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.listView_volunteer);

        //어뎁터 할당
        listView.setAdapter(adapter);

        //DB Create and Open
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        String[] category = mDbOpenHelper.getListViewcategory(selectFolder);
        String[] projectName = mDbOpenHelper.getListViewTitle(selectFolder);
        String[] date = mDbOpenHelper.getListViewDate(selectFolder);
        int[] contentID = mDbOpenHelper.getListViewID(selectFolder);

        //adapter를 통한 값 전달
        for(int i = 0; i < category.length; i++) {
            adapter.addVO(category[i], projectName[i], date[i], contentID[i]);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //리스트뷰 클릭 이벤트

                Log.i("position", "position : " + position);

                ListVO selectedList = (ListVO) adapter.getItem(position);
                String title = selectedList.getProjectName();
                int content_id = selectedList.getContentID();

                Log.i("title", "title : " + title);
                Log.i("id", "id : " + content_id);

                String[] selectedRow = mDbOpenHelper.getSelectedRow(content_id);

                String selectedTitle = selectedRow[0];
                String selectedCategory = selectedRow[1];
                String selectedStartDate = selectedRow[2];
                String selectedEndDate = selectedRow[3];
                String selectedContent = selectedRow[4];
                String selectedImgPath = selectedRow[5];

                Intent intent = new Intent(getApplicationContext(), DetailContentActivity.class);
                intent.putExtra("title", selectedTitle);
                intent.putExtra("cate", selectedCategory);
                intent.putExtra("startDate", selectedStartDate);
                intent.putExtra("endDate", selectedEndDate);
                intent.putExtra("content", selectedContent);
                intent.putExtra("contentID", content_id);
                intent.putExtra("image", selectedImgPath);

                startActivityForResult(intent, 15);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("longClick", "onLongClick: true");
                Log.i("position", "position : " + position);

                final int index = position;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VolunteerActivities.this);
                alertDialogBuilder.setTitle("삭제");
                alertDialogBuilder.setMessage("선택한 항목을 삭제하시겠습니까?\n삭제된 내역은 복구가 불가능합니다.");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListVO selectedList = (ListVO) adapter.getItem(index);;
                        int content_id = selectedList.getContentID();
                        mDbOpenHelper.delete(content_id);
                        adapter.removeChild(index);
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.create();

                alertDialogBuilder.show();

                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume: ", "onResume: start");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 15) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("onActivityResult: ", "onActivityResult: start");
                int contentID = data.getIntExtra("contentID", 0);
                String cate = data.getStringExtra("cate");
                String title = data.getStringExtra("title");
                String startDate = data.getStringExtra("startDate");
                String currentCate = data.getStringExtra("current_cate");
                adapter.updateChild(contentID, cate, title, startDate, currentCate);
                adapter.notifyDataSetChanged();
            }
        }
    }

}

