package com.pokhare.wondersinindia;


import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;

public class CardFragment extends Fragment {

    private static final String LOG_TAG = "CardFragment";
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<WonderModel> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    DatabaseReference wondersOfIndiaDB;
    private CardsAdapter itemsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle("Wonderful places in India");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        itemsAdapter = new CardsAdapter(listitems, new CardsAdapter.CardsAdapterListener() {
            @Override
            public void directionsOnClick(View v, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + String.valueOf(listitems.get(position).getLatitude()) + "," + listitems.get(position).getLongitude()));
                startActivity(intent);
            }
        }, getContext());
        if (MyRecyclerView != null) {
            MyRecyclerView.setAdapter(itemsAdapter);
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshCards();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void refreshCards() {
        wondersOfIndiaDB.child("places").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("CardFragment","Data refreshed");
                listitems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final WonderModel model = ds.getValue(WonderModel.class);
                    listitems.add(model);
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initializeList() {
        listitems.clear();
        wondersOfIndiaDB = FirebaseDatabase.getInstance().getReference("WondersOfIndia");
        wondersOfIndiaDB.child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listitems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final WonderModel model = ds.getValue(WonderModel.class);
                    listitems.add(model);
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}