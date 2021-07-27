package com.aosproject.dailyone.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aosproject.dailyone.SwipeDismissListViewTouchListener;
import com.aosproject.dailyone.adapter.ListAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.bean.Diary;
import com.aosproject.dailyone.util.DiaryHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListFragment extends Fragment {

    DiaryHelper helper;
    ListAdapter adapter;
    ArrayList<Diary> diaries = new ArrayList<Diary>();

    ListView listView;
    TextView tv_year, tv_month;

    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

    String year = yearFormat.format(currentTime);
    String month = monthFormat.format(currentTime);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_list, container,false);

        listView = view.findViewById(R.id.listView_lv_diaryList);
        tv_year = view.findViewById(R.id.listView_tv_year);
        tv_month = view.findViewById(R.id.listView_tv_month);

        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                    .setTitle("연도를 선택하세요.")
                    .setItems(R.array.year,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] years = getResources().getStringArray(R.array.year);
                                    tv_year = view.findViewById(R.id.listView_tv_year);
                                    tv_year.setText(years[which] + "년");
                                    year = years[which];
                                }
                            }
                        )
                    .setNegativeButton("취소", null)
                    .show();
            }
        });

        tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("월을 선택하세요.")
                        .setItems(R.array.month,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String[] months = getResources().getStringArray(R.array.month);
                                        tv_month = view.findViewById(R.id.listView_tv_month);
                                        tv_month.setText(months[which] + "월");
                                        month = months[which];
                                        onResume();
                                    }
                                }
                        )
                        .setNegativeButton("취소", null)
                        .show();
            }
        });



        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    String deleteResult = connectDeleteData(diaries.get(position).getId());
                                    diaries.remove(diaries.get(position));
                                    if(deleteResult.equals("1")){
                                        Toast.makeText(getContext(), "일기가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getContext(), "일기 삭제 실패했습니다.\n관리자에게 문의하세요", Toast.LENGTH_LONG).show();
                                    }
                                    connectGetData();
                                }
                                adapter.notifyDataSetChanged();
                            }

                        });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        connectGetData();
    }

    private void connectGetData() {
        SQLiteDatabase DB;

        helper = new DiaryHelper(getActivity());

        tv_year.setText(year + "년");
        tv_month.setText(month + "월");

        diaries.clear();

        try {
            DB = helper.getReadableDatabase();
            String query = "SELECT id, content, emoji, SUBSTRING(deal,9) FROM diarydata WHERE date like '" + year + "-" + month + "%';";
            Cursor cursor = DB.rawQuery(query, null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                int emoji = cursor.getInt(2);
                String date = cursor.getString(3);

                Diary diary = new Diary(id, content, emoji, date);
                diaries.add(diary);
            }
            cursor.close();
            helper.close();

            adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
            listView.setAdapter(adapter);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String connectDeleteData(int id){
        String result = null;
        try {
            SQLiteDatabase DB;

            helper = new DiaryHelper(getActivity());

            try {
                DB = helper.getReadableDatabase();
                String query = "DELETE FROM diarydata WHERE id=" + id + ";";
                DB.execSQL(query);
                result = "1";
                helper.close();

            }catch(Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
        //잘끝났으면 1 아니면 에러
    }
}
