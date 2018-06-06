package org.drawcoding.spectacle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by USER on 2018-05-27.
 */

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

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
                        Log.i("입력한 비밀번호는 : ", password);
                        String[] passwordDB = mDbOpenHelper.getPassword();
                        if (password.equals(passwordDB[0])) {
                            finish();
                            Log.i("비밀번호 : ", passwordDB[0]);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                            inputPassword.setText("");
                        }
                    }
                }
            });
        }

        // 취소 버튼 이벤트 처리
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        // backspace 버튼 이벤트 처리
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPassword.getText() != null) {
                    String temp = inputPassword.getText().toString();
                    int length = temp.length();
                    if (length > 0)
                        // 숫자 하나만 지우기
                        inputPassword.setText(temp.substring(0, length - 1));
                }
            }
        });

        // 비밀번호 입력 editText의 커서를 항상 끝에 위치시키기. 작동하지 않음. 수정 필요.
        inputPassword.setSelection(inputPassword.getText().length());
    }

    // 자체 키패드를 만들었으므로 키보드 항상 내려가있게 하기
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        //.setTitle("종료")
                        .setMessage("앱을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("아니오", null).show();
                return false;
            default :
                return true;
        }
        //return super.onKeyDown(keyCode, event);
    }
}
