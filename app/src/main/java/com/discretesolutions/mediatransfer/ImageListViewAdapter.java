package com.discretesolutions.mediatransfer;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

import static android.media.ThumbnailUtils.createImageThumbnail;

public class ImageListViewAdapter extends BaseAdapter implements ThumbnailConfigurationInterface, selectItemsInterface {
    private final Context context;
    private final ArrayList<ImageItem> imgItems;
    ContentResolver cr;
    private int ThumbnailSize;

    public ImageListViewAdapter(Context context, ArrayList<ImageItem> items) {
        imgItems = items;
        this.context = context;
        cr = this.context.getContentResolver();
        ThumbnailSize = MediaStore.Images.Thumbnails.MINI_KIND;
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

        view = LayoutInflater.from(context).inflate(R.layout.imagelistitemlayout, viewGroup, false);

        ImageItem imt = (ImageItem) getItem(pos);
        ImageView imv = view.findViewById(R.id.imgVThumbnail);
        CheckBox chSelect = view.findViewById(R.id.chkTransfer);
        chSelect.setText(imt.getId());
        chSelect.setChecked((imt.getSelected()));
        Bitmap thumb = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        opt.inJustDecodeBounds = false;
        thumb = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), imt.getID(), MediaStore.Images.Thumbnails.MINI_KIND, opt);


        imv.setImageBitmap(thumb);
        chSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                imt.setSelected(!imt.selected);
            }
        });
        return view;
    }

    public ArrayList<ImageItem> getImageItemsList() {
        return this.imgItems;
    }

    @Override
    public void setThumbnailSize(int ThumbSize) {
        ThumbnailSize = ThumbSize;
    }

    @Override
    public void selectAllImages() {
        for (ImageItem imt : imgItems) {
            imt.setSelected(true);
        }
    }

    @Override
    public void selectNone() {
        for (ImageItem imt : imgItems) {
            imt.setSelected(false);
        }
    }
}
