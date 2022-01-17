package com.discretesolutions.mediatransfer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.media.ThumbnailUtils.createAudioThumbnail;
import static android.media.ThumbnailUtils.createImageThumbnail;

public class ImageGridViewAdapter extends BaseAdapter implements ThumbnailConfigurationInterface, selectItemsInterface {
    ArrayList<ImageItem> imgItems;
    ContentResolver cr;
    Context contex;
    int ThumbnailSize;
    int screenwidth = 0;
    int screenheight = 0;
    int imgWidth;


    public ImageGridViewAdapter(@NonNull Context context, ArrayList<ImageItem> resource) {
        imgItems = resource;
        this.contex = context;
        cr = this.contex.getContentResolver();
        ThumbnailSize = MediaStore.Images.Thumbnails.MINI_KIND;

        DisplayMetrics dpm = new DisplayMetrics();
        screenwidth = dpm.widthPixels;
        imgWidth = (screenwidth / 5);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(contex).inflate(R.layout.imagegriditemlayout, viewGroup, false);
        //view.setMinimumWidth(imgWidth -50);
        ImageItem imt = (ImageItem) getItem(i);
        ImageView imv = view.findViewById(R.id.imgVThumbnail);

        CheckBox chSelect = view.findViewById(R.id.chkTransfer);
        chSelect.setText(imt.getId());
        chSelect.setChecked((imt.getSelected()));
        Bitmap thumb = null;

        CancellationSignal cs = new CancellationSignal();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        opt.inJustDecodeBounds = false;
        opt.outWidth = imgWidth;
        thumb = MediaStore.Images.Thumbnails.getThumbnail(contex.getContentResolver(), imt.getID(), MediaStore.Images.Thumbnails.MINI_KIND, opt);

        imv.setImageBitmap(thumb);
        chSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                imt.setSelected(!imt.selected);
            }
        });
        return view;
    }

    @Override
    public void setThumbnailSize(int ThumbSize) {
        ThumbnailSize = ThumbSize;
    }

    @Override
    public void selectAllImages() {

        for (int i = 0; i < imgItems.size(); i++) {
            imgItems.get(i).setSelected(true);
        }
    }

    @Override
    public void selectNone() {
        for (int i = 0; i < imgItems.size(); i++) {
            imgItems.get(i).setSelected(false);
        }
    }

    @Override
    public ArrayList<ImageItem> getImageItemsList() {
        return imgItems;
    }
}
