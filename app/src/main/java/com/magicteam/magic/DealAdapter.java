package com.magicteam.magic;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder> {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public List<Deal> dealList;
    public Context context;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int item) {
        viewHolder.setIsRecyclable(false);
//        final String dealId = dealList.get(item).;
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
        private TextView rateView;
        private ImageView imageView;
        private ImageView likeButton;
        private ImageView unlikeButton;
        private ImageView commentButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            likeButton = mView.findViewById(R.id.likeMainDeal);
            unlikeButton = mView.findViewById(R.id.unlikeMainDeal);
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
        }

        public void setPriceText(String priceText) {
            priceView = mView.findViewById(R.id.priceMainDeal);
            priceView.setText(priceText + "€");
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
