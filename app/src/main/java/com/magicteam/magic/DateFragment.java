package com.magicteam.magic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment {
    private RecyclerView dealListDate;


    public DateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dealListDate = getActivity().findViewById(R.id.dealListDate);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

}
