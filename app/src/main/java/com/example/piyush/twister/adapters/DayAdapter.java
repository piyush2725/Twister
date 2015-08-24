package com.example.piyush.twister.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piyush.twister.R;
import com.example.piyush.twister.Weather.Day;

/**
 * Created by piyush on 15-05-2015.
 */
//Custom Adapter to adapt the Day model data
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context,Day[] days){
        mContext=context;
        mDays=days;
    }
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;       //not used...tag items for only references
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null) {
            // brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);
        }
            else{
            holder=(ViewHolder)convertView.getTag();
            }
        Day day=mDays[position];

        holder.iconImageView.setImageResource(day.getIconId()); //Add Helper method
        holder.temperatureLabel.setText(day.getTemperatureMax()+"");
        holder.dayLabel.setText(day.getDayOfTheWeek());  //Add Helper method
        return null;
    }

    private static class ViewHolder {
        ImageView iconImageView //public by default
        TextView temperatureLabel;
        TextView dayLabel;
    }


}
