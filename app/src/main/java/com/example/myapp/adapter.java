package com.example.myapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    private File imagesfile;
    public adapter(File folderfile ){
        imagesfile = folderfile;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        File imagefile = imagesfile.listFiles()[i];
        Bitmap imageBitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
        holder.getImageView().setImageBitmap(imageBitmap);

    }

    @Override
    public int getItemCount() {
        return imagesfile.listFiles().length;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageview;


        public ViewHolder (View view){
            super(view);

            imageview = (ImageView)view.findViewById(R.id.imageView);

        }

        public ImageView getImageView(){
            return imageview;
        }
    }
}
