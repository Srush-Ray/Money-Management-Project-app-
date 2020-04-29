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

public class OweDialog extends AppCompatDialogFragment {
    private EditText editTextPerson;
    private EditText editTextAmount;

    private OweDialogListener listener=new OweDialogListener() {
        public void applyTexts(String Person,String amount,String od){

        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.owedept, null);
        editTextPerson =view.findViewById(R.id.text1);
        editTextAmount=view.findViewById(R.id.textInputLayout2);
        builder.setView(view)
                .setTitle("Information")
                .setNegativeButton("Debt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String Person = editTextPerson.getText().toString();
                        String Amount = editTextAmount.getText().toString();
                        if (TextUtils.isEmpty(Person)) {
                            Toast.makeText(getContext(), "Please enter Person name!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(Amount)) {
                            Toast.makeText(getContext(), "Please enter Amount!", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        listener.applyTexts(Person, Amount,"Debt");

                    }
                })
                .setPositiveButton("Owe", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String Person = editTextPerson.getText().toString();
                        String Amount = editTextAmount.getText().toString();
                        if (TextUtils.isEmpty(Person)) {
                            Toast.makeText(getContext(), "Please enter Person name!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(Amount)) {
                            Toast.makeText(getContext(), "Please enter Amount!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listener.applyTexts(Person, Amount,"Owe");
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OweDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }




    public interface OweDialogListener{

        void applyTexts(String person, String amount,String od);
    }

}
