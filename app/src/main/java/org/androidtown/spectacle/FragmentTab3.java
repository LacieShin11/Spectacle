package org.androidtown.spectacle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class FragmentTab3 extends Fragment  implements OnChartValueSelectedListener {
    private int[] COLOR;
    private DbOpenHelper mDbOpenHelper;
    private String[] xTitle = {"교내활동", "대외활동", "인턴&아르바이트", "봉사활동", "어학", "자격증"};

    public static FragmentTab3 newInstance() {
        FragmentTab3 fragment = new FragmentTab3();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true); //메뉴 버튼 활성화

        int charColor1 = getResources().getColor(R.color.chartColor1);
        int charColor2 = getResources().getColor(R.color.chartColor2);
        int charColor3 = getResources().getColor(R.color.chartColor3);
        int charColor4 = getResources().getColor(R.color.chartColor4);
        int charColor5 = getResources().getColor(R.color.chartColor5);
        int charColor6 = getResources().getColor(R.color.chartColor6);
        COLOR = new int[] {charColor1, charColor2, charColor3, charColor4, charColor5, charColor6 };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        //DB Create and Open
        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();
        PieChart pieChart = (PieChart) view.findViewById(R.id.piechart);

        //y 값의 퍼센트 표시
        pieChart.setUsePercentValues(true);

        //Y 값
        int[] values = mDbOpenHelper.getSpecCount();
        //data가 없는 경우
        int index;
        for(index = 0; index < values.length; index++) {
            if(values[index] != 0 ) break;
        }
        if(index == values.length) {
            Toast.makeText(getContext(), "차트를 만들 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        };
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0; i < values.length; i++) {
            if(values[i] != 0 ) {
                yvalues.add(new Entry(values[i], i));
                xVals.add(xTitle[i]);
            }
        }

        PieDataSet dataSet = new PieDataSet(yvalues, "스펙 카테고리");

        PieData data = new PieData(xVals, dataSet);
        //default value
        data.setValueFormatter(new DefaultValueFormatter(0));
        //파이 차트 글씨
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setData(data);

        pieChart.setDescription("카테고리 별 활동 비율");
        //파이 차트 색상
        dataSet.setColors(COLOR);
        pieChart.getLegend().setEnabled(false);
        //Legend 값 표시
        TextView tLegend = (TextView) view.findViewById(R.id.chart_legend1);
        tLegend.setText(pieChart.getXValue(0));
        tLegend = (TextView) view.findViewById(R.id.chart_legend2);
        tLegend.setText(pieChart.getXValue(1));
        tLegend = (TextView) view.findViewById(R.id.chart_legend3);
        tLegend.setText(pieChart.getXValue(2));
        tLegend = (TextView) view.findViewById(R.id.chart_legend4);
        tLegend.setText(pieChart.getXValue(3));
        tLegend = (TextView) view.findViewById(R.id.chart_legend5);
        tLegend.setText(pieChart.getXValue(4));
        tLegend = (TextView) view.findViewById(R.id.chart_legend6);
        tLegend.setText(pieChart.getXValue(5));


        //Disable Hole in the Pie Chart
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(25f);

        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);

        mDbOpenHelper.close();

        return view;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
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

        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
  }

}
