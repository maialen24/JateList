package com.example.jatelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class infoDialog extends DialogFragment {

    /* This class create info dialog that shows a description of the app */

    infoDialog.Listener miListener;

    //create interface that login class is going to implement
    public interface Listener {
        void alpulsarOK();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (infoDialog.Listener) getActivity();

        Log.i("info","info Dialog");




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.about));
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.info_dialog,null);
        builder.setView(elaspecto);

        //close dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                miListener.alpulsarOK();
            }
        });


        return builder.create();


    }
}


