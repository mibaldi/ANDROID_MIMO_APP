package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by pabji on 30/04/2016.
 */
public class OwnIngredientDialog extends DialogFragment {


    private EditText newIngredient;

    public static OwnIngredientDialog newInstance(int title) {
        OwnIngredientDialog frag = new OwnIngredientDialog();
        Bundle args = new Bundle();
        args.putInt("dialog", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog,null);
        newIngredient = (EditText) view.findViewById(R.id.ownIngredient);
        return  new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public int numberId;

                    public void onClick(DialogInterface dialog, int which) {
                        final String ingredient = newIngredient.getText().toString();

                        final Firebase refUser = new Firebase(getResources().getString(R.string.users));
                        final String uid = LoginActivity.mAuthData.getUid();
                        Log.d("llkn",uid);
                        refUser.child(uid).child("owningredient").orderByKey().startAt(uid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                numberId = (int) dataSnapshot.getChildrenCount() + 1;
                                String ingredientId = uid+String.valueOf(numberId);
                                Firebase refOwnIngredient = refUser.child(LoginActivity.mAuthData.getUid()).child("owningredient").child(ingredientId);
                                OwnIngredientFB ownIngredientFB = new OwnIngredientFB(ingredientId, "0", "1");
                                refOwnIngredient.setValue(ownIngredientFB);

                                Firebase refIngredient = new Firebase("https://otakucook.firebaseio.com/ingredients");
                                Ingredient customIngredient = new Ingredient();
                                customIngredient.name = ingredient;
                                refIngredient.child(ingredientId).setValue(customIngredient);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }
}
