package org.androidtown.spectacle;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class
AddDataActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Spinner categorySpinner;
    private CheckBox sameCheck;
    private Button startDateBtn, endDateBtn;
    private DatePicker datePicker;
    private ImageButton plusImgBtn, eraseBtn, copyBtn, deleteBtn;
    private EditText titleText, contentText;
    private int startY, startM, startD, endY, endM, endD;
    private ImageView selectImg;
    private DbOpenHelper mDbOpenHelper;
    private TextView noneImgText, imgNameText;
    String startDateStr, endDateStr, imgPath = "", imgName;
    File to;
    int imgCount = 0;

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setContentView(R.layout.activity_add_data);
        setTitle("새로운 항목추가");

        //뒤로가기 아이콘 추가
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //DB 생성 및 열기
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        sameCheck = (CheckBox) findViewById(R.id.same_date_check);
        startDateBtn = (Button) findViewById(R.id.start_date_btn);
        endDateBtn = (Button) findViewById(R.id.end_date_btn);
        titleText = (EditText) findViewById(R.id.activity_title_edit);
        contentText = (EditText) findViewById(R.id.activity_content_edit);
        eraseBtn = (ImageButton) findViewById(R.id.eraser_img_btn);
        plusImgBtn = (ImageButton) findViewById(R.id.add_img_btn);
        copyBtn = (ImageButton) findViewById(R.id.copy_img_btn);
        selectImg = (ImageView) findViewById(R.id.select_img);
        deleteBtn = (ImageButton) findViewById(R.id.close_img);
        noneImgText = (TextView) findViewById(R.id.none_img_text);
        imgNameText = (TextView) findViewById(R.id.img_name_text);
        datePicker = new DatePicker(AddDataActivity.this);

        Calendar cal = Calendar.getInstance();

        sameCheck.setChecked(true);
        endDateBtn.setEnabled(false);

        startY = endY = cal.get(cal.YEAR);
        startM = endM = cal.get(cal.MONTH) + 1;
        startD = endD = cal.get(cal.DATE);

        startDateStr = startY + "-" + startM + "-" + startD;
        endDateStr = endY + "-" + endM + "-" + endD;
        startDateBtn.setText(startDateStr);
        endDateBtn.setText(endDateStr);

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
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
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
                                Toast.makeText(AddDataActivity.this, "시작일자가 종료일자보다 늦습니다.", Toast.LENGTH_SHORT).show();
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
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

                            Toast.makeText(AddDataActivity.this, "종료일자가 시작일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddDataActivity.this, "복사할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
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

                File from = new File(imgPath); //기존 파일
                to = new File(file + "/" + imgName);

                to.createNewFile(); //복사할 파일명 가져와서 빈 파일 생성

                copyFile(from, to.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (category.equals("(선택없음)") || title.equals("")) {
                if (title.equals(""))
                    Toast.makeText(getApplicationContext(), "활동명을 작성해주세요.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "분류할 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                mDbOpenHelper.insertColumn(category, title, content, startDate, endDate, to.toString());

                mDbOpenHelper.displayColumn();

                Intent intent = new Intent(AddDataActivity.this, FragmentTab1.class);
                intent.putExtra("ok", "");
                setResult(RESULT_OK, intent);
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //이미지 추가 클릭시
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

                if (imgCount == 1)
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

    //DbOpenHelper close
    @Override
    protected void onDestroy() {
        // mDbOpenHelper.close();
        super.onDestroy();
    }

}
