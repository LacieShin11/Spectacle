package org.androidtown.spectacle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


public class DetailContentActivity extends AppCompatActivity {
    TextView activityTitleDisplay, categoryDisplay, startDateDisplay, activityContentDisplay;
    private DbOpenHelper mDbOpenHelper;
    String inputTitle, inputContent, inputCategory, inputStartDate, inputEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        activityTitleDisplay = (TextView) findViewById(R.id.activity_title_display);
        categoryDisplay = (TextView) findViewById(R.id.category_display);
        startDateDisplay = (TextView) findViewById(R.id.start_date_display);
        activityContentDisplay = (TextView) findViewById(R.id.activity_content_display);

        Intent intent = getIntent();
        inputTitle = intent.getStringExtra("title");
        inputContent = intent.getStringExtra("content");
        inputCategory = intent.getStringExtra("cate");
        inputStartDate = intent.getStringExtra("startDate");
        inputEndDate = intent.getStringExtra("endDate");

        activityTitleDisplay.setText(inputTitle);
        categoryDisplay.setText(inputCategory);

        //날짜가 같을 때와 다를 때 다른 문자열 입력
        if (inputStartDate.equals(inputEndDate))
            startDateDisplay.setText(inputStartDate);
        else
            startDateDisplay.setText(inputStartDate + " ~ " + inputEndDate);

        //본문이 있을 때와 없을 때 다른 문자열 입력
        if (inputContent.equals("")) {
            activityContentDisplay.setText("입력된 내용이 없습니다.");
            activityContentDisplay.setTextColor(Color.parseColor("#BDBDBD"));
            activityContentDisplay.setGravity(Gravity.CENTER);
        } else {
            activityContentDisplay.setText(inputContent);
            activityContentDisplay.setTextColor(Color.parseColor("#000000"));
            activityContentDisplay.setGravity(Gravity.LEFT);
        }
    }
}
