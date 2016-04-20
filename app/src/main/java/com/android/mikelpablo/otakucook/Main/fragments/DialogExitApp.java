package com.android.mikelpablo.otakucook.Main.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogExitApp extends android.app.DialogFragment  {

    public static DialogExitApp newInstance(int title) {
        DialogExitApp frag = new DialogExitApp();
        Bundle args = new Bundle();
        args.putInt("dialog", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return  new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Do you want to exit from app?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }
}
