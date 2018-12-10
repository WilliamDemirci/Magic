package com.magicteam.magic;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder> {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public List<Deal> dealList;
    public Context context;
    private int tmpScore;
    private int minusSize, plusSize;

    public DealAdapter(List<Deal> dealList) {
        this.dealList = dealList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deal, viewGroup, false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int item) {
        viewHolder.setIsRecyclable(false);
        final String dealId = dealList.get(item).DealId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String titleData = dealList.get(item).getTitle();
        viewHolder.setTitleText(titleData);

        String image = dealList.get(item).getImage();
        String thumb = dealList.get(item).getThumb();
        viewHolder.setImages(image, thumb);

        String categories = dealList.get(item).getCategories();
        viewHolder.setCategoriesText(categories);

        String endingDate = dealList.get(item).getEndingDate();
        viewHolder.setEndingDateText(endingDate);

        String price = dealList.get(item).getPrice();
        viewHolder.setPriceText(price);

        // rating score
//        String scoreFF = dealList.get(item).getScore();
//        viewHolder.setScore(scoreFF);
//        tmpScore = Integer.valueOf(score);

        // get rate vote to set thumb icon color
        firebaseFirestore.collection("Deals").document(dealId).collection("Plus").document(currentUserId).addSnapshotListener((Activity) context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    viewHolder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                    viewHolder.unlikeButton.setEnabled(false);
                }
                else {
                    viewHolder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_up_grey_24dp));
                }
            }
        });
        firebaseFirestore.collection("Deals").document(dealId).collection("Minus").document(currentUserId).addSnapshotListener((Activity) context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    viewHolder.unlikeButton.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_down_blue_24dp));
                    viewHolder.likeButton.setEnabled(false);
                }
                else {
                    viewHolder.unlikeButton.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_down_grey_24dp));
                }
            }
        });

        // like button -> disable unlike button and put timestamp in database
        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.unlikeButton.setEnabled(false);
                firebaseFirestore.collection("Deals").document(dealId).collection("Plus").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()) {
                            Map<String, Object> rateMap = new HashMap<>();
                            rateMap.put("timestamp", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Deals").document(dealId).collection("Plus").document(currentUserId).set(rateMap);
                        }
                    }
                });
            }
        });

        // unlike button -> disable like button and put timestamp in database
        viewHolder.unlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.likeButton.setEnabled(false);
                firebaseFirestore.collection("Deals").document(dealId).collection("Minus").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()) {
                            Map<String, Object> rateMap = new HashMap<>();
                            rateMap.put("timestamp", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Deals").document(dealId).collection("Minus").document(currentUserId).set(rateMap);
                        }
                    }
                });
            }
        });

        // retrieve size of Minus
        firebaseFirestore.collection("Deals").document(dealId).collection("Minus").addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    minusSize = documentSnapshots.size();
                    viewHolder.setScore(String.valueOf(plusSize - minusSize));
//                    viewHolder.setScore(scoreFF);
                }
                else {
                    minusSize = 0;
                    viewHolder.setScore(String.valueOf(plusSize - minusSize));
//                    viewHolder.setScore("0");
                }

            }
        });

        // retrieve size of Plus
        firebaseFirestore.collection("Deals").document(dealId).collection("Plus").addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    plusSize = documentSnapshots.size();
                    viewHolder.setScore(String.valueOf(plusSize - minusSize));
//                    viewHolder.setScore(scoreFF);
                }
                else {
                    plusSize = 0;
                    viewHolder.setScore(String.valueOf(plusSize - minusSize));
//                    viewHolder.setScore("0");
                }

            }
        });


//        Map<String, Object> scoreMap = new HashMap<>();
//        scoreMap.put(String.valueOf(tmpScore), score);
//        firebaseFirestore.collection("Deals").document(dealId).set(scoreMap);
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView titleView;
        private TextView categoriesView;
        private TextView endingDateView;
        private TextView priceView;
        private ImageView imageView;
        private TextView rateView;
        private ImageView likeButton;
        private ImageView unlikeButton;
        private ImageView commentButton;
        private ImageView shareButton;
        private ImageView hourglassIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            likeButton = mView.findViewById(R.id.likeMainDeal);
            unlikeButton = mView.findViewById(R.id.unlikeMainDeal);
            shareButton = mView.findViewById(R.id.shareMainDeal);
        }

        public void setScore(String rateValue) {
            rateView = mView.findViewById(R.id.rateMainDeal);
            rateView.setText(rateValue);
        }

        public void setTitleText(String titleText) {
            titleView = mView.findViewById(R.id.titleMainDeal);
            titleView.setText(titleText);
        }

        public void setCategoriesText(String categoriesText) {
            categoriesView = mView.findViewById(R.id.categoriesMainDeal);
            categoriesView.setText(categoriesText);
        }

        public void setEndingDateText(String endingDateText) {
            endingDateView = mView.findViewById(R.id.dateMainDeal);
            endingDateView.setText(endingDateText);

            if(endingDateText.isEmpty()) {
                hourglassIcon = mView.findViewById(R.id.hourglassMainDeal);
                hourglassIcon.setVisibility(View.INVISIBLE);
            }
        }

        public void setPriceText(String priceText) {
            priceView = mView.findViewById(R.id.priceMainDeal);
            priceView.setText(priceText + " " + context.getString(R.string.czech_currency));
        }

        public void setImages(String downloadUri, String thumbUri){
            imageView = mView.findViewById(R.id.imageMainDeal);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.hat);

            Glide.with(context).applyDefaultRequestOptions(requestOptions)
                    .load(downloadUri)
                    .thumbnail(Glide.with(context).load(thumbUri)).into(imageView);
        }
    }
}
