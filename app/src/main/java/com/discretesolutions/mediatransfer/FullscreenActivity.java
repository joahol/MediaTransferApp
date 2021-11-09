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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.discretesolutions.mediatransfer.databinding.ActivityFullscreenBinding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


Spinner modeSpinner;
Spinner selectTypeSpinner;
BluetoothAdapter bAdapter;
ActivityResultLauncher aResLauncher;
private Button btnConnect;
private Button btnTransfer;
private Set<BluetoothDevice> pairedBtDevices;
private int SelectMode=0;
private int DISPLAYMODE=0;

//Constants for select and display modes
private final int SELECTMODE_ALL=1;
private final int SELECTMODE_SELECTED=2;
private final int DISPLAYMODE_SMALLTHUMB=1;
private final int DISPLAYMODE_LARGETHUMB=2;
private final int DISPLAYMODE_LIST=3;


private boolean avoid = true;//WHEN DEBUGGING IN EMULATOR SET THIS TO TRUE, DUE TO MISSING BLUETOOTH IN EMULATOR
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        MediaStorageUnit msu = new MediaStorageUnit();
        //Initialize Buttons
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnTransfer =(Button)findViewById(R.id.btnTransfer);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTSelectorDialog btsd = new BTSelectorDialog();
                btsd.show(getSupportFragmentManager(), "BTSelectorDialog");
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast availBTDev = Toast.makeText(getApplicationContext(),"Bluetooth", 0x33);
                availBTDev.setText("Start transfer");
                availBTDev.show();
            }
        });

        //Initialize spinners
        modeSpinner = (Spinner)findViewById(R.id.spinMode);
        selectTypeSpinner=(Spinner)findViewById(R.id.spinSelect);
        ArrayAdapter<CharSequence> arrAdaptMode = ArrayAdapter.createFromResource(this,R.array.listmode,R.layout.support_simple_spinner_dropdown_item);
        arrAdaptMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(arrAdaptMode);
        ArrayAdapter<CharSequence> arrAdaptSelect = ArrayAdapter.createFromResource(this, R.array.spinnoptions, R.layout.support_simple_spinner_dropdown_item);
        arrAdaptSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTypeSpinner.setAdapter(arrAdaptSelect);
        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            String selected = adapterView.getItemAtPosition(pos).toString();

                if (selected.equals("Selected")) {
                    SelectMode = SELECTMODE_SELECTED;
                } else if (selected.equals("All")) {
                    SelectMode = SELECTMODE_ALL;
                }
                Log.v("Selected ",selected+" "+SelectMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            String selected = adapterView.getItemAtPosition(pos).toString();
            Log.v("Selected ",selected);

                if(selected.equals("Small thumbnail")){
                    DISPLAYMODE=DISPLAYMODE_SMALLTHUMB;

                }
                else if(selected.equals("Large thumbnail")) {
                    DISPLAYMODE=DISPLAYMODE_LARGETHUMB;

                }
                else if(selected.equals("List")){
                    DISPLAYMODE=DISPLAYMODE_LIST;
                }

                Log.v("Selected ",selected+" "+DISPLAYMODE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//Check permisssions for reading from external storage
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission:", "Granted");

            msu.queryMedia(this);
        } else {
            Log.v("Permission:", "FAILED");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);
        }
//Fill the listview with images
        final ImageListViewAdapter adapter = new ImageListViewAdapter(this, msu.getImageItems());
        ListView lstv = (ListView) findViewById(R.id.lstItems);
        lstv.setAdapter(adapter);

        //Bluetooth CODE section
        if(!avoid) {
            aResLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
        @Override
        public void onActivityResult(Object result) {
            Log.v("ActivityResult", "some callback");

        }
    });
    initialize();
}
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    //Init bluetooth fetch available devices
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
        ArrayList<String> btNameDev = new ArrayList<String>();
        for(BluetoothDevice bt : pairedBtDevices){
            btNameDev.add(bt.getName());
            Log.v("BTDevice:", bt.getName());
        }
        // ListView lvBTDevices = new ListView();
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