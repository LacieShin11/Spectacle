package org.androidtown.spectacle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;


public class DetailContentActivity extends AppCompatActivity {
    TextView activityTitleDisplay, categoryDisplay, startDateDisplay, activityContentDisplay, noneImgFileText, imgFileNameText;
    ImageView selectImg;
    private DbOpenHelper mDbOpenHelper;
    String inputTitle, inputContent, inputCategory, inputStartDate, inputEndDate, imgPath;
    ImageButton downloadBtn;
    LinearLayout layout;
    int contentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);*/

        activityTitleDisplay = (TextView) findViewById(R.id.activity_title_display);
        categoryDisplay = (TextView) findViewById(R.id.category_display);
        startDateDisplay = (TextView) findViewById(R.id.start_date_display);
        activityContentDisplay = (TextView) findViewById(R.id.activity_content_display);
        selectImg = (ImageView) findViewById(R.id.select_img);
        downloadBtn = (ImageButton) findViewById(R.id.download_img);
        noneImgFileText = (TextView) findViewById(R.id.none_img_text);
        layout = (LinearLayout) findViewById(R.id.image_layout);
        imgFileNameText = (TextView) findViewById(R.id.img_name_text);

        Intent intent = getIntent();
        inputTitle = intent.getStringExtra("title");
        inputContent = intent.getStringExtra("content");
        inputCategory = intent.getStringExtra("cate");
        inputStartDate = intent.getStringExtra("startDate");
        inputEndDate = intent.getStringExtra("endDate");
        imgPath = intent.getStringExtra("image");

        activityTitleDisplay = (TextView) findViewById(R.id.activity_title_display);
        categoryDisplay = (TextView) findViewById(R.id.category_display);
        startDateDisplay = (TextView) findViewById(R.id.start_date_display);
        activityContentDisplay = (TextView) findViewById(R.id.activity_content_display);

        contentID = intent.getIntExtra("contentID", 0);


        mDbOpenHelper = new DbOpenHelper(DetailContentActivity.this);
        mDbOpenHelper.open();

        int index = imgPath.indexOf("Spectacle/image/");

        imgFileNameText.setText(imgPath.substring(index + 1));

        Log.i("이미지 경로", imgPath);

        activityTitleDisplay.setText(inputTitle);
        categoryDisplay.setText(inputCategory);

        //날짜가 같을 때와 다를 때 다른 문자열 입력
        if (inputStartDate.equals(inputEndDate))
            startDateDisplay.setText(inputStartDate);
        else
            startDateDisplay.setText(inputStartDate + " ~ " + inputEndDate);

        File imgFile = new File(imgPath);

        //이미지가 있을 때와 없을 때 뷰를 바꿈
        if (imgFile.exists() && !(imgPath.equals("/storage/emulated/0/Spectacle/image/null"))) {
            layout.setVisibility(View.VISIBLE);
            noneImgFileText.setVisibility(View.INVISIBLE);

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            selectImg.setImageBitmap(myBitmap);
        } else {
            layout.setVisibility(View.INVISIBLE);
            noneImgFileText.setVisibility(View.VISIBLE);
        }

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

        //다운로드 버튼 클릭시 이벤트
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName;
                String savePath = Environment.getExternalStorageDirectory().toString() + "/Spectacle";

                File dir = new File(savePath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                String[] selectedRow = mDbOpenHelper.getSelectedRow(contentID);

                final String selectedTitle = selectedRow[0];
                final String selectedCategory = selectedRow[1];
                final String selectedStartDate = selectedRow[2];
                final String selectedEndDate = selectedRow[3];
                final String selectedContent = selectedRow[4];

                Intent intent = new Intent(DetailContentActivity.this, ModifyActivity.class);
                intent.putExtra("title", selectedTitle);
                intent.putExtra("cate", selectedCategory);
                intent.putExtra("startDate", selectedStartDate);
                intent.putExtra("endDate", selectedEndDate);
                intent.putExtra("content", selectedContent);
                intent.putExtra("contentID", contentID);

                startActivityForResult(intent, 1);
                break;

            case R.id.delete:
                finish();
                break;
        }
        return true;
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
}
