package com.pokhare.wondersinindia;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {
    private Context context;
    private CardsAdapterListener cardsAdapterListener;
    private ArrayList<WonderModel> list;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public CardsAdapter(ArrayList<WonderModel> Data, CardsAdapterListener cardsAdapterListener, Context context) {
        this.list = Data;
        this.cardsAdapterListener=cardsAdapterListener;
        this.context=context;
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference();
    }

    @Override
    public CardsAdapter.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);
        return new CardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardsAdapter.CardsViewHolder holder, final int position) {

        holder.titleTextView.setText(list.get(position).getCardName());
//            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
//            holder.coverImageView.setTag(list.get(position).getImageResourceId());
        holder.likeImageView.setTag(R.drawable.ic_like);
        final WonderModel model = list.get(position);
        //loadBitmap(model.imageResourceId,holder.coverImageView);


        StorageReference imageRef = storageRef.child("images/india/"+model.imageResourceId);
        GlideApp.with(context)
                .load(imageRef)
                .into(holder.coverImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CardsAdapterListener{
        void directionsOnClick(View v, int position);
        void detailsOnClick(View v, int position);
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public ImageView directionsImageView;

        public CardsViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            directionsImageView=(ImageView)v.findViewById(R.id.directionsImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_like){

                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);

                        Toast.makeText(context,titleTextView.getText()+" added to favourites",Toast.LENGTH_SHORT).show();

                    }else{

                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
                        Toast.makeText(context,titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + context.getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + context.getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.send_to)));
                }
            });

            directionsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardsAdapterListener.directionsOnClick(v, getAdapterPosition());

                }
            });

            coverImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardsAdapterListener.detailsOnClick(v,getAdapterPosition());
                }
            });
        }
    }
}
