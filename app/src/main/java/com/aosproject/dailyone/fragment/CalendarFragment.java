package com.aosproject.dailyone.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.decorator.EventDecorator;
import com.aosproject.dailyone.decorator.OneDayDecorator;
import com.aosproject.dailyone.decorator.SelectDayDecorator;
import com.aosproject.dailyone.util.DiaryHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarFragment extends Fragment implements OnDateSelectedListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private final SelectDayDecorator selectDayDecorator = new SelectDayDecorator();

    MaterialCalendarView materialCalendarView;
    TextView calendar_tv_content;
    ImageView calendar_iv_emoji;
    SQLiteDatabase db;
    DiaryHelper diary;
    String dbContent, dbEmoji, dbDate, calendarDate;
    String[] dbContentList, dbEmojiList, dbDateList;
    LinearLayout calendarLinearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_calendar, container,false);
        Log.v("Message", "onCreateView");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("캘린더");
        Log.v("Message", "Resume");
        connectGoData();
    }

    // sqlite와 layout과 연결
    private void connectGoData() {
        diary = new DiaryHelper(getActivity());

        materialCalendarView = getActivity().findViewById(R.id.material_calendar_view);
        calendar_tv_content = getActivity().findViewById(R.id.calendar_tv_content);
        calendar_iv_emoji = getActivity().findViewById(R.id.calendar_iv_emoji);
        calendarLinearLayout = getActivity().findViewById(R.id.calendar_linearLayout);

        dbSelect();
    }

    // sqlite db 불러오기 및 캘린더 초기 설정
    private void dbSelect() {
        try {
            db = diary.getReadableDatabase();
            String query = "SELECT content, emoji, date FROM diarydata;";
            Cursor cursor = db.rawQuery(query, null);
            StringBuffer stringBufferContent = new StringBuffer();
            StringBuffer stringBufferEmoji = new StringBuffer();
            StringBuffer stringBufferDate = new StringBuffer();
            while (cursor.moveToNext()){
                String content = cursor.getString(0);
                int emoji = cursor.getInt(1);
                String date = cursor.getString(2);
                stringBufferContent.append(content+"@,~/!@");
                stringBufferEmoji.append(emoji+",");
                stringBufferDate.append(date+",");
            }

            dbContent = stringBufferContent.toString();
            dbEmoji = stringBufferEmoji.toString();
            dbDate = stringBufferDate.toString();

            dbContentList = dbContent.split("@,~/!@");
            dbEmojiList = dbEmoji.split(",");
            dbDateList = dbDate.split(",");

            cursor.close();
            diary.close();

            materialCalendarView.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
            materialCalendarView.setDateTextAppearance(R.style.TextAppearance_AppCompat_Medium);
            materialCalendarView.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);

            materialCalendarView.addDecorators(
                    selectDayDecorator,
                    oneDayDecorator
            );
            new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

            materialCalendarView.setOnDateChangedListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 캘린더에서 특정 날짜를 선택했을 경우
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectDayDecorator.setDate(date.getDate());
        materialCalendarView.invalidateDecorators();

        calendarDate = FORMATTER.format(date.getDate());
        if(selected) {
            calendarLinearLayout.setVisibility(View.GONE);
            if(dbDateList[0].isEmpty() == false){
                for (int i = 0; i < dbDateList.length; i++) {
                    if (calendarDate.equals(dbDateList[i].substring(0, dbDateList[i].indexOf(" ")))) {
                        calendarLinearLayout.setVisibility(View.VISIBLE);

                        if (dbContentList[i].trim().length() == 0) {
                            calendar_tv_content.setText(" ");
                        }else {
                            calendar_tv_content.setText(dbContentList[i]);
                        }

                        if(dbEmojiList[i].equals("1")){
                            calendar_iv_emoji.setImageResource(R.drawable.emoji_happy);
                        }else if(dbEmojiList[i].equals("2")){
                            calendar_iv_emoji.setImageResource(R.drawable.emoji_sad);
                        }else if(dbEmojiList[i].equals("3")){
                            calendar_iv_emoji.setImageResource(R.drawable.emoji_angry);
                        }else {
                            calendar_iv_emoji.setImageResource(R.drawable.emoji_soso);
                        }
                    }
                }
            }

        }
    }
    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        // sqlite에 데이터가 있는 날짜에 점 찍기
        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final ArrayList<CalendarDay> dates = new ArrayList<>();

            if (dbDateList[0].isEmpty() == false) {
                for (int i = 0; i < dbDateList.length; i++) {
                    final CalendarDay day = CalendarDay.from(LocalDate.parse(dbDateList[i].substring(0, dbDateList[i].indexOf(" "))));
                    dates.add(day);
                }
            }

            return dates;
        }

        // 특정 날짜에 찍는 점에 대한 decorator 추가
        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#1b663e"), calendarDays));

        }
    }
}