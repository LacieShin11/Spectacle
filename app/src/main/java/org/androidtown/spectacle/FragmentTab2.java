package org.androidtown.spectacle;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class FragmentTab2 extends Fragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    View myView;
    ImageButton campusActivitiesBtn, internationalActivitiesBtn, internBtn, certificateBtn, volunteerBtn, languageBtn;
    DbOpenHelper mDbOpenHelper;

    public static FragmentTab2 newInstance() {
        FragmentTab2 fragment = new FragmentTab2();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true); //메뉴 버튼 활성화

        mDbOpenHelper = new DbOpenHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_tab2, container, false);

        campusActivitiesBtn = (ImageButton) myView.findViewById(R.id.campus_activities_btn);
        internationalActivitiesBtn = (ImageButton) myView.findViewById(R.id.international_activities_btn);
        internBtn = (ImageButton) myView.findViewById(R.id.intern_btn);
        certificateBtn = (ImageButton) myView.findViewById(R.id.certificate_btn);
        volunteerBtn = (ImageButton) myView.findViewById(R.id.volunteer_activities_btn);
        languageBtn = (ImageButton) myView.findViewById(R.id.language_btn);

        campusActivitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CampusActivities.class);
                startActivity(intent);


            }
        });

        internationalActivitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InternationalActivities.class);
                startActivity(intent);

            }
        });

        internBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Intern.class);
                startActivity(intent);

            }
        });

        certificateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Certificate.class);
                startActivity(intent);

            }
        });
        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VolunteerActivities.class);
                startActivity(intent);
            }
        });

        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Language.class);
                startActivity(intent);
            }
        });

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //파일을 생성할 것이므로 READ 가 아니라 WRITE로
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_EXTERNAL_STORAGE);
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
                        Toast.makeText(getActivity(), "성공", Toast.LENGTH_LONG).show();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                    }
                }
            }
        }//다 WRITE로! 파일 생성할 거니까
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {
            mDbOpenHelper.open();
            Cursor cursor = mDbOpenHelper.getTable(); //db전체 내용 가져옴

//*****나중에 알림창 띄우는 기능 추가할 수 있으면 할 것*********
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                    getContext().getApplicationContext());
//
//            alertDialogBuilder
//                    .setTitle("엑셀파일 생성 권한")
//                    .setMessage("[엑셀 내보내기] 기능을 사용하시려면 내부 파일, 미디어 접근 권한을 [허용]해주셔야 합니다. " +
//                            "'다시 묻지 않기' 후에 [거부]를 누르시면 [엑셀 내보내기] 기능을 사용하실 수 없으니 이 점 참고해 주시길 바랍니다.")
//                    .setCancelable(false)
//                    .setPositiveButton("확인했습니다",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog, int id) {
//                                    // 프로그램을 종료한다
//                                }
//                            });

            askForPermission();

            //우선 엑셀 파일 저장할 디렉토리랑 파일 생성
            File sd = Environment.getExternalStorageDirectory();
            File directory = new File(sd.getAbsolutePath() + "/Spectacle");
            String csvFile = "mySpec.xls";//xlsx로 하면 파일 확장자 오류? 내부파일 망가졌다고? 오류나므로 xls로 확장자할 것
            String path = directory.toString();// /storage/emulated/0/Spectacle *0은 루트 의미하는 듯

            File file = new File(path); //해당 경로 밑에 xsl파일 생성
            if (!file.isDirectory()) {
                file.mkdirs();
            }

            try {
                File file_2 = new File(directory, csvFile);
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook;

                workbook = Workbook.createWorkbook(file_2, wbSettings);

                //WorkbookSetting class의 역할: 엑셀 시트 만들어주는데 필요한 클래스.
                WritableSheet sheet = workbook.createSheet("MySPECtacle", 0); //엑셀파일 생성 및 이름 설정
                sheet.addCell(new Label(0, 0, "번호"));
                sheet.addCell(new Label(1, 0, "카테고리"));
                sheet.addCell(new Label(2, 0, "활동명"));
                sheet.addCell(new Label(3, 0, "활동내용"));
                sheet.addCell(new Label(4, 0, "시작날짜"));
                sheet.addCell(new Label(5, 0, "종료날짜"));
                //차례로 column번호, row번호, row이름 정해주기

                if (cursor.moveToFirst()) {
                    String[] ids = mDbOpenHelper.getID(); //contentId 정보 가져옴
                    String[] category = mDbOpenHelper.getCategory(); //category 정보 가져옴
                    String[] activityname = mDbOpenHelper.getTitle(); //activityname 값들 가져옴
                    String[] activitycontent = mDbOpenHelper.getContent(); //activtyContent값들 가져옴
                    String[] startdate = mDbOpenHelper.getDate1(); //Date 데이터 가져옴
                    String[] enddate = mDbOpenHelper.getDate2(); //Date 데이터 가져옴

                    for (int i = 0; i < ids.length; i++) {
                        sheet.addCell(new Label(0, i + 1, ids[i]));
                        sheet.addCell(new Label(1, i + 1, category[i]));
                        sheet.addCell(new Label(2, i + 1, activityname[i]));
                        sheet.addCell(new Label(3, i + 1, activitycontent[i]));
                        sheet.addCell(new Label(4, i + 1, startdate[i]));
                        sheet.addCell(new Label(5, i + 1, enddate[i]));
                    }//데이터베이스 내용 가져오기
                }

                cursor.close();
                workbook.write();
                workbook.close();
                Toast.makeText(getActivity(), "다음 경로에 파일이 생성되었습니다." + '\n' + "(" + sd.getAbsolutePath().toString() +
                        "/Spectacle/mySpec.xsl)", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}