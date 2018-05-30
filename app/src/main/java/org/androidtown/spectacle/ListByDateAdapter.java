package org.androidtown.spectacle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListByDateAdapter extends BaseExpandableListAdapter {
    ArrayList<ListByDateHeaderItem> headerList;
    LayoutInflater inflater;
    ArrayList<ArrayList<ListByDateItem>> childList;

    //부모리스트 크기 반환
    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public ListByDateHeaderItem getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public ListByDateItem getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        Context context = parent.getContext();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.header_item, parent, false);
        }

        ImageView arrowIcon = (ImageView) v.findViewById(R.id.expand_arrow);
        TextView month = (TextView) v.findViewById(R.id.header_text);
        TextView count = (TextView) v.findViewById(R.id.spec_count);

        //그룹 펼쳐짐 여부에 따라 아이콘 변경
        if (isExpanded)
            arrowIcon.setImageResource(R.drawable.ic_expand_more_24dp);
        else
            arrowIcon.setImageResource(R.drawable.ic_expand_less_24dp);

        month.setText(getGroup(groupPosition).month);
        count.setText(getGroup(groupPosition).count);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        Context context = parent.getContext();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.child_item, parent, false);
        }

        TextView categoryName = (TextView) v.findViewById(R.id.category);
        TextView specName = (TextView) v.findViewById(R.id.spec_name);
        TextView startDate = (TextView) v.findViewById(R.id.start_date);
        ImageView categoryIcon = (ImageView) v.findViewById(R.id.category_icon);

        categoryName.setText(getChild(groupPosition, childPosition).categoryName);
        specName.setText(getChild(groupPosition, childPosition).specName);
        startDate.setText(getChild(groupPosition, childPosition).date);

        switch (categoryName.getText().toString()) {
            case "어학":
                categoryIcon.setImageResource(R.drawable.ic_translate_24dp);
                break;
            case "자격증":
                categoryIcon.setImageResource(R.drawable.ic_qualification_24dp);
                break;
            case "인턴&알바":
                categoryIcon.setImageResource(R.drawable.ic_company_24dp);
                break;
            case "봉사활동":
                categoryIcon.setImageResource(R.drawable.ic_favorite_24dp);
                break;
            case "교내활동":
                categoryIcon.setImageResource(R.drawable.ic_school_24dp);
                break;
            case "대외활동":
                categoryIcon.setImageResource(R.drawable.ic_card_travel_24dp);
                break;
        }
        return v;
    }

    public void removeChild(int groupPosition, int childPosition)
    {
        childList.get(groupPosition).remove(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
