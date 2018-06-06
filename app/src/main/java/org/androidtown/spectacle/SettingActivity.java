package org.androidtown.spectacle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"비밀번호 설정", "비밀번호 변경", "앱 정보 및 사용설명서"};
    private DbOpenHelper mDbOpenHelper;
    public final String PREFERENCE = "org.androidtown.spectacle";

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setContentView(R.layout.activity_setting);

        // DB create and open
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        //스위치 상태 저장하기 위해 preference 사용
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        Boolean result = pref.getBoolean("key", false);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView settingList = (ListView) findViewById(R.id.setting_list);
        settingList.setAdapter(adapter);

        //뒤로가기 아이콘 추가
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // 암호 사용 유무를 설정하는 스위치 선언
        final Switch isLock = (Switch) findViewById(R.id.app_lock_switch);

        isLock.setChecked(result);

        isLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // preference 사용법
                editor.putBoolean("key", isChecked);
                // 스위치 상태 commit
                editor.commit();
                if (isChecked) {
                    // On
                    if (mDbOpenHelper.getPassword().length == 0) {
                        isLock.setChecked(false);
                        Toast.makeText(SettingActivity.this, "먼저 비밀번호를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Off
                }
            }
        });

        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String strText = (String) parent.getItemAtPosition(position);

                // 비밀번호 설정 클릭했을 때
                if (strText == "비밀번호 설정") {
                    String[] passwordDB = mDbOpenHelper.getPassword();
                    if (passwordDB.length == 0) {
                        Intent intent = new Intent(SettingActivity.this, PasswordSettingActivity.class);
                        startActivityForResult(intent, 0);
                    } else {
                        Toast.makeText(SettingActivity.this, "이미 설정되었습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (strText == "비밀번호 변경") { // 비밀번호 변경 클릭했을 때
                    if (mDbOpenHelper.getPassword().length == 0) {
                        Toast.makeText(SettingActivity.this, "먼저 비밀번호를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(SettingActivity.this, PasswordModifyingActivity.class);
                        startActivityForResult(intent, 0);
                    }
                } else if (strText == "앱 정보 및 사용설명서") { // 어플 정보 클릭했을 때
                    Intent intent = new Intent(SettingActivity.this, AppInformationActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }
}
