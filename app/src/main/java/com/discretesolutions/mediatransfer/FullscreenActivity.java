package com.discretesolutions.mediatransfer;

import android.Manifest;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.ViewStub;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;

import java.util.Set;

public class FullscreenActivity extends AppCompatActivity implements BTSelectorDialog.BluetoothDialogSelector {


    private static final int ACECCESS_BLUETOOTH_SHARE = 0;
    GridView gridView;
    ListView listView;
    ViewStub gridStub;
    ViewStub listStub;
    Spinner modeSpinner;
    Spinner selectTypeSpinner;
    BluetoothAdapter bAdapter;
    ActivityResultLauncher aResLauncher;
    private Button btnTransfer;
    private Set<BluetoothDevice> pairedBtDevices;
    private int SelectMode = 0;
    private String selectedDeviceName;
    private int DISPLAYMODE = 0;
    BaseAdapter adapter;
    //Constants for select and display modes
    private final int SELECTMODE_ALL = 1;
    private final int SELECTMODE_SELECTED = 2;
    private final int DISPLAYMODE_SMALLTHUMB = 1;
    private final int DISPLAYMODE_LARGETHUMB = 2;
    private final int DISPLAYMODE_LIST = 3;

    TransferModel tModel;
    /*
        private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if(isGranted){}else{
                        //Forklar bruker hvorfor tilatelsene trengs
                    }
                });*/
    private final boolean avoid = false;//WHEN DEBUGGING IN EMULATOR SET THIS TO TRUE, DUE TO MISSING BLUETOOTH IN EMULATOR

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        gridStub = findViewById(R.id.stub_grid);
        listStub = findViewById(R.id.stub_list);
        gridStub.inflate();
        listStub.inflate();
        listView = findViewById(R.id.lstItems);
        gridView = findViewById(R.id.gridView);


        MediaStorageUnit msu = new MediaStorageUnit();
        tModel = new TransferModel(this);
        //Initialize Buttons
        // btnConnect = findViewById(R.id.btnConnect);
        btnTransfer = findViewById(R.id.btnTransfer);
       /* btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                //Find all available bluetooth devices
                ArrayList<String> btArr = new ArrayList<String>();
                for (BluetoothDevice Btd : pairedBtDevices) {
                    btArr.add(Btd.getName());
                }
                bundle.putStringArrayList("btdevices", btArr);
                //Show a list of all available bluetooth devices.
                FragmentManager fm = getSupportFragmentManager();
                BTSelectorDialog btsd = BTSelectorDialog.newInstance("Bluetooth select", bundle);
                btsd.show(fm, BTSelectorDialog.TAG);
            }
        });
*/
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast availBTDev = Toast.makeText(getApplicationContext(), "Bluetooth", Toast.LENGTH_LONG);
                availBTDev.setText("Start transfer: ");
                availBTDev.show();
                BluetoothDevice btd = null;

                for (BluetoothDevice bt : pairedBtDevices) {
                    if (bt.getName().equals(selectedDeviceName)) {
                        btd = bt;
                        Log.v("BT", "FoundDevice");
                        break;
                    } else {
                        Log.v("Device: ", bt.getName() + " " + selectedDeviceName);
                    }
                }
                if (!pairedBtDevices.isEmpty()) {
                    btd = pairedBtDevices.iterator().next();
                }
                //fetch a list of selected media files
                //    ArrayList<ImageItem> imgList = ((selectItemsInterface)adapter).getImageItemsList();

                ArrayList<String> selected = new ArrayList<String>();
                for (ImageItem itm : ((selectItemsInterface) adapter).getImageItemsList()) {
                    if (itm.selected) {
                        selected.add(itm.thumbPath);
                        Log.v("add to que ", itm.thumbPath);
                    }
                }
                BTService.sendFile3(btd, selected, getApplicationContext());
            }
        });

        //Initialize select mode spinner
        modeSpinner = findViewById(R.id.spinMode);
        selectTypeSpinner = findViewById(R.id.spinSelect);

        ArrayAdapter<CharSequence> arrAdaptMode = ArrayAdapter.createFromResource(this, R.array.listmode, R.layout.support_simple_spinner_dropdown_item);
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
                    ((selectItemsInterface) adapter).selectNone();
                    adapter.notifyDataSetChanged();
                    Log.v("Selected ", selected + " " + SelectMode);
                } else if (selected.equals("All")) {
                    SelectMode = SELECTMODE_ALL;
                    ((selectItemsInterface) adapter).selectAllImages();
                    adapter.notifyDataSetChanged();
                    Log.v("Selected ", selected + " " + SelectMode);
                }
                Log.v("Selected ", selected + " " + SelectMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*
        Initialize selection of display mode
         */
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                String selected = adapterView.getItemAtPosition(pos).toString();
                Log.v("Selected ", selected);

                if (selected.equals("Small thumbnail")) {
                    DISPLAYMODE = DISPLAYMODE_SMALLTHUMB;
                } else if (selected.equals("Large thumbnail")) {
                    DISPLAYMODE=DISPLAYMODE_LARGETHUMB;
                } else if (selected.equals("List")) {
                    DISPLAYMODE = DISPLAYMODE_LIST;
                    //Fill the listview with images
                    listView.setVisibility(View.VISIBLE);
                    listStub.setVisibility(View.VISIBLE);
                    ImageListViewAdapter adapter = new ImageListViewAdapter(getApplicationContext(), msu.getImageItems());
                    listView.setAdapter(adapter);
                    gridStub.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    //listView.setAdapter(adapter);
                } else {// if(selected.equals("Grid")){
                    listView.setVisibility(View.GONE);
                    listStub.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    gridStub.setVisibility(View.VISIBLE);
                    ImageGridViewAdapter gridAdapter = new ImageGridViewAdapter(getApplicationContext(), msu.getImageItems());
                    gridView.setAdapter(gridAdapter);
                }

                Log.v("Selected ", selected + " " + DISPLAYMODE);
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
        adapter = new ImageListViewAdapter(this, msu.getImageItems());
        listView = findViewById(R.id.lstItems);
        listView.setAdapter(adapter);

        //Bluetooth CODE section
        if (!avoid) {
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
    public void initialize() {
        Intent btTurnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ActivityResult aResult = new ActivityResult(0, btTurnOn);
        if (btTurnOn.resolveActivity(getPackageManager()) != null) {
            aResLauncher.launch(btTurnOn);
        } else {
            Log.v("BT", "Could not resolve activity package manager.");
        }

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedBtDevices = bAdapter.getBondedDevices();
        ArrayList<String> btNameDev = new ArrayList<String>();
        for (BluetoothDevice bt : pairedBtDevices) {
            btNameDev.add(bt.getName());
            // Log.v("BTDevice:", bt.getName());
        }
    }

    private void show() {

    }

    @Override
    public void onBluetoothTargetSelected(String selectedDevice) {
        selectedDeviceName = selectedDevice;
        Log.v("onbttargetselecte", selectedDevice);
        Toast.makeText(this, selectedDevice, Toast.LENGTH_LONG).show();
        Log.v("onbttargetselecte", "Step 1");
    }

    @Override
    public void onRequestPermissionsResult(int request, String[] permission, int[] results) {

        super.onRequestPermissionsResult(request, permission, results);
        switch (request) {
            //case BluetoothShare.PERMISSION_ACCESS :{
            //   if (results[0] != PackageManager.PERMISSION_GRANTED) {
            //       Toast.makeText(this, "Sharing media over Bluetooth denied", Toast.LENGTH_SHORT).show();
            //   } else {
            //all good to go, start transfer...
            //   }
            //   break;
            // }
//        default: {

            //      }
        }
    }

    public boolean checkPermission(Context context, Permission perm) {
        if (ContextCompat.checkSelfPermission(context, perm.getName()) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, perm.getName())) {
                showAlertDialog(context, perm.getName(), "This permission is needed to share media files.");
            } else {
                String[] perms = {perm.getName()};
                ActivityCompat.requestPermissions((Activity) context, perms, 0);
            }
            return false;

        } else {
            return true;
        }
    }

    public void showAlertDialog(Context context, String permission, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Requiered permission");
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission},
                        ACECCESS_BLUETOOTH_SHARE);
            }
        });
        AlertDialog alertPerm = alert.create();
        alertPerm.show();

    }


}