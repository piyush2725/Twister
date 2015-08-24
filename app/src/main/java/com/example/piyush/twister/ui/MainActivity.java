package com.example.piyush.twister.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush.twister.R;
import com.example.piyush.twister.Weather.Current;
import com.example.piyush.twister.Weather.Day;
import com.example.piyush.twister.Weather.Forecast;
import com.example.piyush.twister.Weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;

    @InjectView(R.id.temperatureLabel)TextView mtemperatureLabel;
    @InjectView(R.id.timelabel)TextView mTimeLabel;
    @InjectView(R.id.humidityvalue)TextView mHumidityvalue;
    @InjectView(R.id.precipvalue)TextView mPrecipvalue;
    @InjectView(R.id.conditionlabel)TextView mConditionLabel;
    @InjectView(R.id.iconImage)ImageView mIconImage;
    @InjectView(R.id.refreshimageView)ImageView mrefreshImageView;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude=37.8267;
        final double longitude=-122.423;


        mrefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });

        getForecast(latitude,longitude);
          Log.d(TAG,"Main thread is running!!");

    }

    private void getForecast(double latitude,double longitude) {
        String apiKey="103a616ab63003dcbc032a5f0ae78487";
         String forecastUrl="https://api.forecast.io/forecast/"+apiKey +"/"+latitude+","+ longitude;

        if(isNetworkAvailable()==true) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          toggleRefresh();
                      }
                  });
                    createAlertDialog();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    String jsonData = response.body().string();
                    try {
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            createAlertDialog();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception caught:", e);
                    }


                }
            });
        }
        else {
            Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mrefreshImageView.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.VISIBLE);
            mrefreshImageView.setVisibility(View.INVISIBLE);

        }
    }

    private void updateDisplay() {
        Current current= mForecast.getCurrentForecast();
        mtemperatureLabel.setText(current.getTemperature() + "");
        mConditionLabel.setText(current.getSummary());
        mHumidityvalue.setText(current.getHumidity() + "");
        mPrecipvalue.setText(current.getPrecipChance() + "%");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        Drawable drawable=getResources().getDrawable(current.getIconId());
        mIconImage.setImageDrawable(drawable);

    }

    private Forecast parseForecastDetails(String jsonData)
            throws JSONException{
        Forecast forecast=new Forecast();
        forecast.setCurrentForecast(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;
    }

    private Hour[] getHourlyForecast(String jsonData)  throws JSONException {

        JSONObject forecast=new JSONObject(jsonData);
        String timezone=forecast.getString("timezone");
        JSONObject hourly=forecast.getJSONObject("hourly");
        JSONArray data=hourly.getJSONArray("data");

        Hour[] hours =new Hour[data.length()];

        for(int i=0;i<data.length();i++){
            JSONObject jsonHour=data.getJSONObject(i);
            Hour hour=new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimeZone(timezone);

            hours[i]=hour;
        }
        return hours;

    }

    private Day[] getDailyForecast(String jsonData) throws JSONException{
        JSONObject forecast=new JSONObject(jsonData);
        String timezone=forecast.getString("timezone");
        JSONObject daily=forecast.getJSONObject("daily");
        JSONArray data=daily.getJSONArray("data");

        Day[] days =new Day[data.length()];

        for(int i=0;i<data.length();i++){
            JSONObject jsonday=data.getJSONObject(i);
            Day day=new Day();

            day.setSummary(jsonday.getString("summary"));
            day.setTemperatureMax(jsonday.getDouble("temperatureMax"));
            day.setIcon(jsonday.getString("icon"));
            day.setTime(jsonday.getLong("time"));
            day.setTimeZone(timezone);

            days[i]=day;
        }
        return days;

    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);
        String timezone=forecast.getString("timezone");
        Log.i(TAG, "From Json:" + timezone);
        JSONObject currently=forecast.getJSONObject("currently");

        Current Current =new Current();
        Current.setHumidity(currently.getDouble("humidity"));
        Current.setTemperature(currently.getDouble("temperature"));
        Current.setPrecipChance(currently.getDouble("precipProbability"));
        Current.setIcon(currently.getString("icon"));
        Current.setTime(currently.getLong("time"));
        Current.setSummary(currently.getString("summary"));
        Current.setTimeZone(timezone);

        Log.d(TAG, Current.getFormattedTime());
        return Current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isAvailable=false;
        if(info!=null && info.isConnected()) {
            isAvailable=true;
        }
        return isAvailable;
    }

    private void createAlertDialog() {
    AlertDialogFragment dialog=new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }
}



