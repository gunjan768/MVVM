package com.example.mvvm.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm.R;
import com.example.mvvm.models.NicePlace;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    private List<NicePlace> nicePlaces;
    private Context context;

    public RecyclerViewAdapter(Context context, List<NicePlace> nicePlaces)
    {
        this.context = context;
        this.nicePlaces = nicePlaces;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        // Set the name of the 'NicePlace'
        holder.mName.setText(nicePlaces.get(position).getTitle());

        // Set the image
        RequestOptions defaultOptions = new RequestOptions().error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(defaultOptions)
                .load(nicePlaces.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return nicePlaces.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView mImage;
        private TextView mName;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mImage = itemView.findViewById(R.id.circle_image_view);
            mName = itemView.findViewById(R.id.image_name);
        }
    }
}