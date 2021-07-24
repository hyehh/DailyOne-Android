package com.aosproject.dailyone.fragment;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.aosproject.dailyone.MainActivity;
import com.aosproject.dailyone.R;
import com.aosproject.dailyone.util.DiaryHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryFragment extends Fragment {

    private final String TAG = "DiaryFragment";

    DiaryHelper diaryHelper;
    EditText edtContent;
    ImageView ivJoy, ivSad, ivAngry, ivSoso;
    Button btnInsert;
    SQLiteDatabase DB;
    ImageView[] ivEmoji = null;

    int dbId = 0, dbEmoji = 0; // joy = 1, sad = 2, angry = 3, soso = 4
    String diaryContent, dbDate, today;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_diary, container,false);

        diaryHelper = new DiaryHelper(getContext());
        addListener(view);

        today = getTime();
        if (checkWriteDiary(today.substring(0,10))){
            edtContent.setText(diaryContent);
            chooseEmoji(dbEmoji-1);
        }

        return view;
    }

    private void addListener(View view){
        edtContent = view.findViewById(R.id.diary_edt_content);

        ivJoy = view.findViewById(R.id.diary_iv_emoji_joy);
        ivSad = view.findViewById(R.id.diary_iv_emoji_sad);
        ivAngry = view.findViewById(R.id.diary_iv_emoji_angry);
        ivSoso = view.findViewById(R.id.diary_iv_emoji_soso);
        ivEmoji = new ImageView[] {ivJoy, ivSad, ivAngry, ivSoso};

        btnInsert = view.findViewById(R.id.diary_btn_insert);

        edtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        ivJoy.setOnClickListener(emojiClickListener);
        ivSad.setOnClickListener(emojiClickListener);
        ivAngry.setOnClickListener(emojiClickListener);
        ivSoso.setOnClickListener(emojiClickListener);

        btnInsert.setOnClickListener(insertClickListener);
    }

    View.OnClickListener insertClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DB = diaryHelper.getWritableDatabase();
            diaryContent = edtContent.getText().toString();
            if (dbId == 0) {
                if (dbEmoji == 0) {
                    Toast.makeText(getContext(), "오늘 하루는 어떠셨나요?\n오늘의 감정을 선택해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String query = "INSERT INTO diarydata (content, emoji) VALUES ('" + diaryContent + "', " + dbEmoji + ");";
                        DB.execSQL(query);

                        diaryHelper.close();
                        Toast.makeText(getContext(), "오늘의 일기가 등록되었습니다.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Insert Error!", Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                try{
                    today = getTime();
                    String query = "UPDATE diarydata SET content = '" + diaryContent + "', emoji = " + dbEmoji + ", date = '" + today + "' WHERE id = " + dbId + ";";
                    DB.execSQL(query);
                    diaryHelper.close();
                    Toast.makeText(getContext(), "Update OK!", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Update Error!" , Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    View.OnClickListener emojiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.diary_iv_emoji_joy:
                    dbEmoji = chooseEmoji(0);
                    break;
                case R.id.diary_iv_emoji_sad:
                    dbEmoji = chooseEmoji(1);
                    break;
                case R.id.diary_iv_emoji_angry:
                    dbEmoji = chooseEmoji(2);
                    break;
                case R.id.diary_iv_emoji_soso:
                    dbEmoji = chooseEmoji(3);
                    break;
                default:
                    break;
            }
        }
    };

    // get emoji number
    private int chooseEmoji(int emojiNum){
        for(int i=0; i<ivEmoji.length; i++){
            ivEmoji[i].setBackgroundColor(Color.TRANSPARENT);
            if(i==emojiNum){
                ivEmoji[i].setBackgroundColor(Color.LTGRAY);
            }
        }
        return emojiNum + 1;
    }

    private boolean checkWriteDiary(String today){
        try{
            DB = diaryHelper.getReadableDatabase();
            String query = "SELECT id, content, emoji, date FROM diarydata WHERE date like '" + today + "%';";

            Cursor cursor = DB.rawQuery(query, null);
            StringBuffer stringBuffer = new StringBuffer();

            while(cursor.moveToNext()){
                dbId = cursor.getInt(0);
                diaryContent = cursor.getString(1);
                dbEmoji = cursor.getInt(2);
                dbDate = cursor.getString(3);
                Log.v(TAG, query);
                Log.v(TAG, "id = " + dbId + "content = " + diaryContent);
            }
            cursor.close();
            diaryHelper.close();
            Toast.makeText(getContext(), "Select OK!", Toast.LENGTH_LONG).show();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Select Error!" , Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private String getTime() {
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String stringDate = mFormat.format(mDate);
        return stringDate;
    }
}
