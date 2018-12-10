package com.magicteam.magic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment {
    private RecyclerView dealListDate;
    private List<Deal> dealList;
    private FirebaseFirestore firebaseFirestore;
    private DealAdapter dealAdapter;

    public DateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_date, container, false);
        dealListDate = view.findViewById(R.id.dealListDate);
        dealList = new ArrayList<>();

        dealAdapter = new DealAdapter(dealList);

        dealListDate.setLayoutManager(new LinearLayoutManager(container.getContext()));
        dealListDate.setAdapter(dealAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Deals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()) {
                    if(documentChange.getType() == DocumentChange.Type.ADDED) {
                        Deal deal = documentChange.getDocument().toObject(Deal.class);
                        dealList.add(deal);
                        dealAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
