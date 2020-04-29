package com.example.sdl;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity  {

    protected String userid;
    private FirebaseDatabase firebaseDatabase;
    MainActivity M=new MainActivity();
    private ProgressDialog pd;
    private EditText userEmail,password;
    private TextView forgotpass,newacct;
    private Button login;
    private String email=null,pass=null;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
CardView cardView;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LOGIN");

//        if(!isConnected(Login.this)) buildDialog(Login.this).show();
//        else {
//        }

        pd = new ProgressDialog(Login.this);
        pd.setMessage("Loading");
        userEmail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        forgotpass = (TextView) findViewById(R.id.forgotpassword);
        newacct = (TextView) findViewById(R.id.NewAccount);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");

        newacct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                loginUserAccount();
            }
        });

    }
    private void recoverPassword(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout liner=new LinearLayout(this);
        final EditText e=new EditText(this);
        e.setHint("Email");
        e.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        liner.addView(e);
        liner.setPadding(10,10,10,10);

        builder.setView(liner);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String em=e.getText().toString().trim();
                beginRecovery(em);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void beginRecovery(String em) {
        mAuth.sendPasswordResetEmail(em).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful()){
               Toast.makeText(Login.this,"Recovery link sent to email.",Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(Login.this,"Cannot send Recovery link. Try again later.",Toast.LENGTH_SHORT).show();
           }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this,"Failed to send link due to : "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUserAccount() {
       email=userEmail.getText().toString().trim();
       pass=password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.cancel();
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
//                            firebaseDatabase= FirebaseDatabase.getInstance();
//                            databaseReference=firebaseDatabase.getReference("Users");
//                            databaseReference.keepSynced(true);
                          //  databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("DailyDetails").child("amount").setValue(0);
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            pd.cancel();
                            Toast.makeText(getApplicationContext(), " Please try again later.Login failed due to : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public String getUID(){
        return userid;
    }

}
