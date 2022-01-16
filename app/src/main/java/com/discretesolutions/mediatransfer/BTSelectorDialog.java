package com.discretesolutions.mediatransfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BTSelectorDialog extends DialogFragment {
    ListView btDevices;
    static ArrayList<String> StringArrBt;

    public interface BluetoothDialogSelector {
        void onBluetoothTargetSelected(String selectedDevice);
    }

    public BTSelectorDialog() {
    }

    public static BTSelectorDialog newInstance(String title) {
        BTSelectorDialog diag = new BTSelectorDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        diag.setArguments(args);
        return diag;
    }

    public static BTSelectorDialog newInstance(String title, Bundle bundle) {
        BTSelectorDialog diag = new BTSelectorDialog();
        bundle.putString("title", title);
        StringArrBt = bundle.getStringArrayList("btdevices");
        diag.setArguments(bundle);
        return diag;
    }

    @Override
    public void onViewCreated(View current, @Nullable Bundle instance) {
        super.onViewCreated(current, instance);
        Log.v("onviewcreated", "waefa");
        btDevices = getView().findViewById(R.id.lstvDevices);
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(getContext(), R.layout.btselectoritem, R.id.tvBtItem, StringArrBt);
        btDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                BluetoothDialogSelector listener = (BluetoothDialogSelector) getActivity();
                listener.onBluetoothTargetSelected(selected);
            }
        });
        btDevices.setAdapter(arrAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.btselector, container);
    }

    /*

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle instance) {

        AlertDialog.Builder abuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        abuilder.setView(inflater.inflate(R.layout.btselector, null));
        return abuilder.create();
    }*/


    public static String TAG = "BtSelectorDialog";
}
