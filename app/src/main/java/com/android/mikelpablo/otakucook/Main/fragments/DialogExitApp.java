package com.android.mikelpablo.otakucook.Main.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;

public class DialogExitApp extends DialogFragment {

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
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT",true);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }
}
