package org.androidtown.spectacle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//채용정보 리스트 어댑터
public class EmploymentListAdapter extends BaseAdapter {
    ArrayList<EmploymentInfoItem> infoArray = new ArrayList<>();

    static class ViewHolder {
        private TextView tv1, tv2, tv3, tv4, tv5;
        private int index;
        EmploymentInfoItem item;
    }

    @Override
    public int getCount() {
        return infoArray.size();
    }

    @Override
    public EmploymentInfoItem getItem(int position) {
        return infoArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context mContext = parent.getContext();

        //레이아웃 inflate로 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.employment_item, parent, false);
        }

        final TextView companyNameText = (TextView) convertView.findViewById(R.id.company_text);
        final TextView jobInfoText = (TextView) convertView.findViewById(R.id.job_info_text);
        final TextView locationText = (TextView) convertView.findViewById(R.id.area_text);
        final TextView experienceText = (TextView) convertView.findViewById(R.id.experience_text);
        final TextView jobCategoryText = (TextView) convertView.findViewById(R.id.job_type_text);
        final TextView endDateText = (TextView) convertView.findViewById(R.id.end_date_text);

        final EmploymentInfoItem item = infoArray.get(position);

        companyNameText.setText(item.companyName);
        jobInfoText.setText(item.jobInfo);
        locationText.setText(item.location);
        experienceText.setText(item.experience);
        jobCategoryText.setText(item.jobCategory);
        endDateText.setText(item.endDate);

        return convertView;
    }

    //새로운 요소 추가
    public void addItem(String companyName, String endDate, String jobCategory, String jobInfo, String location, String link, String experience) {
        infoArray.add(new EmploymentInfoItem(companyName, endDate, jobCategory, jobInfo, location, link, experience));
    }

    public void removeAll() {
        infoArray.clear();
    }
}
