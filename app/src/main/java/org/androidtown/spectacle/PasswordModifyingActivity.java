package org.androidtown.spectacle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by USER on 2018-05-28.
 */

public class PasswordModifyingActivity extends AppCompatActivity {
    EditText inputPassword;

    Button passwordBtn0, passwordBtn1, passwordBtn2, passwordBtn3, passwordBtn4, passwordBtn5,
            passwordBtn6, passwordBtn7, passwordBtn8, passwordBtn9, cancleBtn, backspaceBtn;

    Button[] buttonList = new Button[]{passwordBtn0, passwordBtn1, passwordBtn2, passwordBtn3, passwordBtn4,
            passwordBtn5, passwordBtn6, passwordBtn6, passwordBtn7, passwordBtn8, passwordBtn9, cancleBtn, backspaceBtn};

    int[] numButton = {R.id.password0_btn, R.id.password1_btn, R.id.password2_btn, R.id.password3_btn,
            R.id.password4_btn, R.id.password5_btn, R.id.password6_btn, R.id.password7_btn, R.id.password8_btn,
            R.id.password9_btn};

    private DbOpenHelper mDbOpenHelper;

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setContentView(R.layout.activity_password_modifying);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // DB create and open
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        inputPassword = (EditText) findViewById(R.id.input_password);
        cancleBtn = (Button) findViewById(R.id.password_cancle_btn);
        backspaceBtn = (Button) findViewById(R.id.password_backspace_btn);

        // 숫자 버튼 xml 연결 처리. 전역변수로 해줄 시 에러 발생.
        for (int i = 0; i < 10; i++) {
            buttonList[i] = (Button) findViewById(numButton[i]);
        }

        // 숫자 버튼 이벤트 처리
        for (int i = 0; i < 10; i++) {
            final int index;
            index = i;
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inputPassword.getText() == null)
                        inputPassword.setText(index);
                    else
                        inputPassword.append(Integer.toString(index));

                    if (inputPassword.getText().length() == 4) {
                        String password = inputPassword.getText().toString();
                        String[] prepasswordDB = mDbOpenHelper.getPassword();
                        Log.i("바꿀 비밀번호 : ", password);
                        Log.i("기존 비밀번호 : ", prepasswordDB[0]);
                        if (password.equals(prepasswordDB[0])) {
                            inputPassword.setText("");
                            Toast.makeText(PasswordModifyingActivity.this, "기존 비밀번호와 같습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            mDbOpenHelper.updateColumnPass(password);
                            String[] passwordDB = mDbOpenHelper.getPassword();
                            Toast.makeText(PasswordModifyingActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            Log.i("비밀번호 : ", passwordDB[0]);
                        }
                    }
                }
            });
        }

        // 취소 버튼 이벤트 처리
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // backspace 버튼 이벤트 처리
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPassword.getText() != null) {
                    String temp = inputPassword.getText().toString();
                    int length = temp.length();
                    if (length > 0) {
                        // 숫자 하나만 지우기
                        inputPassword.setText(temp.substring(0, length - 1));
                        //inputPassword.setSelection(inputPassword.getText().length());
                    }
                }
            }
        });

        // 비밀번호 입력 editText의 커서를 항상 끝에 위치시키기. 작동하지 않음. 수정 필요.
        //inputPassword.setSelection(inputPassword.getText().length());
    }
}
