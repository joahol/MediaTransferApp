package com.discretesolutions.mediatransfer;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MediaStorageUnit {

/*
@Description query the mediastore object for mediafiles of given type
@Parameter Type of media to query.
 */
ArrayList<ImageItem> items;
    public MediaStorageUnit(){}

public void queryMedia(Context context) {
    items = new ArrayList<ImageItem>();
    String projection[] = {
            MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.Thumbnails.DATA, MediaStore.Images.ImageColumns.SIZE
    };
    String select = MediaStore.Images.Media._ID + "";
    String selectArgs[] = {};
    String orderBy = MediaStore.Images.Media.DATE_ADDED;

    //requestPermissions(context,new String[], {Manifest.permission.READ_EXTERNAL_STORAGE},)
    Cursor cursor = (context.getApplicationContext().getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, select, selectArgs, orderBy));

    String[] names = cursor.getColumnNames();

    for(String s: names){
        Log.v("name:",s);
    }
    while (cursor.moveToNext()){
        ImageItem a = new ImageItem(cursor.getString(1));
        a.setThumbPath(cursor.getString(2));
        items.add(a);
        Log.v("item:", a.getId() + " " + a.getThumbPath() + " " + cursor.getString(3));
    }
}

    public ArrayList<ImageItem> getImageItems() {

        return items;
    }

    public Bitmap loadImage(String path) {
        Bitmap imgLoaded = BitmapFactory.decodeFile(path);
        return imgLoaded;
    }

    /*
    @description load bitmap from file and convert the file to a byte array.
    @return byte[] representation of bitmap
     */
    public byte[] loadImageAsByteArray(String path) {
        Bitmap imgLoaded = loadImage(path);
        ByteBuffer buffer = ByteBuffer.allocate(imgLoaded.getByteCount());
        imgLoaded.copyPixelsToBuffer(buffer);
        return buffer.array();
    }
}


