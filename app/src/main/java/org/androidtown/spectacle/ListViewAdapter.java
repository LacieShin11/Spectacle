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
        //position = ListView 위치, 0부터 시작
        final int pos = position;
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



        /*//리스트뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 터치 시 자세하게 보이는 항목

                // 클릭한 항목의 pos의 아이템을 listViewItem에 넣기
                listViewItem = listVO.get(pos);
                Intent intent = new Intent(context, DetailContentActivity.class);

                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        listViewItem = listVO.get(position);*/


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



    //데이터값 넣어줌
    public void addVO(String category, String projectName, String date, int contentID) {
        ListVO item = new ListVO();

        item.setCategory(category);
        item.setProjectName(projectName);
        item.setDate(date);
        item.setContentID(contentID);

        listVO.add(item);
    }
}

