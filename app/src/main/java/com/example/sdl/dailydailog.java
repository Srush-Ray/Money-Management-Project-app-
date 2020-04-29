package com.example.sdl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class dailydailog extends AppCompatDialogFragment {

      private EditText editTextRequirement;
      private EditText editTextCost;
      Integer amt=0;

        private dailydailogListener listener=new dailydailogListener() {
            @Override
            public void applyTexts(String req, Integer cost) {

            }
        };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dailyexp,null);


        editTextRequirement = view.findViewById(R.id.Requirement);
        editTextCost = view.findViewById(R.id.Cost);

        builder.setView(view)
                .setTitle("Expense")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String req = editTextRequirement.getText().toString();
                        String cost = editTextCost.getText().toString();
                        amt=Integer.parseInt(cost);
                        if (TextUtils.isEmpty(req)) {
                            Toast.makeText(getContext(), "Please enter requirement!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(cost)) {
                            Toast.makeText(getContext(), "Please enter cost!", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        listener.applyTexts(req,amt);
                    }
                });


        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (dailydailogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement dailydialogListener");
        }
    }


    public interface dailydailogListener {

        void applyTexts(String req, Integer cost);

    }
}
