package com.discretesolutions.mediatransfer;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ListView;

import com.discretesolutions.mediatransfer.databinding.ActivityFullscreenBinding;

import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {



BluetoothAdapter bAdapter;
ActivityResultLauncher aResLauncher;
private Set<BluetoothDevice> pairedBtDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        MediaStorageUnit msu = new MediaStorageUnit();

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
        Log.v("Permission:","Granted");

            msu.queryMedia(this);
        }else{
            Log.v("Permission:","FAILED");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_MEDIA_LOCATION},1);
        }
        final ImageListViewAdapter adapter = new ImageListViewAdapter(this,msu.getImageItems());
        ListView lstv = (ListView) findViewById(R.id.lstItems);
        lstv.setAdapter(adapter);
        aResLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                Log.v("ActivityResult","some callback");

            }
        });
        initialize();



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }
private void initBluetooth(){

   // Intent btTurnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
 //   aResLauncher.launch(btTurnOn);
   // pairedBtDevices = bAdapter.getBondedDevices();

}

    public void initialize(){
         Intent btTurnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ActivityResult aResult = new ActivityResult(0,btTurnOn);
        if (btTurnOn.resolveActivity(getPackageManager()) != null) {
            aResLauncher.launch(btTurnOn);
        }
        else{
            Log.v("BT","could not resolve package manager");
        }
        // aResLauncher.launch(aResult);
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedBtDevices = bAdapter.getBondedDevices();
        for(BluetoothDevice bt : pairedBtDevices){
            Log.v("BTDevice:",bt.getName());
        }
        /*
        pairedBtDevices = bAdapter.getBondedDevices();
        for(BluetoothDevice bt : pairedBtDevices){
            Log.v("BTDevice:",bt.getName());
        }

         */
       // ActivityResultContracts.StartActivityForResult(0,aResult);

}

    private void show() {

    }

}