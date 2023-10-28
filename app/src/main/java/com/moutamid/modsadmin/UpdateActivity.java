package com.moutamid.modsadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.modsadmin.databinding.ActivityUpdateBinding;
import com.moutamid.modsadmin.models.ItemModel;

import java.util.Random;
import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    ItemModel itemModel;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);
        itemModel = (ItemModel) Stash.getObject(Constants.ITEM, ItemModel.class);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Update");

        updateUI();

        binding.AddPhotoLayout.setOnClickListener(v -> getImage());
        binding.AddPhotoLayoutImage.setOnClickListener(v -> getImage());

        binding.add.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                if (imageUri != null) {
                    uploadImage();
                } else {
                    uploadDate(itemModel.getImage());
                }
            }
        });

    }

    private ItemModel getModel(String Image) {
        return new ItemModel(
                Integer.parseInt(binding.appId.getEditText().getText().toString()), itemModel.getId(),
                binding.archive.getEditText().getText().toString(),
                binding.description.getEditText().getText().toString(),
                itemModel.getDownloads(),
                Image,
                binding.name.getEditText().getText().toString(),
                Double.parseDouble(binding.rating.getEditText().getText().toString()),
                Integer.parseInt(binding.sort.getEditText().getText().toString()),
                Integer.parseInt(binding.unSort.getEditText().getText().toString()),
                getType(),
                binding.version.getEditText().getText().toString(),
                itemModel.getViews()
        );
    }

    private void uploadDate(String image) {
        ItemModel itemModel = getModel(image);
        Constants.databaseReference().child(itemModel.getType()).child(String.valueOf(itemModel.getId()))
                .setValue(itemModel).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImage() {
        Constants.storageReference("images").child(UUID.randomUUID().toString())
                .putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadDate(uri.toString());
                    });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, ""), 1);
    }

    private boolean valid() {
        return true;
    }

    private void updateUI() {
        binding.AddPhotoLayout.setVisibility(View.GONE);
        binding.AddPhotoLayoutImage.setVisibility(View.VISIBLE);
        Glide.with(this).load(itemModel.getImage()).into(binding.image);
        binding.appId.getEditText().setText(itemModel.getApp_id() + "");
        binding.name.getEditText().setText(itemModel.getName());
        binding.description.getEditText().setText(itemModel.getDescription());
        binding.version.getEditText().setText(itemModel.getVersion());
        binding.archive.getEditText().setText(itemModel.getArchive());
        binding.rating.getEditText().setText(itemModel.getRating() + "");
        binding.sort.getEditText().setText(itemModel.getSort() + "");
        binding.unSort.getEditText().setText(itemModel.getUnSort() + "");

        if (itemModel.getType().equals(Constants.MIX)) {
            binding.radio3.setChecked(true);
        } else if (itemModel.getType().equals(Constants.MAP)) {
            binding.radio2.setChecked(true);
        } else if (itemModel.getType().equals(Constants.MOD)) {
            binding.radio1.setChecked(true);
        }
    }

    private String getType() {
        if (binding.radio1.isChecked()) {
            return Constants.MOD;
        } else if (binding.radio2.isChecked()) {
            return Constants.MAP;
        } else if (binding.radio3.isChecked()) {
            return Constants.MIX;
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            binding.AddPhotoLayout.setVisibility(View.GONE);
            binding.AddPhotoLayoutImage.setVisibility(View.VISIBLE);
            imageUri = data.getData();
            binding.image.setImageURI(imageUri);
        }
    }

}