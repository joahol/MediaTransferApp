package com.discretesolutions.mediatransfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BTSelectorDialog extends DialogFragment {
    ListView btDevices;

    public BTSelectorDialog() {
    }

    /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstace){

            return inflater.inflate(R.layout.btselector,parent, false);
        }
    */
    @Override
    public void onViewCreated(View current, @Nullable Bundle instance) {
        btDevices = (ListView) getView().findViewById(R.id.lstvDevices);


    }

    @Override
    public Dialog onCreateDialog(Bundle instance) {
        AlertDialog.Builder abuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        abuilder.setView(inflater.inflate(R.layout.btselector, null));
        return abuilder.create();
    }

}
