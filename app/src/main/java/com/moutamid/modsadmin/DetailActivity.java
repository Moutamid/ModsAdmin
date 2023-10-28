package com.moutamid.modsadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

        Constants.initDialog(this);

        ItemModel itemModel = (ItemModel) Stash.getObject(Constants.ITEM, ItemModel.class);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(itemModel.getName());
        binding.toolbar.delete.setVisibility(View.VISIBLE);

        binding.edit.setOnClickListener(v -> {
            startActivity(new Intent(this, UpdateActivity.class));
            finish();
        });

        binding.downloads.setText(itemModel.getDownloads());
        binding.rating.setText(""+itemModel.getRating());
        binding.views.setText(""+itemModel.getViews());
        binding.name.setText(itemModel.getName());
        binding.version.setText(itemModel.getVersion());
        binding.description.setText(itemModel.getDescription());
        Glide.with(this).load(itemModel.getImage()).into(binding.image);

        binding.toolbar.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Permanently")
                    .setMessage("Are you sure you want to delete?")
                    .setNegativeButton("No", ((dialog, which) -> dialog.dismiss()))
                    .setPositiveButton("Yes", ((dialog, which) -> {
                        dialog.dismiss();
                        Constants.showDialog();
                        Constants.databaseReference().child(itemModel.getType()).child(itemModel.getId()+"")
                                .removeValue().addOnSuccessListener(unused -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }))
                    .show();
        });
    }
}