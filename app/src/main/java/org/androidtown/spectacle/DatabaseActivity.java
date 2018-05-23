//package org.androidtown.spectacle;
//
//import android.app.Activity;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.w3c.dom.Text;
//
//public class DatabaseActivity extends Activity {
//
//    private static final String TAG = "TestDataBaseActivity";
//    private DbOpenHelper mDbOpenHelper;
//    private Cursor mCursor;
//    //private InfoClass mInfoClass;
//    //private ArrayList<infoclass> mInfoArray;
//    //private CustomAdapter mAdapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.main);
//
//        setLayout();
//
//        //DB Create and Open
//        mDbOpenHelper = new DbOpenHelper(this);
//        mDbOpenHelper.open();
//
//        mDbOpenHelper.insertColumn("a", "b", "c","d","e","f");
//
//        //startManagingCursor(mCursor);
//        mInfoArray = new ArrayList<infoClass>();
//
//        doWhileCursorToArray();
//
//        for(InfoClass i : mInfoArray) {
//
//        }
//        mAdapter = new CustomAdapter(this, mInfoArray);
//        mListView.setAdapter(mAdapter);
//        mListView.setOnItemLongClickListener(longClickListener);
//    }
//
//    @Override
//    protected void onDestroy() {
//        mDbOpenHelper.close();
//        super.onDestroy();
//    }
//
//    /**
//     * ListView의 Item을 롱클릭 할때 호출 ( 선택한 아이템의 DB 컬럼과 Data를 삭제 한다. )
//     */
//    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
//        @Override
//        public boolean onItemLongClick(AdapterView<!--?--> arg0, View arg1,
//                                       int position, long arg3) {
//
//            DLog.e(TAG, "position = " + position);
//
//            boolean result = mDbOpenHelper.deleteColumn(position + 1);
//            DLog.e(TAG, "result = " + result);
//
//            if(result){
//                mInfoArray.remove(position);
//                mAdapter.setArrayList(mInfoArray);
//                mAdapter.notifyDataSetChanged();
//            }else {
//                Toast.makeText(getApplicationContext(), "INDEX를 확인해 주세요.",
//                        Toast.LENGTH_LONG).show();
//            }
//
//            return false;
//        }
//    };
//
//
//    /**
//     * DB에서 받아온 값을 ArrayList에 Add
//     */
//    private void doWhileCursorToArray(){
//
//        mCursor = null;
//        mCursor = mDbOpenHelper.getAllColumns();
//        DLog.e(TAG, "COUNT = " + mCursor.getCount());
//
//        while (mCursor.moveToNext()) {
//
//            mInfoClass = new InfoClass(
//                    mCursor.getInt(mCursor.getColumnIndex("contentId")),
//                    mCursor.getString(mCursor.getColumnIndex("category")),
//                    mCursor.getString(mCursor.getColumnIndex("activityName")),
//                    mCursor.getString(mCursor.getColumnIndex("activityContent")),
//                    mCursor.getString(mCursor.getColumnIndex("startDate")),
//                    mCursor.getString(mCursor.getColumnIndex("endDate")),
//                    mCursor.getString(mCursor.getColumnIndex("image"))
//            );
//
//            mInfoArray.add(mInfoClass);
//        }
//
//        mCursor.close();
//    }
//
//
//    /**
//     * OnClick Button
//     * @param v
//     */
//    public void onClick(View v){
//        switch (v.getId()) {
//            case R.id.btn_add:
//                mDbOpenHelper.insertColumn
//                        (
//                                mEditTexts[Constants.NAME].getText().toString().trim(),
//                                mEditTexts[Constants.CONTACT].getText().toString().trim(),
//                                mEditTexts[Constants.EMAIL].getText().toString().trim()
//                        );
//
//                mInfoArray.clear();
//
//                doWhileCursorToArray();
//
//                mAdapter.setArrayList(mInfoArray);
//                mAdapter.notifyDataSetChanged();
//
//                mCursor.close();
//
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    /*
//     * Layout
//     */
//    private TextView[] mEditTexts;
//    private ListView mListView;
//
//    private void setLayout(){
//        mEditTexts = new TextView[]{
//                (TextView)findViewById(R.id.category),
//                (TextView)findViewById(R.id.project_name),
//                (TextView)findViewById(R.id.date)
//        };
//
//        mListView = (ListView) findViewById(R.id.listView_language);
//    }
//}
//
