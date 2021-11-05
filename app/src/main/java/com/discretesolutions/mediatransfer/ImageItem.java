package com.discretesolutions.mediatransfer;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageItem {
    String id;
    Bitmap thumbnail;
    boolean selected;
    Uri uri;
    public ImageItem(String iD){
    id = iD;
    }


    public void setThumbnail(Bitmap thumb){
        thumbnail = thumb;
    }
    public void setSelected(boolean select){
        selected = select;
    }
    public void setUri(Uri current){
        uri = current;
    }
    public Bitmap getThumbnail(){
        return thumbnail;
    }
    public boolean getSelected(){
        return selected;
    }
    public Uri getUri(){
        return uri;
    }
    public String getId(){
        return id;
    }
}
