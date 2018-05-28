package org.androidtown.spectacle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class DetailContentActivity extends AppCompatActivity {
    TextView activityTitleDisplay, categoryDisplay, startDateDisplay, endDateDisplay, activityContentDisplay;
    private DbOpenHelper mDbOpenHelper;
    String inputTitle, inputContent, inputCategory, inputStartDate, inputEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);

        activityTitleDisplay = (TextView) findViewById(R.id.activity_title_display);
        categoryDisplay = (TextView) findViewById(R.id.category_display);
        startDateDisplay = (TextView) findViewById(R.id.start_date_display);
        endDateDisplay = (TextView) findViewById(R.id.end_date_display);
        activityContentDisplay = (TextView) findViewById(R.id.activity_content_display);

        Intent intent = getIntent();
        inputTitle = intent.getStringExtra("title");
        inputContent = intent.getStringExtra("content");
        inputCategory = intent.getStringExtra("cate");
        inputStartDate = intent.getStringExtra("startDate");
        inputEndDate = intent.getStringExtra("endDate");

        activityTitleDisplay.setText(inputTitle);
        categoryDisplay.setText(inputCategory);
        startDateDisplay.setText(inputStartDate);
        endDateDisplay.setText(inputEndDate);
        activityContentDisplay.setText(inputContent);

    }
}
