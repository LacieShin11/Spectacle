package org.androidtown.spectacle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 리스트뷰에 데이터값 넣어주기 위한 어뎁터
 */

public class ListViewAdapter extends BaseAdapter {
    private ListVO listViewItem;
    private ArrayList<ListVO> listVO = new ArrayList<ListVO>();

    public ListViewAdapter() {

    }
    @Override
    public int getCount() {
        return listVO.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // position = ListView 위치, 0부터 시작
        // final int pos = position;
        final Context context = parent.getContext();
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);
        }
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView projectName = (TextView) convertView.findViewById(R.id.project_name);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        listViewItem = listVO.get(position);

        //아이템 내 각 위젯에 데이터 반영
        category.setText(listViewItem.getCategory());
        projectName.setText(listViewItem.getProjectName());
        date.setText(listViewItem.getDate());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listVO.get(position);
    }

    public void removeChild(int position)
    {
        listVO.remove(position);
    }

    public void updateChild(int contentID, String cate, String title, String startDate)
    {
        for (int i = 0; i < listVO.size(); i++)
        {
            listViewItem = listVO.get(i);
            if (contentID == listViewItem.getContentID()) {
                listViewItem.setDate(startDate);
                listViewItem.setCategory(cate);
                listViewItem.setProjectName(title);
            }

        }
    }

    //데이터값 넣어줌
    public void addVO(String category, String projectName, String date, int contentID) {
        ListVO item = new ListVO();

        item.setCategory(category);
        item.setProjectName(projectName);
        item.setDate(date);
        item.setContentID(contentID);

        listVO.add(item);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

