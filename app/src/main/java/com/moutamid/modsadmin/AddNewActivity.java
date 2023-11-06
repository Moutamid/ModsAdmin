package com.moutamid.modsadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.moutamid.modsadmin.databinding.ActivityAddNewBinding;
import com.moutamid.modsadmin.models.ItemModel;
import com.moutamid.modsadmin.models.Rating;

import java.util.Random;
import java.util.UUID;

public class AddNewActivity extends AppCompatActivity {
    ActivityAddNewBinding binding;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Add New");

        binding.AddPhotoLayout.setOnClickListener(v -> getImage());
        binding.AddPhotoLayoutImage.setOnClickListener(v -> getImage());

        binding.add.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.storageReference("images").child(UUID.randomUUID().toString())
                        .putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                ItemModel itemModel = getModel(uri.toString());
                                Constants.databaseReference().child(itemModel.getType()).child(String.valueOf(itemModel.getId()))
                                        .setValue(itemModel).addOnSuccessListener(unused -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }).addOnFailureListener(e -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            });
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private ItemModel getModel(String Image) {
        Random random = new Random();
        int min = 0;
        int max = 999999;
        int id = random.nextInt((max - min) + 1) + min;
        return new ItemModel(
                Integer.parseInt(binding.appId.getEditText().getText().toString()), id,
                binding.archive.getEditText().getText().toString(),
                binding.description.getEditText().getText().toString(),
                binding.downloads.getEditText().getText().toString(),
                Image,
                binding.name.getEditText().getText().toString(),
                Double.parseDouble(binding.rating.getEditText().getText().toString()),
                0,0,
                getType(),
                binding.version.getEditText().getText().toString(),
                Integer.parseInt(binding.views.getEditText().getText().toString())
        );
    }

    private String getType() {
        if (binding.radio1.isChecked()) {
            return Constants.MOD;
        } else if (binding.radio2.isChecked()) {
            return Constants.MAP;
        }
        return "";
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, ""), 1);
    }

    private boolean valid() {
        return true;
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