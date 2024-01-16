package com.example.passwordmanager.features;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordmanager.R;

import java.security.InvalidAlgorithmParameterException;
import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ItemViewHolder>{
    private List<Site> siteList;
    private FingerprintManagerCompat fingerprintManager;
    private OnSiteClickListener listener;

    public SiteAdapter(List<Site> siteList, FingerprintManagerCompat fingerprintManager, OnSiteClickListener listener) {
        this.siteList = siteList;
        this.fingerprintManager = fingerprintManager;
        this.listener = listener;
    }

    public void setFilteredList(List<Site> filteredList){
        this.siteList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SiteAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.site, parent, false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SiteAdapter.ItemViewHolder holder, int position) {
        Site site = siteList.get(position);
        holder.nameTextView.setText(site.getName());
        holder.passwordTextView.setText("password: •••••");
        holder.usernameTextView.setText("username: ");
        holder.usernameTextView.append(site.getUsername());

        holder.itemView.setOnClickListener(v -> {
            try {
                holder.passwordTextView.setText("password: ");
                holder.passwordTextView.append(site.getPassword());
                listener.onSiteClick(site);
                holder.itemView.setOnClickListener(v1->{
                    holder.passwordTextView.setText("password: •••••");
                });
            } catch (InvalidAlgorithmParameterException e) {
                throw new RuntimeException(e);
            }
        });

        int resourceId = holder.itemView.getContext().getResources()
                .getIdentifier(site.getName().toLowerCase(), "drawable", holder.imageView.getContext().getPackageName());

        if(resourceId != 0){
            holder.imageView.setImageResource(resourceId);
        } else {
            holder.imageView.setImageResource(R.drawable.resource_default);
        }
    }
    @Override
    public int getItemCount() {
        return siteList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView nameTextView;
        public TextView passwordTextView;
        public TextView usernameTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.item_name);
            passwordTextView = itemView.findViewById(R.id.site_password);
            usernameTextView = itemView.findViewById(R.id.site_username);
        }
    }

    public interface OnSiteClickListener{
        void onSiteClick(Site site) throws InvalidAlgorithmParameterException;
    }

}
