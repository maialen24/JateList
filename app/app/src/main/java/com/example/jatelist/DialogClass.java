package com.example.jatelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogClass extends DialogFragment {
    Listener miListener;

    public interface Listener {
        void alpulsarSI();

        void alpulsarNO();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (Listener) getActivity();

        Log.i("info","CREATE NEW USER");

        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            try {
                this.miListener = (Listener)activity;
            }
            catch (final ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SIGN UP");
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.dialog_sigin,null);
        builder.setView(elaspecto);


        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarSI();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarNO();
            }
        });

        return builder.create();


    }


}
