package com.example.anamartinazzo.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_refresh) {
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("Sao Paulo,BR");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate view that will be used to find elements later
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //fake data array
        String[] forecastArray = {
                "hoje - quente",
                "amanha - quente",
                "quarta - chuva",
                "quinta - mais chuva",
                "sexta - frio",
                "sabado - dia de bike",
                "domingo - solzinho"
        };

        //convert to string
        List<String> weekForecast = new ArrayList<String> (Arrays.asList(forecastArray));

        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(
                getActivity(), //current context (this fragment's parent activity)
                R.layout.list_item, //list item layout
                R.id.list_item_textview, //id of the textview to populate
                weekForecast
        );

        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    //AsyncTask<input, progress, output>
    public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground (String... params) {
            // HTTP CLIENT START
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            //PARAMS
            String format = "json";
            String units = "metric";
            int numDays = 7;
            String appId = "05377ce9c74e7c1920dd52f63fc48297";

            try {
                final String baseUrl = "http://api.openweathermap.org/data/2.5/forecast?";
                final String queryParam = "q";
                final String formatParam = "mode";
                final String unitsParam = "units";
                final String daysParam = "cnt";
                final String key = "APPID";

                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(queryParam, params[0])
                        .appendQueryParameter(formatParam, format)
                        .appendQueryParameter(unitsParam, units)
                        .appendQueryParameter(daysParam, Integer.toString(numDays))
                        .appendQueryParameter(key, appId)
                        .build();

                Log.i(LOG_TAG, builtUri.toString());

                URL url = new URL(builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }
    }
}
