package com.aosproject.dailyone.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.util.DiaryHelper;

public class ListFragment extends Fragment {

    DiaryHelper diary;

    Spinner spinnerMonth = null;
    Spinner spinnerYear = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_list, container,false);

//        spinnerYear = findViewById(R.id.select_year);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SQLiteDatabase DB;

        try {
            DB = diary.getReadableDatabase();                  // select 할 수 있는 권한을 준다.
            String query = "SELECT content, emoji, date FROM diarydata WHERE;";
            Cursor cursor = DB.rawQuery(query, null);   // Cursor = DB 위치를 알려주는 class

            StringBuffer stringBuffer = new StringBuffer();         // ArrayList 대신 StringBuffer (ArrayList는 Bean을 만들어야해서 불편)
            while (cursor.moveToNext()) {                           // cursor.moveToNext() = rs.next()
                String content = cursor.getString(0);
                int emoji = cursor.getInt(1);
                String date = cursor.getString(2);

                stringBuffer.append("content : " + content + ", emoji : " + emoji + ", date : " + date + "\n");
            }

//            tvResult.setText(stringBuffer.toString());

            cursor.close();
            diary.close();
//            Toast.makeText(MainActivity.this, "Select OK!", Toast.LENGTH_SHORT).show();

        }catch(Exception e) {
            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "Select Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
