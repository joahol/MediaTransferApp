package com.discretesolutions.mediatransfer;

import android.content.Context;

import java.util.Stack;
/*
Beskrivelse:
Klasse for å håndtere kø over filer som skal overføres.
Todo:
-Vurder om Context har noe i denne klassen å gjør.
 */

public class TransferModel {
    private final Context context;

    private final Stack<String> imagePaths;

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
