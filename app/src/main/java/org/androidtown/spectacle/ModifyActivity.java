package org.androidtown.spectacle;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by NKJ on 2018-05-30.
 */

public class ModifyActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Spinner categorySpinner;
    private CheckBox sameCheck;
    private Button startDateBtn, endDateBtn;
    private DatePicker datePicker;
    LinearLayout layout;
    private ImageButton eraseBtn, copyBtn, deleteBtn;
    private EditText titleText, contentText;
    private int startY, startM, startD, endY, endM, endD;
    private DbOpenHelper mDbOpenHelper;
    private ImageView selectImg;
    private TextView noneImgFileText, imgNameText;
    String startDateStr, endDateStr, imgPath, imgName;
    String inputTitle, inputContent, inputCategory, inputStartDate, inputEndDate, deleteImgPath;
    int categoryIndex, contentID;
    File to, imgFile;
    int imgCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modification);

        datePicker = new DatePicker(ModifyActivity.this);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        sameCheck = (CheckBox) findViewById(R.id.same_date_check);
        startDateBtn = (Button) findViewById(R.id.start_date_btn);
        endDateBtn = (Button) findViewById(R.id.end_date_btn);
        titleText = (EditText) findViewById(R.id.activity_title_edit);
        contentText = (EditText) findViewById(R.id.activity_content_edit);
        eraseBtn = (ImageButton) findViewById(R.id.eraser_img_btn);
        deleteBtn = (ImageButton) findViewById(R.id.close_img);
        noneImgFileText = (TextView) findViewById(R.id.none_img_text);
        imgNameText = (TextView) findViewById(R.id.img_name_text);
        copyBtn = (ImageButton) findViewById(R.id.copy_img_btn);
        selectImg = (ImageView) findViewById(R.id.select_img);
        layout = findViewById(R.id.image_layout);

        Intent intent = getIntent();
        inputTitle = intent.getStringExtra("title");
        inputContent = intent.getStringExtra("content");
        inputCategory = intent.getStringExtra("cate");
        inputStartDate = intent.getStringExtra("startDate");
        inputEndDate = intent.getStringExtra("endDate");
        contentID = intent.getIntExtra("contentID", 0);
        imgPath = intent.getStringExtra("image");

        deleteImgPath = imgPath;

        //이미지가 있을 때와 없을 때 뷰를 바꿈
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        imgFile = new File(imgPath);

        if (imgFile.exists() && !(imgPath.equals(sd + "/Spectacle/image") || imgPath.equals(sd + "/Spectacle/image/null"))) {
            layout.setVisibility(View.VISIBLE);
            noneImgFileText.setVisibility(View.INVISIBLE);

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()); //해당 이미지 가져옴
            selectImg.setImageBitmap(myBitmap);
            imgNameText.setText(imgPath.substring(imgPath.lastIndexOf("/") + 1));
            imgCount = 1;

        } else {
            layout.setVisibility(View.INVISIBLE);
            noneImgFileText.setVisibility(View.VISIBLE);
            imgCount = 0;
        }

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        if (inputCategory.equals("어학"))
            categoryIndex = 1;
        else if (inputCategory.equals("자격증"))
            categoryIndex = 2;
        else if (inputCategory.equals("인턴·알바"))
            categoryIndex = 3;
        else if (inputCategory.equals("봉사활동"))
            categoryIndex = 4;
        else if (inputCategory.equals("교내활동"))
            categoryIndex = 5;
        else if (inputCategory.equals("대외활동"))
            categoryIndex = 6;

        titleText.setText(inputTitle);
        contentText.setText(inputContent);
        categorySpinner.setSelection(categoryIndex);
        startDateBtn.setText(inputStartDate);
        endDateBtn.setText(inputEndDate);
        startDateStr = inputStartDate;
        endDateStr = inputEndDate;


        startDateStr = inputStartDate;
        endDateStr = inputEndDate;

        String[] endDate = inputEndDate.split("-");

        endY = Integer.parseInt(endDate[0]);
        endM = Integer.parseInt(endDate[1]);
        endD = Integer.parseInt(endDate[2]);

        sameCheck.setChecked(false);
        if (inputStartDate.equals(inputEndDate)) {
            sameCheck.setChecked(true);
            endDateBtn.setEnabled(false);
        }

        sameCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sameCheck.isChecked()) {
                    endDateBtn.setEnabled(false);
                    endDateBtn.setText(startDateBtn.getText());
                } else {
                    endDateBtn.setEnabled(true);
                }
            }
        });

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
                if (datePicker.getParent() != null)
                    ((ViewGroup) datePicker.getParent()).removeView(datePicker);
                builder.setView(datePicker);

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startY = datePicker.getYear();
                        startM = datePicker.getMonth() + 1;
                        startD = datePicker.getDayOfMonth();
                        startDateStr = startY + "-" + startM + "-" + startD;

                        //시작 날짜가 종료 날짜보다 늦을 때
                        if (sameCheck.isChecked() || startY > endY || (startY >= endY && startM > endM) || (startY >= endY && startM >= endM && startD > endD)) {
                            endDateBtn.setText(startDateStr);
                            endDateStr = startDateStr;
                            endY = startY;
                            endM = startM;
                            endD = startD;

                            if (!sameCheck.isChecked())
                                Toast.makeText(ModifyActivity.this, "시작일자가 종료일자보다 늦습니다.", Toast.LENGTH_SHORT).show();
                        }
                        startDateBtn.setText(startDateStr);

                        if (startDateStr.equals(endDateStr))
                            sameCheck.setChecked(true);
                    }
                });
                builder.show();
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
                if (datePicker.getParent() != null)
                    ((ViewGroup) datePicker.getParent()).removeView(datePicker);
                builder.setView(datePicker);


                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //취소
                    }
                });

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endY = datePicker.getYear();
                        endM = datePicker.getMonth() + 1;
                        endD = datePicker.getDayOfMonth();
                        endDateStr = endY + "-" + endM + "-" + endD;

                        //종료날짜가 시작 날짜를 앞설때
                        if (endY < startY || (endY <= startY && endM < startM) || (endY <= startY && endM <= startM && endD < startD)) {
                            startDateBtn.setText(endDateStr);
                            startDateStr = endDateStr;
                            startY = endY;
                            startM = endM;
                            startD = endD;

                            Toast.makeText(ModifyActivity.this, "종료일자가 시작일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                        }

                        endDateBtn.setText(endDateStr);

                        if (startDateStr.equals(endDateStr))
                            sameCheck.setChecked(true);
                    }
                });

                builder.show();
            }
        });

        //지우개 아이콘 클릭시 내용 삭제
        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentText.setText("");
            }
        });

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentText.getText().length() == 0)
                    Toast.makeText(ModifyActivity.this, "복사할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                else
                    clipboardManager.setText(contentText.getText());
            }
        });

        //이미지 삭제 버튼 클릭 이벤트
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView();

                imgCount = 0;
                imgName = "";
                imgPath = "";
            }
        });
    }

    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //파일을 생성할 것이므로 READ 가 아니라 WRITE로
        ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "성공", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_data, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.check) {
            String category = categorySpinner.getSelectedItem().toString();
            String title = titleText.getText().toString();
            String content = contentText.getText().toString();
            String startDate = startDateBtn.getText().toString();
            String endDate = endDateBtn.getText().toString();

            try {
                File sd = Environment.getExternalStorageDirectory();
                File directory = new File(sd.getAbsolutePath() + "/Spectacle/image");
                String path = directory.toString();

                File file = new File(path);

                if (!file.isDirectory()) { //디렉토리가 만들어지지 않았을 경우 새로 생성
                    file.mkdirs();
                }

                to = new File(file + "/" + imgName);

                if (!imgPath.isEmpty() && !imgName.isEmpty()) {
                    File from = new File(imgPath); //기존 파일

                    to.createNewFile(); //복사할 파일명 가져와서 빈 파일 생성

                    copyFile(from, to.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (category.equals("(선택없음)") || title.equals("")) {
                if (title.equals(""))
                    Toast.makeText(getApplicationContext(), "활동명을 작성해주세요.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "분류할 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                mDbOpenHelper.updateColumn(contentID, category, title, content, startDate, endDate, imgPath);
                mDbOpenHelper.displayColumn();

                Intent intent = new Intent(ModifyActivity.this, DetailContentActivity.class);
                intent.putExtra("ok", "");
                setResult(RESULT_OK, intent);
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addImgBtnOnClicked(View view) {
        askForPermission();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //갤러리에서 이미지 선택한 후의 동작 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            imgName = getImageNameToUri(data.getData());

            if (requestCode == PICK_FROM_ALBUM) {
                selectImg.setImageBitmap(bitmap);
                imgNameText.setText(imgName);
                imgCount++;

                if (imgCount == 1) //이미지가 없는 상태였을 때만 레이아웃 변경
                    changeView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    //원본 파일, 덮어씌울 파일 받아서 복사하는 함수
    private void copyFile(File from, String to) throws Exception {

        if (from != null && from.exists()) {
            try {
                FileInputStream fis = new FileInputStream(from);
                FileOutputStream newfos = new FileOutputStream(to);
                int readcount;
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

    public void changeView() {
        LinearLayout layout = findViewById(R.id.image_layout);
        TextView textView = findViewById(R.id.none_img_text);

        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        // mDbOpenHelper.close();
        super.onDestroy();
    }

}

