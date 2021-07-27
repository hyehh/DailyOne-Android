package com.aosproject.dailyone.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

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
    SeachAdapter adapter;
    ArrayList<Diary> diaries = new ArrayList<Diary>();

    ListView listView;
    ToggleButton joyButton, sadButton, angryButton, sosoButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_search, container,false);

        listView = view.findViewById(R.id.searched_list);
        helper = new DiaryHelper(getActivity());
        adapter = new SeachAdapter(getContext(), R.layout.cell_search_list, diaries);
        listView.setAdapter(adapter);

        joyButton = view.findViewById(R.id.search_joy_button);
        joyButton.setOnClickListener(onClickListener);

        sadButton = view.findViewById(R.id.search_sad_button);
        sadButton.setOnClickListener(onClickListener);

        angryButton = view.findViewById(R.id.search_angry_button);
        angryButton.setOnClickListener(onClickListener);

        sosoButton = view.findViewById(R.id.search_soso_button);
        sosoButton.setOnClickListener(onClickListener);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("검색");
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search_joy_button:
                    searchingDiary(1);
                    if (joyButton.isChecked()) {
                        joyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_happy));
                        sadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_sad_off));
                        angryButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_angry_off));
                        sosoButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_soso_off));
                        sadButton.setChecked(false);
                        angryButton.setChecked(false);
                        sosoButton.setChecked(false);
                    }
//                    else {
//                        joyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_happy_off));
//                    }
                    break;
                case R.id.search_sad_button:
                    searchingDiary(2);
                    if (sadButton.isChecked()) {
                        joyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_happy_off));
                        sadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_sad));
                        angryButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_angry_off));
                        sosoButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_soso_off));
                        joyButton.setChecked(false);
                        angryButton.setChecked(false);
                        sosoButton.setChecked(false);
                    }
//                    else {
//                        sadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_sad_off));
//                    }
                    break;
                case R.id.search_angry_button:
                    searchingDiary(3);
                    if (angryButton.isChecked()) {
                        joyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_happy_off));
                        sadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_sad_off));
                        angryButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_angry));
                        sosoButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_soso_off));
                        sadButton.setChecked(false);
                        joyButton.setChecked(false);
                        sosoButton.setChecked(false);
                    }
//                    else {
//                        angryButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_angry_off));
//                    }
                    break;
                case R.id.search_soso_button:
                    searchingDiary(4);
                    if (sosoButton.isChecked()) {
                        joyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_happy_off));
                        sadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_sad_off));
                        angryButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_angry_off));
                        sosoButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_soso));
                        sadButton.setChecked(false);
                        angryButton.setChecked(false);
                        joyButton.setChecked(false);
                    }
//                    else {
//                        sosoButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.emoji_soso_off));
//                    }
                    break;
            }
        }
    };

}
