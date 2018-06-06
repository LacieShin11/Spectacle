package org.androidtown.spectacle;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class FragmentTab1 extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback { //접근권한 위해 추가함
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    int year;
    TextView yearText;
    ImageButton leftArrow, rightArrow;
    Context mContext;
    DbOpenHelper mDbOpenHelper;
    ExpandableListView listView;
    ListByDateAdapter adapter;
    ArrayList<ListByDateHeaderItem> groupList = new ArrayList<>();
    ArrayList<ArrayList<ListByDateItem>> childList = new ArrayList<>();
    ArrayList<ArrayList<ListByDateItem>> monthArray = new ArrayList<>();
    ArrayList<ListByDateItem> month1 = new ArrayList<>();
    ArrayList<ListByDateItem> month2 = new ArrayList<>();
    ArrayList<ListByDateItem> month3 = new ArrayList<>();
    ArrayList<ListByDateItem> month4 = new ArrayList<>();
    ArrayList<ListByDateItem> month5 = new ArrayList<>();
    ArrayList<ListByDateItem> month6 = new ArrayList<>();
    ArrayList<ListByDateItem> month7 = new ArrayList<>();
    ArrayList<ListByDateItem> month8 = new ArrayList<>();
    ArrayList<ListByDateItem> month9 = new ArrayList<>();
    ArrayList<ListByDateItem> month10 = new ArrayList<>();
    ArrayList<ListByDateItem> month11 = new ArrayList<>();
    ArrayList<ListByDateItem> month12 = new ArrayList<>();
    ListByDateItem selectedItem;

    public static FragmentTab1 newInstance() {
        FragmentTab1 fragment = new FragmentTab1();
        return fragment;
    }

    // getContext가 null을 반환하지 않도록 함
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true);

        monthArray.add(month1);
        monthArray.add(month2);
        monthArray.add(month3);
        monthArray.add(month4);
        monthArray.add(month5);
        monthArray.add(month6);
        monthArray.add(month7);
        monthArray.add(month8);
        monthArray.add(month9);
        monthArray.add(month10);
        monthArray.add(month11);
        monthArray.add(month12);
        //리스트 내용 설정

        //DB 생성 및 열기
        mDbOpenHelper = new DbOpenHelper(getContext());
        if (!mDbOpenHelper.getState()) {
            mDbOpenHelper.open();
            Log.i("DB 상태", mDbOpenHelper.getState() + " ");
        }

        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    //플로팅 액션 버튼 이벤트
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;

        if (mDbOpenHelper.isEmpty(mDbOpenHelper.getTable())) {
            View view = inflater.inflate(R.layout.list_empty_layout, container, false);
            FloatingActionButton fab = view.findViewById(R.id.fab);

            v = view;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddDataActivity.class);
                    startActivityForResult(intent, 0);
                }
            });
        } //db가 텅 비어있으면 빈 화면 보여주기

        else {
            View view = inflater.inflate(R.layout.fragment_tab1, container, false);
            FloatingActionButton fab = view.findViewById(R.id.fab);
            v = view;

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddDataActivity.class);
                    startActivityForResult(intent, 0);
                }
            });

            yearText = view.findViewById(R.id.year_text);
            leftArrow = view.findViewById(R.id.left_arrow);
            rightArrow = view.findViewById(R.id.right_arrow);

            yearText.setText(year + "년");

            listView = (ExpandableListView) view.findViewById(R.id.date_list);
            listView.setGroupIndicator(null);

            adapter = new ListByDateAdapter();
            adapter.headerList = groupList;
            adapter.childList = childList;
            listView.setAdapter(adapter);

            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Log.i("childPosition", "childPosition : " + childPosition);
                    selectedItem = (ListByDateItem) adapter.getChild(groupPosition, childPosition);

                    int content_id = selectedItem.getContentID();
                    String cate = selectedItem.getCategoryName();
                    Log.i("content_id", "content_id : " + content_id);
                    Log.i("cate", "cate : " + cate);

                    String[] selectedRow = mDbOpenHelper.getSelectedRow(content_id);

                    String selectedTitle = selectedRow[0];
                    String selectedCategory = selectedRow[1];
                    String selectedStartDate = selectedRow[2];
                    String selectedEndDate = selectedRow[3];
                    String selectedContent = selectedRow[4];
                    String selectedImgPath = selectedRow[5];

                    Intent intent = new Intent(getContext(), DetailContentActivity.class);
                    intent.putExtra("title", selectedTitle);
                    intent.putExtra("cate", selectedCategory);
                    intent.putExtra("startDate", selectedStartDate);
                    intent.putExtra("endDate", selectedEndDate);
                    intent.putExtra("content", selectedContent);
                    intent.putExtra("contentID", content_id);
                    intent.putExtra("image", selectedImgPath);

                    startActivity(intent);

                    return false;
                }

            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        final int childPosition = ExpandableListView.getPackedPositionChild(id);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("삭제");
                        alertDialogBuilder.setMessage("선택한 항목을 삭제하시겠습니까?\n삭제된 내역은 복구가 불가능합니다.");
                        alertDialogBuilder.setCancelable(false);

                        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selectedItem = (ListByDateItem) adapter.getChild(groupPosition, childPosition);
                                int content_id = selectedItem.getContentID();

                                mDbOpenHelper.delete(content_id);
                                adapter.removeChild(groupPosition, childPosition);

                                setListItem();
                                adapter.notifyDataSetChanged();

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(FragmentTab1.this).attach(FragmentTab1.this).commit();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        alertDialogBuilder.create();
                        alertDialogBuilder.show();

                        return true;
                    }

                    return false;
                }
            });

            //좌우 화살표 클릭 이벤트
            leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    year--;
                    yearText.setText(year + "년");
                    setListItem();
                    adapter.notifyDataSetChanged();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(FragmentTab1.this).attach(FragmentTab1.this).commit();
                }
            });

            rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    year++;
                    yearText.setText(year + "년");
                    setListItem();
                    adapter.notifyDataSetChanged();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(FragmentTab1.this).attach(FragmentTab1.this).commit();
                }
            });
        }

        setListItem();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //adapter.notifyDataSetChanged();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // adapter.notifyDataSetChanged();
}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestory 호출!" + mDbOpenHelper.getState());
        //mDbOpenHelper.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {//엑셀 파일로 만들기
            mDbOpenHelper.open();
            Cursor cursor = mDbOpenHelper.getTable(); //db전체 내용 가져옴
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

                sheet.addCell(new Label(0, 0, "카테고리"));
                sheet.addCell(new Label(1, 0, "활동명"));
                sheet.addCell(new Label(2, 0, "활동내용"));
                sheet.addCell(new Label(3, 0, "시작날짜"));
                sheet.addCell(new Label(4, 0, "종료날짜"));
                //차례로 column번호, row번호, row이름 정해주기

                if (cursor.moveToFirst()) {
                    String[] ids = mDbOpenHelper.getID(); //contentId 정보 가져옴
                    String[] category = mDbOpenHelper.getCategory(); //category 정보 가져옴
                    String[] activityname = mDbOpenHelper.getTitle(); //activityname 값들 가져옴
                    String[] activitycontent = mDbOpenHelper.getContent(); //activtyContent값들 가져옴
                    String[] startdate = mDbOpenHelper.getDate1(); //Date 데이터 가져옴
                    String[] enddate = mDbOpenHelper.getDate2(); //Date 데이터 가져옴

                    for (int i = 0; i < ids.length; i++) {
                        sheet.addCell(new Label(0, i + 1, category[i]));
                        sheet.addCell(new Label(1, i + 1, activityname[i]));
                        sheet.addCell(new Label(2, i + 1, activitycontent[i]));
                        sheet.addCell(new Label(3, i + 1, startdate[i]));
                        sheet.addCell(new Label(4, i + 1, enddate[i]));
                    }//데이터베이스 내용 가져오기
                }

                cursor.close();//커서 안 닫으면 저장 안 됨
                workbook.write();
                workbook.close();
                Toast.makeText(getActivity(), "다음 경로에 파일이 생성되었습니다." + '\n' + "(" + "내 파일/내장 메모리" +
                        "/Spectacle/mySpec.xsl)", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.initialization) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("모든 내역을 초기화 하시겠습니까? \n초기화 된 내용은 복구가 불가능합니다.").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //사진 폴더 삭제
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Spectacle/image");
                            File[] childFileList = file.listFiles();
                            for (File child : childFileList) {
                                if (!child.isDirectory())
                                    child.delete();
                            }

                            file.delete();

                            //DB 테이블 삭제
                            mDbOpenHelper.deleteTable();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(FragmentTab1.this).attach(FragmentTab1.this).commit();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }

        return true;
    }

    //**엑셀용 코드
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }//외부 저장소가 read,write 현재 가능한지 점검

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }//외부 저장소가 현재 read만이라도 할 수 있는지 점검

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
                //Toast.makeText(getActivity(), "성공", Toast.LENGTH_LONG).show();
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                    }
                }
            }
        }//다 WRITE로! 파일 생성할 거니까
// Here, thisActivity is the current activity
    }

    public void shouldShowRequestPermissionRationale(){

    }

    public void setListItem() {
        mDbOpenHelper.open();

        String[] categoryArray = mDbOpenHelper.getCategory();
        String[] titleArray = mDbOpenHelper.getTitle();
        String[] dateArray = mDbOpenHelper.getDate();
        int[] contentIDArray = mDbOpenHelper.getContentID();

        //리스트 내용 초기화
        groupList.clear();
        childList.clear();

        for (int i = 0; i < monthArray.size(); i++) {
            monthArray.get(i).clear();
        }

        for (int i = 0; i < categoryArray.length; i++) {
            String date[] = dateArray[i].split("-");

            //현재 년도에 해당하는 아이템일 경우에만
            if ((date[0] + "년").equals(yearText.getText())) {
                if (date[1].equals("1"))
                    month1.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("2"))
                    month2.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("3"))
                    month3.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("4"))
                    month4.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("5"))
                    month5.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("6"))
                    month6.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("7"))
                    month7.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("8"))
                    month8.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("9"))
                    month9.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("10"))
                    month10.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("11"))
                    month11.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));

                else if (date[1].equals("12"))
                    month12.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i], contentIDArray[i]));
            }
        }

        for (int i = 0; i < monthArray.size(); i++) {
            childList.add(monthArray.get(i));
        }

        //리스트 내용 추가
        for (int i = 1; i <= 12; i++) {
            groupList.add(new ListByDateHeaderItem(i + "월", childList.get(i - 1).size() + "건"));
        }
    }

}

