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
import com.moutamid.modsadmin.databinding.FragmentMixedBinding;
import com.moutamid.modsadmin.models.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MixedFragment extends Fragment {
    FragmentMixedBinding binding;
    ArrayList<ItemModel> list, map, mod;

    public MixedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMixedBinding.inflate(getLayoutInflater(), container, false);

        binding.recy.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recy.setHasFixedSize(false);

        list = new ArrayList<>();
        map = new ArrayList<>();
        mod = new ArrayList<>();

        Constants.databaseReference().child(Constants.MAP)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            map.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                                map.add(itemModel);
                            }
                        }
                        Constants.databaseReference().child(Constants.MOD)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            mod.clear();
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                                                mod.add(itemModel);
                                            }
                                        }
                                        list.clear();
                                        list.addAll(mod);
                                        list.addAll(map);
                                        Collections.shuffle(list);
                                        ItemAdapter adapter = new ItemAdapter(binding.getRoot().getContext(), list);
                                        binding.recy.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return binding.getRoot();
    }
}