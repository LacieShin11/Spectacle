package org.androidtown.spectacle;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDataActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 1;
    private Spinner categorySpinner;
    private CheckBox sameCheck;
    private Button startDateBtn, endDateBtn;
    private DatePicker datePicker;
    private ImageButton plusImgBtn, eraseBtn, copyBtn;
    private EditText titleText, contentText;
    private int startY, startM, startD, endY, endM, endD;

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setContentView(R.layout.activity_add_data);
        setTitle("새로운 항목추가");

        //뒤로가기 아이콘 추가
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        sameCheck = (CheckBox) findViewById(R.id.same_date_check);
        startDateBtn = (Button) findViewById(R.id.start_date_btn);
        endDateBtn = (Button) findViewById(R.id.end_date_btn);
        datePicker = new DatePicker(AddDataActivity.this);
        titleText = (EditText) findViewById(R.id.activity_title_edit);
        contentText = (EditText) findViewById(R.id.activity_content_edit);
        eraseBtn = (ImageButton) findViewById(R.id.eraser_img_btn);
        plusImgBtn = (ImageButton) findViewById(R.id.add_img_btn);
        copyBtn = (ImageButton) findViewById(R.id.copy_img_btn);

        Calendar cal = Calendar.getInstance();

        startY = endY = cal.get(cal.YEAR);
        startM = endM = cal.get(cal.MONTH) + 1;
        startD = endD = cal.get(cal.DATE);

        final String startDate = startY + "-" + startM + "-" + startD;
        final String endDate = endY + "-" + endM + "-" + endD;
        startDateBtn.setText(startDate);
        endDateBtn.setText(endDate);

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

                        //시작 날짜가 종료 날짜보다 늦을 때
                        if (startY > endY || (startY >= endY && startM > endM) || (startY >= endY && startM >= endM && startD > endD)) {
                            endDateBtn.setText(startY + "-" + startM + "-" + startD);
                            endY = startY;
                            endM = startM;
                            endD = startD;
                            Log.i("시작일자가 더 늦음", "시작일 : " + startY + "-" + startM + "-" + startD + " 종료일 : " + endY + "-" + endM + "-" + endD);
                            Toast.makeText(AddDataActivity.this, "시작일자가 종료일자보다 늦습니다.", Toast.LENGTH_SHORT).show();
                        }
                        startDateBtn.setText(startY + "-" + startM + "-" + startD);
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

                        //종료날짜가 시작 날짜를 앞설때
                        if (endY < startY || (endY <= startY && endM < startM) || (endY <= startY && endM <= startM && endD < startD)) {
                            startDateBtn.setText(endY + "-" + endM + "-" + endD);
                            startY = endY;
                            startM = endM;
                            startD = endD;
                            Log.i("종료일자가 더 빠름", "시작일 : " + startY + "-" + startM + "-" + startD + " 종료일 : " + endY + "-" + endM + "-" + endD);
                            Toast.makeText(AddDataActivity.this, "종료일자가 시작일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                        }
                        endDateBtn.setText(endY + "-" + endM + "-" + endD);
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

        final ClipboardManager clipboardManager =  (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentText.getText().length() == 0)
                    Toast.makeText(AddDataActivity.this, "복사할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                else
                clipboardManager.setText(contentText.getText());
            }
        });

        plusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImgBtnOnClicked();
            }
        });
        //카테고리 선택 이벤트
        /*categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addImgBtnOnClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }
}
