package org.androidtown.spectacle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

//채용정보 리스트 어댑터
public class EmploymentListAdapter extends BaseAdapter{
    ArrayList<EmploymentInfoItem> infoArray = new ArrayList<>();

    public EmploymentListAdapter() {
    }

    @Override
    public int getCount() {
        return infoArray.size();
    }

    @Override
    public Object getItem(int position) {
        return infoArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
