package com.discretesolutions.mediatransfer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageListViewAdapter extends BaseAdapter {
private Context context;
private ArrayList<ImageItem> imgItems;

    @Override
    public int getCount() {
        return imgItems.size();
    }

    @Override
    public Object getItem(int i) {
        return imgItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
    if(view ==null){
        view = LayoutInflater.from(context).inflate(R.layout.imagelistitemlayout, viewGroup,false);
        ImageItem imt = (ImageItem) getItem(pos);
        ImageView imv = (ImageView)view.findViewById(R.id.imageView);
        CheckBox chSelect = (CheckBox)view.findViewById(R.id.chkTransfer);
        chSelect.setText(imt.getId());
        imv.setImageBitmap(imt.getThumbnail());
    }
        return view;
    }
}
