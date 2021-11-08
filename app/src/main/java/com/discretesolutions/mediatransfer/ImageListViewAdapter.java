package com.discretesolutions.mediatransfer;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageListViewAdapter extends BaseAdapter {
private Context context;
private ArrayList<ImageItem> imgItems;
ContentResolver cr;
public ImageListViewAdapter(Context context, ArrayList<ImageItem> items){
    imgItems = items;
    this.context = context;
    cr = this.context.getContentResolver();
}
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
        return i;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
    //if(view ==null){
        view = LayoutInflater.from(context).inflate(R.layout.imagelistitemlayout, viewGroup,false);
   // }
        ImageItem imt = (ImageItem) getItem(pos);
        ImageView imv = (ImageView)view.findViewById(R.id.imgVThumbnail);
        CheckBox chSelect = (CheckBox)view.findViewById(R.id.chkTransfer);
        chSelect.setText(imt.getId());
        Bitmap thumb = ThumbnailUtils.createImageThumbnail(imt.getThumbPath(),MediaStore.Images.Thumbnails.MICRO_KIND);
                //MediaStore.Images.Thumbnails.getThumbnail(cr,imt.getID(),MediaStore.Images.Thumbnails.MICRO_KIND,null);
        imv.setImageBitmap(thumb);


        return view;
    }
}
