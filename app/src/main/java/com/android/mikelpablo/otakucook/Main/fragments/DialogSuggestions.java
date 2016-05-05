package com.android.mikelpablo.otakucook.Main.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.R;

public class DialogSuggestions extends DialogFragment {

    public static DialogSuggestions newInstance() {
        DialogSuggestions frag = new DialogSuggestions();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_suggestion_fragment,null);
        final EditText text = (EditText) view.findViewById(R.id.suggestion);
        return  new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.ic_launcher)
                .setView(view)
                .setTitle(R.string.dialog_suggestion_question)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!text.getText().equals("")) {
                            Intent email = new Intent();
                            email.setAction(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_TEXT, text.getText());
                            email.setType("message/rfc822");
                            email.putExtra(Intent.EXTRA_EMAIL,new String[]{"mibaldi2@gmail.com"});
                            email.putExtra(Intent.EXTRA_SUBJECT,"Sujerencias");
                            email.putExtra(Intent.EXTRA_TEXT,text.getText());

                            startActivity(Intent.createChooser(email,"Escoje un cliente de email"));
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }
}
