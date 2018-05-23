package org.androidtown.spectacle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import org.achartengine.GraphicalView;
//import org.achartengine.model.CategorySeries;
//import org.achartengine.renderer.DefaultRenderer;


public class FragmentTab3 extends Fragment {
    //각 차트의 값
    private int[] pieChartValues;
    //각 차트의 색상
    private static int[] COLOR = new int[] {Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.CYAN };
    //각 차트 이름
    private String[] pieChartTitle = new String[] {"교내활동", "대외활동", "인턴", "봉사활동", "자격증", "어학"};

//    private CategorySeries mSeries = new CategorySeries("계열");
//    private DefaultRenderer mRenderer = new DefaultRenderer();
//    private GraphicalView mChartView;

    public static FragmentTab3 newInstance() {
        FragmentTab3 fragment = new FragmentTab3();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true); //메뉴 버튼 활성화

    }
//
//
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);

//        mRenderer.setApplyBackgroundColor(true);
//        mRenderer.setBackgroundColor(Color.WHITE);
//        mRenderer.setChartTitleTextSize(20);
//        mRenderer.setLabelsTextSize(30);
//        mRenderer.setLegendTextSize(30);
//        mRenderer.setMargins(new int[]{20, 30, 15, 0});
//        mRenderer.setZoomButtonsVisible(true);
//        mRenderer.setStartAngle(90);
//
//        if(mChartView == null ) {
//            LinearLayout layout = (LinearLayout) getView().findViewById(R.id.pie_chart);
//
//            mChartView = ChartFactory.getPieChartView(getContext(), mSeries, mRenderer);
//
//            mRenderer.setClickEnabled(true);
//
//            mRenderer.setSelectableBuffer(10);
//
//            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
//
//                    LinearLayout.LayoutParams.FILL_PARENT));
//        } else {
//            mChartView.repaint();
//        }
//
//        fillPieChart();
//
        return view;
    }
//
//    public void fillPieChart() {
//        for (int i = 0; i < pieChartValues.length; i++) {
//
//            mSeries.add(pieChartTitle[i] + "_" + (String.valueOf(pieChartValues[i])), pieChartValues[i]);
//
//
//            //Chart에서 사용할 값, 색깔, 텍스트등을 DefaultRenderer객체에 설정
//
//            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
//
//            renderer.setColor(COLOR[(mSeries.getItemCount() - 1) % COLOR.length]);
//
//
//            mRenderer.addSeriesRenderer(renderer);
//
//
//            if (mChartView != null)
//
//                mChartView.repaint();
//
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_main, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.login) {
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.make_excel) {
//
//        }
//
//        return true;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//  }

}
