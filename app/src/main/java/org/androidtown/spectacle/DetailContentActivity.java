package org.androidtown.spectacle;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

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

        imgFileNameText.setText(imgPath.substring(imgPath.lastIndexOf("/") + 1)); //이미지 경로 잘라서 이름 설정

        activityTitleDisplay.setText(inputTitle);
        categoryDisplay.setText(inputCategory);

        //날짜가 같을 때와 다를 때 다른 문자열 입력
        if (inputStartDate.equals(inputEndDate))
            startDateDisplay.setText(inputStartDate);
        else
            startDateDisplay.setText(inputStartDate + " ~ " + inputEndDate);

        File imgFile = new File(imgPath);

        /*String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        File imgFile = new File(selectedImgPath);

        if (selectedImgPath.equals(sd + "/Spectacle/image") || selectedImgPath.equals(sd + "/Spectacle/image/null")
            */

        //이미지가 있을 때와 없을 때 뷰를 바꿈
        //이부분 수정 필요
        if (imgFile.exists() && !(imgPath.substring(imgPath.lastIndexOf("/") + 1).equals("null"))) {
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
                intent.putExtra("image", imgPath);

                startActivityForResult(intent, 1);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String[] selectedRow = mDbOpenHelper.getSelectedRow(contentID);

            final String selectedTitle = selectedRow[0];
            final String selectedCategory = selectedRow[1];
            final String selectedStartDate = selectedRow[2];
            final String selectedEndDate = selectedRow[3];
            final String selectedContent = selectedRow[4];
            final String selectedImgPath = selectedRow[5];

            activityTitleDisplay.setText(selectedTitle);
            categoryDisplay.setText(selectedCategory);

            //수정된 이미지로 교체
            String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
            File imgFile = new File(selectedImgPath);

            if (selectedImgPath.equals(sd + "/Spectacle/image") || selectedImgPath.equals(sd + "/Spectacle/image/null")) { //이미지가 없을 때
                Log.i("이미지 존재하지 않음", selectedImgPath + "||" + sd);
                layout.setVisibility(View.INVISIBLE);
                findViewById(R.id.none_img_text).setVisibility(View.VISIBLE);
            }

            else { //이미지가 있을 때
                Log.i("이미지 존재함", selectedImgPath + "||" + sd);
                layout.setVisibility(View.VISIBLE);
                findViewById(R.id.none_img_text).setVisibility(View.INVISIBLE);

                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    selectImg.setImageBitmap(bitmap);
                    imgFileNameText.setText(selectedImgPath.substring(selectedImgPath.lastIndexOf("/") + 1));

                } else {
                    Toast.makeText(this, "이미지 파일이 손상되었습니다.\n" + sd + "/Spectacle/image\" 경로에서 파일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

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
