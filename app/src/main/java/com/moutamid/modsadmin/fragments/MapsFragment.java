package com.moutamid.modsadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.modsadmin.Constants;
import com.moutamid.modsadmin.R;
import com.moutamid.modsadmin.adapters.ItemAdapter;
import com.moutamid.modsadmin.databinding.FragmentMapsBinding;
import com.moutamid.modsadmin.databinding.FragmentMixedBinding;
import com.moutamid.modsadmin.models.ItemModel;

import java.util.ArrayList;

public class MapsFragment extends Fragment {
    FragmentMapsBinding binding;
    ArrayList<ItemModel> list;

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapsBinding.inflate(getLayoutInflater(), container, false);

        binding.recy.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recy.setHasFixedSize(false);

        list = new ArrayList<>();

        Constants.databaseReference().child(Constants.MAP)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                                list.add(itemModel);
                            }

                            ItemAdapter adapter = new ItemAdapter(binding.getRoot().getContext(), list);
                            binding.recy.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return binding.getRoot();
    }
}