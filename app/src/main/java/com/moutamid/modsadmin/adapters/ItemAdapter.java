package com.moutamid.modsadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.modsadmin.Constants;
import com.moutamid.modsadmin.DetailActivity;
import com.moutamid.modsadmin.R;
import com.moutamid.modsadmin.models.ItemModel;
import com.moutamid.modsadmin.models.Rating;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemVH> {

    Context context;
    ArrayList<ItemModel> list;

    public ItemAdapter(Context context, ArrayList<ItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemVH(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemVH holder, int position) {
        ItemModel itemModel = list.get(holder.getAdapterPosition());
        holder.downloads.setText(itemModel.getDownloads());
        Rating rate = itemModel.getRating();
        double rating = (rate.getStar1() + rate.getStar2() + rate.getStar3() + rate.getStar4() + rate.getStar5()) / 5;
        holder.rating.setText(""+rating);
        holder.views.setText(""+itemModel.getViews());
        holder.title.setText(itemModel.getName());
        holder.version.setText(itemModel.getVersion());
        Glide.with(context).load(itemModel.getImage()).into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.ITEM, itemModel);
            context.startActivity(new Intent(context, DetailActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemVH extends RecyclerView.ViewHolder{
        TextView downloads;
        TextView rating;
        ImageView thumbnail;
        TextView title;
        TextView version;
        TextView views;
        public ItemVH(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.views = (TextView) itemView.findViewById(R.id.views);
            this.rating = (TextView) itemView.findViewById(R.id.rating);
            this.version = (TextView) itemView.findViewById(R.id.version);
            this.downloads = (TextView) itemView.findViewById(R.id.downloads);
        }
    }

}
