package com.discretesolutions.mediatransfer;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

import static android.media.ThumbnailUtils.createImageThumbnail;

public class ImageListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ImageItem> imgItems;
    ContentResolver cr;

    public ImageListViewAdapter(Context context, ArrayList<ImageItem> items) {
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        //if(view ==null){
        view = LayoutInflater.from(context).inflate(R.layout.imagelistitemlayout, viewGroup, false);
        // }
        ImageItem imt = (ImageItem) getItem(pos);
        ImageView imv = (ImageView) view.findViewById(R.id.imgVThumbnail);
        CheckBox chSelect = (CheckBox) view.findViewById(R.id.chkTransfer);
        chSelect.setText(imt.getId());
        Bitmap thumb = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            thumb = createImageThumbnail(imt.getThumbPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
        }/*else{KREVER API VERSJON 29, KAN UNGÅS VED Å HEVE API min til 29, nå er dette
        satt til api level 23}*/
        //MediaStore.Images.Thumbnails.getThumbnail(cr,imt.getID(),MediaStore.Images.Thumbnails.MICRO_KIND,null);
        imv.setImageBitmap(thumb);

        chSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (imt.selected) {
                    imt.setSelected(false);
                } else {
                    imt.setSelected(true);
                }
            }
        });
        return view;
    }

    public ArrayList<ImageItem> getImageItemsList() {
        return this.imgItems;
    }
}
