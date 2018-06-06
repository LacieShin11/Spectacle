package org.androidtown.spectacle;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class DetailContentActivity extends AppCompatActivity {
    TextView activityTitleDisplay, categoryDisplay, startDateDisplay, activityContentDisplay, noneImgFileText, imgFileNameText;
    ImageView selectImg;
    private DbOpenHelper mDbOpenHelper;
    String inputTitle, inputContent, inputCategory, inputStartDate, inputEndDate, imgPath;
    LinearLayout layout;
    int contentID;
    String sd = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);

        activityTitleDisplay = (TextView) findViewById(R.id.activity_title_display);
        categoryDisplay = (TextView) findViewById(R.id.category_display);
        startDateDisplay = (TextView) findViewById(R.id.start_date_display);
        activityContentDisplay = (TextView) findViewById(R.id.activity_content_display);
        selectImg = (ImageView) findViewById(R.id.select_img);
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

        //이미지가 있을 때와 없을 때 뷰를 바꿈
        if (imgFile.exists() && !(imgPath.equals(sd + "/Spectacle/image") || imgPath.equals(sd + "/Spectacle/image/null"))) {
            Log.i("이미지 존재", imgPath);
            layout.setVisibility(View.VISIBLE);
            noneImgFileText.setVisibility(View.INVISIBLE);

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            selectImg.setImageBitmap(myBitmap);
        } else {
            Log.i("이미지 존재X", imgPath);
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
    }

    public void downloadBtnOnClicked(View view) {
        try {
            String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
            String galleryPath = sd + "/" + Environment.DIRECTORY_PICTURES;

            File imgFolder = new File(galleryPath + "/Spectacle");
            File saveFile = new File(imgFolder + "/" + imgName);

            if (!imgFolder.isDirectory()) { //디렉토리가 만들어지지 않았을 경우 새로 생성
                imgFolder.mkdirs();
            }

            if (!imgPath.isEmpty()) {
                File from = new File(imgPath); //기존 파일
                saveFile.createNewFile(); //복사할 파일명 가져와서 빈 파일 생성
                copyFile(from, saveFile.toString());
            }

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(saveFile));
            sendBroadcast(intent);

            Toast.makeText(DetailContentActivity.this, "이미지 저장 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            imgPath = selectedRow[5];

            activityTitleDisplay.setText(selectedTitle);
            categoryDisplay.setText(selectedCategory);

            //수정된 이미지로 교체
            File imgFile = new File(imgPath);

            if (imgPath.equals(sd + "/Spectacle/image") || imgPath.equals(sd + "/Spectacle/image/null")) { //이미지가 없을 때
                Log.i("이미지 존재하지 않음", imgPath);
                layout.setVisibility(View.INVISIBLE);
                noneImgFileText.setVisibility(View.VISIBLE);
            } else { //이미지가 있을 때
                Log.i("이미지 존재함", imgPath);
                layout.setVisibility(View.VISIBLE);
                noneImgFileText.setVisibility(View.INVISIBLE);

                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    selectImg.setImageBitmap(bitmap);
                    imgFileNameText.setText(imgPath.substring(imgPath.lastIndexOf("/") + 1));

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

    //원본 파일, 덮어씌울 파일 받아서 복사하는 함수
    private void copyFile(File from, String to) throws Exception {

        if (from != null && from.exists()) {
            try {
                FileInputStream fis = new FileInputStream(from);
                FileOutputStream newfos = new FileOutputStream(to);
                int readcount = 0;
                byte[] buffer = new byte[1024];

                while ((readcount = fis.read(buffer, 0, 1024)) != -1) {
                    newfos.write(buffer, 0, readcount);
                }

                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
