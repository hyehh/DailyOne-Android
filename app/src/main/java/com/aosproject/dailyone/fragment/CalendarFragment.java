package com.aosproject.dailyone.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.decorator.EventDecorator;
import com.aosproject.dailyone.decorator.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarFragment extends Fragment implements OnDateSelectedListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    MaterialCalendarView materialCalendarView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_calendar, container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        connectGoData();
    }

    private void connectGoData() {
        materialCalendarView = getActivity().findViewById(R.id.material_calendar_view);
        textView = getActivity().findViewById(R.id.text_view);

        // text Size
        materialCalendarView.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        materialCalendarView.setDateTextAppearance(R.style.TextAppearance_AppCompat_Small);
        materialCalendarView.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Small);

        materialCalendarView.addDecorators(
//                new MySelectorDecorator(this),
//                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(this);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        materialCalendarView.invalidateDecorators();
        // 여기에서 비교 formatter 랑 sqlite의 날짜가 같다면! 그럼 그 때 표시해줘~~~
        textView.setText(selected ? FORMATTER.format(date.getDate()) : "No Selection");
    }
    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalDate temp = LocalDate.now().minusMonths(2);
            final ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                final CalendarDay day = CalendarDay.from(temp);
                dates.add(day);
                temp = temp.plusDays(5);
            }

            return dates;
        }

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
