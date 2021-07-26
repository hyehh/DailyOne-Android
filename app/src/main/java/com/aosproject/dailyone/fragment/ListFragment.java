package com.aosproject.dailyone.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aosproject.dailyone.adapter.ListAdapter;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
//    ArrayAdapter<CharSequence> arrayAdapterYear = null;
//    ArrayAdapter<CharSequence> arrayAdapterMonth = null;
    ArrayList<Diary> diaries = new ArrayList<Diary>();

    ListView listView;
    TextView tv_year, tv_month;

//    Spinner spinnerMonth = null;
//    Spinner spinnerYear = null;

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

//        String[] arrayYear = getResources().getStringArray(R.array.year);
//        String[] arrayMonth = getResources().getStringArray(R.array.month);

        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                    .setTitle("연도를 선택하세요.")
                    .setItems(R.array.year,        // 여러개 항목 중 선택할 수 있는 메소드 = items
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] years = getResources().getStringArray(R.array.year);
                                    tv_year = view.findViewById(R.id.listView_tv_year);
                                    tv_year.setText(years[which] + "년");       // which가 몇번째인지 알고있음
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
                        .setItems(R.array.month,        // 여러개 항목 중 선택할 수 있는 메소드 = items
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String[] months = getResources().getStringArray(R.array.month);
                                        tv_month = view.findViewById(R.id.listView_tv_month);
                                        tv_month.setText(months[which] + "월");       // which가 몇번째인지 알고있음
                                        month = months[which];
                                        onResume();
                                    }
                                }
                        )
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

//        spinnerYear = view.findViewById(R.id.listView_sp_select_year);
//        arrayAdapterYear = ArrayAdapter.createFromResource(getContext(), R.array.year, android.R.layout.simple_spinner_item);
//        arrayAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerYear.setAdapter(arrayAdapterYear);
//        spinnerYear.setSelection(getIndex(spinnerYear, year));
//        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                year = arrayYear[position];
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        spinnerMonth = view.findViewById(R.id.listView_sp_select_month);
//        arrayAdapterMonth = ArrayAdapter.createFromResource(getContext(), R.array.month, android.R.layout.simple_spinner_item);
//        arrayAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerMonth.setAdapter(arrayAdapterMonth);
//        spinnerMonth.setSelection(getIndex(spinnerMonth, month));
//        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                month = arrayMonth[position];
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


//        helper = new DiaryHelper(getActivity());
//        adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
//        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SQLiteDatabase DB;

        helper = new DiaryHelper(getActivity());
//        adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
//        listView.setAdapter(adapter);

        tv_year.setText(year + "년");
        tv_month.setText(month + "월");
        Log.v("ListFragment", "onResume");
        diaries.clear();
        try {
            DB = helper.getReadableDatabase();
            String query = "SELECT * FROM diarydata WHERE date like '" + year + "-" + month + "%';";
            Cursor cursor = DB.rawQuery(query, null);

            Log.v("ListFragment", "month" + month);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                int emoji = cursor.getInt(2);
                String date = cursor.getString(3);

                Diary diary = new Diary(id, content, emoji, date);
                diaries.add(diary);
                Log.v("ListFragment", "diary" + diaries);
            }
            cursor.close();
            helper.close();

            adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
            listView.setAdapter(adapter);

        }catch(Exception e) {
            e.printStackTrace();
        }

//        try {
//            adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
//            listView.setAdapter(adapter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    private int getIndex(Spinner spinner, String year) {
//        for(int i=0; i<spinner.getCount(); i++) {
//            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(year)) {
//                return i;
//            }
//        }
//        return 0;
//    }

}
