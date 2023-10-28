package com.moutamid.modsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fxn.stash.Stash;
import com.moutamid.modsadmin.databinding.ActivityDetailBinding;
import com.moutamid.modsadmin.models.ItemModel;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ItemModel itemModel = (ItemModel) Stash.getObject(Constants.ITEM, ItemModel.class);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(itemModel.getName());
        binding.toolbar.delete.setVisibility(View.VISIBLE);



    }
}