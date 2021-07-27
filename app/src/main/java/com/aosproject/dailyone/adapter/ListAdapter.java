package com.aosproject.dailyone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosproject.dailyone.R;
import com.aosproject.dailyone.bean.Diary;
import com.aosproject.dailyone.util.DiaryHelper;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Diary> diaries = null;
    private LayoutInflater inflater = null;
    private DiaryHelper diaryHelper;

    public ListAdapter(Context mContext, int layout, ArrayList<Diary> diaries) {
        this.mContext = mContext;
        this.layout = layout;
        this.diaries = diaries;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return diaries.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.listVIew_iv_emoji);
        TextView tv_date = convertView.findViewById(R.id.listView_tv_date);
        TextView tv_content = convertView.findViewById(R.id.listView_tv_content);

        switch (diaries.get(position).getEmoji()) {
            case 1:
                imageView.setImageResource(R.drawable.emoji_happy);
                break;
            case 2:
                imageView.setImageResource(R.drawable.emoji_sad);
                break;
            case 3:
                imageView.setImageResource(R.drawable.emoji_angry);
                break;
            case 4:
                imageView.setImageResource(R.drawable.emoji_soso);
                break;
        }
        tv_date.setText(diaries.get(position).getDate() + "일");
        tv_content.setText(diaries.get(position).getContent());

        return convertView;
    }
}
