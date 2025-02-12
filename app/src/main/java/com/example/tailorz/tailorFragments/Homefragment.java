package com.example.tailorz.tailorFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.tailorAdapters.TailorDesignAdapter;
import com.example.tailorz.tailorModels.TailorDesignModel;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Homefragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    TailorDesignAdapter tailorDesignAdapter;
    ArrayList<TailorDesignModel> list;
    Prefs prefs;

    public Homefragment() {
        // Required empty public constructor
    }


    public static Homefragment newInstance(String param1, String param2) {
        return new Homefragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_homefragment, container, false);

        prefs = new Prefs(requireActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.designRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        tailorDesignAdapter = new TailorDesignAdapter(getContext(), list);
        recyclerView.setAdapter(tailorDesignAdapter);
        GetDataFromDatabase();

        return view;
    }

    public void GetDataFromDatabase() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                database = FirebaseDatabase.getInstance().getReference("Design");
                DatabaseReference tailorRef = database.child(prefs.getUserName());

                tailorRef.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            TailorDesignModel tailorDesignModel = dataSnapshot.getValue(TailorDesignModel.class);
                            list.add(tailorDesignModel);

                        }
                        tailorDesignAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        },300);
    }
}