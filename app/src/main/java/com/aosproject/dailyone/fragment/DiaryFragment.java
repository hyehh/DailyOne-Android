package com.aosproject.dailyone.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.util.DiaryHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryFragment extends Fragment {

    private final String TAG = "DiaryFragment";

    RelativeLayout layout;
    DiaryHelper diaryHelper;
    EditText edtContent;
    ImageView ivHappy, ivSad, ivAngry, ivSoso;
    Button btnInsert;
    SQLiteDatabase DB;
    ImageView[] ivEmoji = null;

    int dbId = 0, dbEmoji = 0; // happy = 1, sad = 2, angry = 3, soso = 4
    String diaryContent, dbDate, today;
    String query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        diaryHelper = new DiaryHelper(getContext());
        addListener(view);

        return view;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("오늘의 일기");
        today = getTime(); // 오늘 날짜

        // 오늘의 일기를 썼는지 체크. 썼다면 emoji, diaryContent 보여주기
        if (checkWriteDiary(today.substring(0, 10))) {
            edtContent.setText(diaryContent.trim());
            chooseEmoji(dbEmoji - 1);
        }


    }

    // 오늘의 일기를 작성했는지 체크
    private boolean checkWriteDiary(String today) {
        try {
            DB = diaryHelper.getReadableDatabase();
            String query = "SELECT id, content, emoji, date FROM diarydata WHERE date like '" + today + "%';";

            Cursor cursor = DB.rawQuery(query, null);
            StringBuffer stringBuffer = new StringBuffer();

            while (cursor.moveToNext()) {
                dbId = cursor.getInt(0);
                diaryContent = cursor.getString(1);
                dbEmoji = cursor.getInt(2);
                dbDate = cursor.getString(3);
            }
            cursor.close();
            diaryHelper.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 오늘 날짜 불러오기
    private String getTime() {
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String stringDate = mFormat.format(mDate);
        return stringDate;
    }

    // emoji 클릭시 배경 변경 ----------------------------
    View.OnClickListener emojiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.diary_iv_emoji_happy:
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
    private int chooseEmoji(int emojiNum) {
        for (int i = 0; i < ivEmoji.length; i++) {
            ivEmoji[i].setBackgroundColor(Color.TRANSPARENT);
            if (i == emojiNum) {
                ivEmoji[i].setBackgroundColor(Color.LTGRAY);
            }
        }
        return emojiNum + 1;
    }
    // ---------------------------- end


    // 작성 완료 버튼 클릭시 insert or update
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
                        if (diaryContent.trim().length() == 0) {
                            query = "INSERT INTO diarydata (content, emoji) VALUES (' ', " + dbEmoji + ");";
                        }else {
                            query = "INSERT INTO diarydata (content, emoji) VALUES ('" + diaryContent + "', " + dbEmoji + ");";
                        }
                        DB.execSQL(query);
                        diaryHelper.close();
                        Toast.makeText(getContext(), "오늘의 일기가 등록되었습니다.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "일기 등록에 실패했습니다!\n관리자에게 문의 해 주세요.", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                try {
                    today = getTime();
                    if(diaryContent.trim().length() == 0) {
                        query = "UPDATE diarydata SET content = ' ', emoji = " + dbEmoji + ", date = '" + today + "' WHERE id = " + dbId + ";";
                    }else {
                        query = "UPDATE diarydata SET content = '" + diaryContent + "', emoji = " + dbEmoji + ", date = '" + today + "' WHERE id = " + dbId + ";";
                    }
                    DB.execSQL(query);
                    diaryHelper.close();
                    Toast.makeText(getContext(), "오늘의 일기가 수정되었습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "일기 수정에 실패했습니다!\n관리자에게 문의 해 주세요.", Toast.LENGTH_LONG).show();
                }
            }

        }
    };


    // init widget
    private void addListener(View view) {

        layout = view.findViewById(R.id.diary_layout);

        edtContent = view.findViewById(R.id.diary_edt_content);

        ivHappy = view.findViewById(R.id.diary_iv_emoji_happy);
        ivSad = view.findViewById(R.id.diary_iv_emoji_sad);
        ivAngry = view.findViewById(R.id.diary_iv_emoji_angry);
        ivSoso = view.findViewById(R.id.diary_iv_emoji_soso);
        ivEmoji = new ImageView[]{ivHappy, ivSad, ivAngry, ivSoso};

        btnInsert = view.findViewById(R.id.diary_btn_insert);

        edtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        ivHappy.setOnClickListener(emojiClickListener);
        ivSad.setOnClickListener(emojiClickListener);
        ivAngry.setOnClickListener(emojiClickListener);
        ivSoso.setOnClickListener(emojiClickListener);

        btnInsert.setOnClickListener(insertClickListener);


        // edittext 외의 화면 클릭시 키보드 내리기
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtContent.clearFocus();
                InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(edtContent.getWindowToken(), 0);
                return false;
            }
        });

    }
}