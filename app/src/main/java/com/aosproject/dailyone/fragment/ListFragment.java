package com.aosproject.dailyone.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.bean.Diary;
import com.aosproject.dailyone.util.DiaryHelper;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    DiaryHelper helper;
    ListAdapter adapter;
    ArrayList<Diary> diaries = new ArrayList<Diary>();

    ListView listView;

    Spinner spinnerMonth = null;
    Spinner spinnerYear = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_list, container,false);

        helper = new DiaryHelper(getActivity());
        adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
        listView.setAdapter(adapter);

        listView = view.findViewById(R.id.listView_lv_diaryList);
        spinnerYear = view.findViewById(R.id.listView_sp_select_year);
        spinnerMonth = view.findViewById(R.id.listView_sp_select_month);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SQLiteDatabase DB;

        try {
            DB = helper.getReadableDatabase();
            String query = "SELECT * FROM diarydata WHERE date like concat(left(curdate(), 7), '%');";
            Cursor cursor = DB.rawQuery(query, null);

            StringBuffer stringBuffer = new StringBuffer();
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

        }catch(Exception e) {
            e.printStackTrace();
        }

        try {
            adapter = new ListAdapter(getContext(), R.layout.inner_list, diaries);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
