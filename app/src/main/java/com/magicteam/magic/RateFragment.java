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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends Fragment {
    private RecyclerView dealListRate;
    private List<Deal> dealList;
    private FirebaseFirestore firebaseFirestore;
    private DealAdapter dealAdapter;

    public RateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        dealListRate = view.findViewById(R.id.dealListRate);
        dealList = new ArrayList<>();

        dealAdapter = new DealAdapter(dealList);

        dealListRate.setLayoutManager(new LinearLayoutManager(container.getContext()));
        dealListRate.setAdapter(dealAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query rateQuery = firebaseFirestore.collection("Deals").orderBy("endingDate", Query.Direction.ASCENDING);

        rateQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()) {
                    if(documentChange.getType() == DocumentChange.Type.ADDED) {
                        // for like/unlike deal
                        String dealId = documentChange.getDocument().getId();
                        Deal deal = documentChange.getDocument().toObject(Deal.class).withId(dealId);
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
