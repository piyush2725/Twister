package com.example.piyush.twister.Weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by piyush on 14-05-2015.
 */
public class Hour {
    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTimeZone;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(mTime * 1000);
        return formatter.format(date);
    }


    public int describeContents() {
        return 0; // ignore
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeString(mSummary);
        dest.writeString(mIcon);
        dest.writeString(mTimeZone);
    }

    private Hour(Parcel in) {
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mSummary = in.readString();
        mIcon = in.readString();
        mTimeZone = in.readString();
    }

    public static final Parcelable.Creator<Hour> CREATOR = new Parcelable.Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
