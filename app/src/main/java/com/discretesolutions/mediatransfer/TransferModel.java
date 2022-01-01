package com.discretesolutions.mediatransfer;

import android.content.Context;

import java.util.Stack;

public class TransferModel {
    private Context context;

    private Stack<String> imagePaths;

    public TransferModel(Context context) {
        this.context = context;
        imagePaths = new Stack<>();
    }

    public void pushImagePath(String path) {
        imagePaths.push(path);
    }

    //Returns the next imagepath if the stack is not empty
    public String popImagePath() {
        if (!imagePaths.isEmpty()) {
            return imagePaths.pop();
        }
        return null;
    }

    private boolean isEmpty() {
        return imagePaths.isEmpty();
    }

    public Context getContext() {
        return this.context;
    }

}
