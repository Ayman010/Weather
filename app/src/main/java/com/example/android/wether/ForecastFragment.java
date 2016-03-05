package com.example.android.wether;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by heba_ on 05/03/16.
 */
    public  class ForecastFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ForecastFragment() {
    }
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(
            Menu menu,MenuInflater inflater){inflater.inflate(R.menu.forecastfragment, menu);}


    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.action_refresh){
            FetchWeatherTask weatherTask=new FetchWeatherTask();
            weatherTask.execute("94043");
            return true;}
        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ForecastFragment newInstance(int sectionNumber) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String []forecastArray={
                "today - Sunny 88/63",
                "Tomorrow - Cloudy 88/63",
                "Weds - Snowy 88/63",
                "Thurs - Foggy 88/63",
                "Fri - Rainy 88/63",
                "Sat - Sunny 88/63",
                "Sun - Sunny 88/63"

        };

        List<String> weekForecast=new ArrayList<String>(Arrays.asList(forecastArray));

        ArrayAdapter mForecastAdapter;
        mForecastAdapter=new ArrayAdapter(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textView,weekForecast);
        ListView listView =(ListView)rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(mForecastAdapter);
        return rootView;
    }
    public class FetchWeatherTask extends AsyncTask<String , Void,Void>{

        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String foreCastJsonStr =null;
            String format="jason";
            String units="metric";
            int numDays=7;



            try {
                final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM="q";
                final String FORMAT_PARAM="mode";
                final String UNITS_PARAM="units";
                final String DAYS_PARAM="cnt";
                Uri builtUri =Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                        .build();
                URL url=new URL(builtUri.toString());
                //"http://api.openweathermap.org/data/2.5/forecast/daily?q=Jeddah&mode=xml&units=metric&cnt=7&appid=44db6a862fba0b067b1930da0d769e98"
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                if (inputStream==null){return null;}
                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line =reader.readLine())!=null){
                    buffer.append(line +"/n");}
                if(buffer.length()==0){
                    return null;}
                foreCastJsonStr=buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            finally {
                if (urlConnection!=null){urlConnection.disconnect();}
            }
            if (reader!=null){try {reader.close();}

            catch (final IOException e){e.printStackTrace();}}



            return null;
        }
    }
}