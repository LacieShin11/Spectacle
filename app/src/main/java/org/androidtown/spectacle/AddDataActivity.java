package org.androidtown.spectacle;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class
AddDataActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 1;
    private Spinner categorySpinner;
    private CheckBox sameCheck;
    private Button startDateBtn, endDateBtn;
    private DatePicker datePicker;
    private ImageButton plusImgBtn, eraseBtn, copyBtn;
    private EditText titleText, contentText;
    private int startY, startM, startD, endY, endM, endD;
    private DbOpenHelper mDbOpenHelper;
    String startDateStr, endDateStr;

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setContentView(R.layout.activity_add_data);
        setTitle("새로운 항목추가");

        //뒤로가기 아이콘 추가
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //DB Create and Open
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

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

        plusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImgBtnOnClicked();
            }
        });

//        if(isChecked) this.finish();
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
            Log.i("확인버튼 선택됨", "확인버튼 눌러짐");
            String category = categorySpinner.getSelectedItem().toString();
            String title = titleText.getText().toString();
            String content = contentText.getText().toString();
            String startDate = startDateBtn.getText().toString();
            String endDate = endDateBtn.getText().toString();

            if (category.equals("(선택없음)") || title.equals("")) {
                if (title.equals(""))
                    Toast.makeText(getApplicationContext(), "활동명을 작성해주세요.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "분류할 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("삽입될 내용", category + " / " + title + " / " + content + " / " + startDate + " / " + endDate + " / ");
                mDbOpenHelper.insertColumn(category, title, content, startDate, endDate, "");
                mDbOpenHelper.displayColumn();

                Intent intent = new Intent(getApplicationContext(), FragmentTab1.class);
                intent.putExtra("ok", "");
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addImgBtnOnClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private Uri imgUri;
    private String currentImgPath; //실제 이미지 파일 경로
    private String mImageCaptureName; //이미지 이름

    //갤러리에서 이미지 선택한 후의 동작 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM) {
        }
    }

    //DbOpenHelper close
    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

}
