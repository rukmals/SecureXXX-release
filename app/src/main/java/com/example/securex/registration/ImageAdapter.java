package com.example.securex.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.example.securex.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<Integer> imageID;
    Context ctx;
    int Size;

    public ImageAdapter(Context ctx, int Size){
        this.ctx=ctx;
        this.Size=Size;
        imageID = new ArrayList<>();
        LoadImages();

    }

    @Override
    public int getCount() {
        return imageID.size() ;
    }

    @Override
    public Object getItem(int position) {
        return imageID.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridview = convertView;
        if(gridview==null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridview = inflater.inflate(R.layout.custom_image_layout,null);
        }

        ImageView i = (ImageView) gridview.findViewById(R.id.gridimage);
        i.setImageResource(imageID.get(position));

        return gridview;

    }

    public void LoadImages(){
        if(Size==4){
            imageID.add(R.drawable.apple);
            imageID.add(R.drawable.banana);
            imageID.add(R.drawable.orange);
            imageID.add(R.drawable.mango);

        }
        else if(Size==6){
            imageID.add(R.drawable.apple);
            imageID.add(R.drawable.grape);
            imageID.add(R.drawable.cherry);
            imageID.add(R.drawable.banana);
            imageID.add(R.drawable.orange);
            imageID.add(R.drawable.mango);


        }
        else if(Size==8){
            imageID.add(R.drawable.stawberry);
            imageID.add(R.drawable.apple);
            imageID.add(R.drawable.grape);
            imageID.add(R.drawable.cherry);
            imageID.add(R.drawable.banana);
            imageID.add(R.drawable.orange);
            imageID.add(R.drawable.mango);
            imageID.add(R.drawable.melon);


        }

    }
    public ArrayList<Integer> getImageID(){
        return imageID;
    }



}
