package com.discretesolutions.mediatransfer;
//responsible for communicating with media storage

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MediaStorageUnit {


/*
@Description query the mediastore object for mediafiles of given type
@Parameter Type of media to query.
 */
public void queryMedia(Context context) {
    String projection[] = {};
    String select = "select *";
    String selectArgs[] = {};
    String orderBy = "";

    Cursor cursor = ((Activity) context).getApplicationContext().getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, select, selectArgs, orderBy);

}
}
