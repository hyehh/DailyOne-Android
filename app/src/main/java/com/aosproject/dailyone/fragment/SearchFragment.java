package com.aosproject.dailyone.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.adapter.SeachAdapter;
import com.aosproject.dailyone.bean.Diary;
import com.aosproject.dailyone.util.DiaryHelper;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    DiaryHelper helper;
    ListView listView;
    SeachAdapter adapter;
    ArrayList<Diary> diaries = new ArrayList<Diary>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_search, container,false);

        listView = view.findViewById(R.id.searched_list);
        helper = new DiaryHelper(getActivity());
        adapter = new SeachAdapter(getContext(), R.layout.cell_search_list, diaries);
        listView.setAdapter(adapter);

        return view;
    }


    private void searchingDiary(int condition) {
        SQLiteDatabase DB;
        try {
            diaries.clear();
            DB = helper.getReadableDatabase();
            String query = "SELECT * FROM diarydata WHERE emoji = " + condition;
            Cursor cursor = DB.rawQuery(query, null);

            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                int emoji = cursor.getInt(2);
                String date = cursor.getString(3);

                Diary diary = new Diary(id, content, emoji, date);
                diaries.add(diary);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            adapter = new SeachAdapter(getContext(), R.layout.cell_search_list, diaries);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
