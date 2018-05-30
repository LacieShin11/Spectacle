package org.androidtown.spectacle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailContentActivity extends AppCompatActivity {
    TextView activityTitleDisplay, categoryDisplay, startDateDisplay, activityContentDisplay;
    private DbOpenHelper mDbOpenHelper;
    String inputTitle, inputContent, inputCategory, inputStartDate, inputEndDate;
    int contentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);

        View myButtonLayout = getLayoutInflater().inflate(R.layout.button, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(myButtonLayout);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        Button modificationBtn = (Button) findViewById(R.id.modification);

        activityTitleDisplay = (TextView) findViewById(R.id.activity_title_display);
        categoryDisplay = (TextView) findViewById(R.id.category_display);
        startDateDisplay = (TextView) findViewById(R.id.start_date_display);
        activityContentDisplay = (TextView) findViewById(R.id.activity_content_display);

        Intent intent = getIntent();
        contentID = intent.getIntExtra("contentID", 0);

        Log.i("DetailContent : ", "contentID : " + contentID);

        mDbOpenHelper = new DbOpenHelper(DetailContentActivity.this);
        mDbOpenHelper.open();
        String[] selectedRow = mDbOpenHelper.getSelectedRow(contentID);

        final String selectedTitle = selectedRow[0];
        final String selectedCategory = selectedRow[1];
        final String selectedStartDate = selectedRow[2];
        final String selectedEndDate = selectedRow[3];
        final String selectedContent = selectedRow[4];

        activityTitleDisplay.setText(selectedTitle);
        categoryDisplay.setText(selectedCategory);

        //날짜가 같을 때와 다를 때 다른 문자열 입력
        if (selectedStartDate.equals(selectedEndDate))
            startDateDisplay.setText(selectedStartDate);
        else
            startDateDisplay.setText(selectedStartDate + " ~ " + selectedEndDate);

        //본문이 있을 때와 없을 때 다른 문자열 입력
        if (selectedContent.equals("")) {
            activityContentDisplay.setText("입력된 내용이 없습니다");
            activityContentDisplay.setTextColor(Color.parseColor("#BDBDBD"));
            activityContentDisplay.setGravity(Gravity.CENTER);
        } else {
            activityContentDisplay.setText(selectedContent);
            activityContentDisplay.setTextColor(Color.parseColor("#000000"));
            activityContentDisplay.setGravity(Gravity.LEFT);
        }

        modificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailContentActivity.this, ModifyActivity.class);
                intent.putExtra("title", selectedTitle);
                intent.putExtra("cate", selectedCategory);
                intent.putExtra("startDate", selectedStartDate);
                intent.putExtra("endDate", selectedEndDate);
                intent.putExtra("content", selectedContent);
                intent.putExtra("contentID", contentID);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            String[] selectedRow = mDbOpenHelper.getSelectedRow(contentID);

            final String selectedTitle = selectedRow[0];
            final String selectedCategory = selectedRow[1];
            final String selectedStartDate = selectedRow[2];
            final String selectedEndDate = selectedRow[3];
            final String selectedContent = selectedRow[4];

            activityTitleDisplay.setText(selectedTitle);
            categoryDisplay.setText(selectedCategory);

            //날짜가 같을 때와 다를 때 다른 문자열 입력
            if (selectedStartDate.equals(selectedEndDate))
                startDateDisplay.setText(selectedStartDate);
            else
                startDateDisplay.setText(selectedStartDate + " ~ " + selectedEndDate);

            //본문이 있을 때와 없을 때 다른 문자열 입력
            if (selectedContent.equals("")) {
                activityContentDisplay.setText("입력된 내용이 없습니다");
                activityContentDisplay.setTextColor(Color.parseColor("#BDBDBD"));
                activityContentDisplay.setGravity(Gravity.CENTER);
            } else {
                activityContentDisplay.setText(selectedContent);
                activityContentDisplay.setTextColor(Color.parseColor("#000000"));
                activityContentDisplay.setGravity(Gravity.LEFT);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
