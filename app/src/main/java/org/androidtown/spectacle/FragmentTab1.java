package org.androidtown.spectacle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class FragmentTab1 extends Fragment {

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

        //DB 생성 및 열기
        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();

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
    }

    //플로팅 액션 버튼 이벤트
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        yearText = view.findViewById(R.id.year_text);
        leftArrow = view.findViewById(R.id.left_arrow);
        rightArrow = view.findViewById(R.id.right_arrow);

        year = Calendar.getInstance().get(Calendar.YEAR);
        yearText.setText(year + "년");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                startActivity(intent);
            }
        });

        listView = (ExpandableListView) view.findViewById(R.id.date_list);
        listView.setGroupIndicator(null);

        ListByDateAdapter adapter = new ListByDateAdapter();
        adapter.headerList = groupList;
        adapter.childList = childList;
        listView.setAdapter(adapter);

        //listview 아이템 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //좌우 화살표 클릭 이벤트
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year--;
                yearText.setText(year + "년");
                setListItem();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year++;
                yearText.setText(year + "년");
                setListItem();
            }
        });

        setListItem();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {

        } else if (id == R.id.initialization) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("모든 내역을 초기화 하시겠습니까? 초기화 된 내용은 복구가 불가능합니다.").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDbOpenHelper.deleteTable();
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.detach(getTargetFragment()).attach(getTargetFragment()).commit();
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

    public void setListItem() {

        String[] categoryArray = mDbOpenHelper.getCategory();
        String[] titleArray = mDbOpenHelper.getTitle();
        String[] dateArray = mDbOpenHelper.getDate();

        for (int i = 0; i < dateArray.length; i++) {
            Log.i("category 값 확인: ", categoryArray[i]);
            Log.i("title 값 확인: ", titleArray[i]);
            Log.i("Date 값 확인: ", dateArray[i]);
        }

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
                    month1.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("2"))
                    month2.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("3"))
                    month3.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("4"))
                    month4.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("5"))
                    month5.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("6"))
                    month6.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("7"))
                    month7.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("8"))
                    month8.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("9"))
                    month9.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("10"))
                    month10.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("11"))
                    month11.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));

                else if (date[1].equals("12"))
                    month12.add(new ListByDateItem(categoryArray[i], titleArray[i], dateArray[i]));
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

