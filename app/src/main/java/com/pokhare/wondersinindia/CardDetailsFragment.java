package com.pokhare.wondersinindia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardDetailsFragment extends Fragment {
    public static final String CARD_DETAIL_ID = "CARD_DETAIL_ID";

    public CardDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_card_details, container, false);
        Bundle arguments = this.getArguments();
        final String cardId=arguments.getString(CARD_DETAIL_ID);
        final WonderModel[] cardModels = new WonderModel[1];
        DatabaseReference wondersOfIndiaDB = FirebaseDatabase.getInstance().getReference("WondersOfIndia");
        wondersOfIndiaDB.child("places").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("CardDetailsFragment","Data retrieved from Database");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final WonderModel model = ds.getValue(WonderModel.class);
                    if(model.getId() == cardId){
                        cardModels[0] =model;
                        ImageView coverImageView=view.findViewById(R.id.headerImageView);
                        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/india/"+model.imageResourceId);
                        GlideApp.with(getActivity())
                                .load(imageRef)
                                .into(coverImageView);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
