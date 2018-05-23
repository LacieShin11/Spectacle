package org.androidtown.spectacle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by honglab403 on 2018-05-14.
 */

public class dlistListViewItemAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<dlistListViewItem> dlistListViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public dlistListViewItemAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return dlistListViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "dlist_item" layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dlist_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView categoryTextView = (TextView) convertView.findViewById(R.id.category_dlist);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_dlist);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date_dlist);

        // Data Set(dlistListViewItemList)에서 position에 위치한 데이터 참조 획득
        dlistListViewItem dlistListViewItem = dlistListViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        categoryTextView.setText(dlistListViewItem.getCategoryDList());
        titleTextView.setText(dlistListViewItem.getTitleDList());
        dateTextView.setText(dlistListViewItem.getDateDList());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return dlistListViewItemList.get(position);
    }

    // 아이템 데이터(리스트 값) 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String  category, String title, String date) {
        dlistListViewItem item = new dlistListViewItem();

        item.setCategoryDList(category);
        item.setTitleDList(title);
        item.setDateDList(date);

        dlistListViewItemList.add(item);
    }
}
