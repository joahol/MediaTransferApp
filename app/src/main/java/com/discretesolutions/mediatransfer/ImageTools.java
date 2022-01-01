package com.discretesolutions.mediatransfer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/*
    @description ImageTools, is responsible to handle loading images from the device and provides
    methods to convert image to transferable formats.

 */
public class ImageTools {

    private Context context;

    /*
    @description Constructor for imagetools
    @param Context of the calling app, needed for handling image references in store.
     */
    public ImageTools(Context context) {
        this.context = context;
    }

/*
    @description loading image from resources as a byte array
    @return  byte[] representation of the file.
 */

    public byte[] getImage(String path) {

        int resid = context.getResources().getIdentifier("homer", "drawable", context.getPackageName());
        InputStream is = context.getResources().openRawResource(resid);
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        int next = 0;
        try {
            next = is.read(bytes);
            while (next != -1) {
                bais.write(bytes, 0, next);
                next = is.read(bytes);
                Log.v("B:", String.valueOf(next));
            }
            //for( int next=0; (next = is.read(bytes)) != -1;){
            //      bais.write(bytes,0,next);
            //}

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bais.toByteArray();
    }

    /*
        @description Convert a bitmap image to byte[] representation
        @param Bitmap source, the image to convert to byte[]
        @return the converted byte[] representation
     */
    public byte[] BitmapToByteArray(Bitmap source) {
        long size = source.getHeight() * source.getRowBytes();

        ByteBuffer buffer = ByteBuffer.allocate((int) size);

        source.copyPixelsToBuffer(buffer);
        byte[] target = buffer.array();
        return target;
    }

    public String getFileName(String path) {
        int i = path.lastIndexOf('/');
        String filename = path.substring(i);
        return filename;

    }
}
