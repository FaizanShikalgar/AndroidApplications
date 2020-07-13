package com.example.liker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<ImageViewHOlder> {

    private Context context;
    private List<ModelImage> imageList;
    public MyAdapter(Context context, List<ModelImage> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_item,parent,false);
        return new ImageViewHOlder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHOlder holder, int position) {

        holder.textView.setText(" "+imageList.get(position).getLikes()+" Likes");
        Glide.with(context).load(imageList.get(position).getImageurl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
class ImageViewHOlder extends RecyclerView.ViewHolder{

    ImageView imageView;
    ProgressBar progressBar;
    TextView textView;
    public ImageViewHOlder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.likeCount);
         progressBar = itemView.findViewById(R.id.progress);
        imageView = itemView.findViewById(R.id.imageView12);
    }
}