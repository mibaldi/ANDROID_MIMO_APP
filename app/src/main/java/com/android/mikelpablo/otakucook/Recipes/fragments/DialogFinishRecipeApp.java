package com.android.mikelpablo.otakucook.Recipes.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeFinalActivity;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeTaskViewPageActivity;

public class DialogFinishRecipeApp extends android.app.DialogFragment  {

    public static DialogFinishRecipeApp newInstance(int title,Recipe recipe) {
        DialogFinishRecipeApp frag = new DialogFinishRecipeApp();
        Bundle args = new Bundle();
        args.putInt("dialog", title);
        args.putParcelable("recipe",recipe);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        return  new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("¿Quieres finalizar la receta?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(),RecipeFinalActivity.class);
                        intent.putExtra("recipe",getArguments().getParcelable("recipe"));
                        getActivity().startActivity(intent);
                        Toast.makeText(getContext(),"Final",Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }
}
