package com.discretesolutions.mediatransfer;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

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
    String projection[] = {MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.DATE_ADDED};
    String select = MediaStore.Images.Media._ID+"";
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
        Log.v("item:",a.getId());
    }
}
public List<ImageItem> getImageItems(){

        return items;
}
}


