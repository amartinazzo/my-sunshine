package com.example.anamartinazzo.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        inflate view that will be used to find elements later
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        fake data array
        String[] forecastArray = {
                "hoje - quente",
                "amanha - quente",
                "quarta - chuva",
                "quinta - mais chuva",
                "sexta - frio",
                "sabado - dia de bike",
                "domingo - solzinho"
        };

//         convert to string
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
}
