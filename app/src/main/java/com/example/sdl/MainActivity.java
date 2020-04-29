package com.example.sdl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd;
    protected String USERID=null;
    private EditText userEmail,password,confirmpassword,phoneno,Username;
    private Button signin;
    private FirebaseAuth firebaseauth;
    private String email=null,pass=null,cpass=null,phoneNumber=null,user=null;
    private DatabaseReference databaseref;
    private FirebaseDatabase firedatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd=new ProgressDialog(MainActivity.this);
        pd.setTitle("Registering");
        pd.setMessage("Please Wait...");
        userEmail=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirmpassword=(EditText)findViewById(R.id.cpassword);
        Username=(EditText)findViewById(R.id.username);
        phoneno=(EditText)findViewById(R.id.phone);
        signin=(Button)findViewById(R.id.SignIn);

       firebaseauth=FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.show();
                email=userEmail.getText().toString().trim();
                pass= password.getText().toString().trim();
                cpass=confirmpassword.getText().toString().trim();
                phoneNumber=phoneno.getText().toString().trim();
                user=Username.getText().toString().trim();

                firedatabase=FirebaseDatabase.getInstance();
                databaseref=firedatabase.getReference("Users");

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(MainActivity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(cpass)){
                    Toast.makeText(MainActivity.this,"Confirm Password did not match",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(user)){
                    Toast.makeText(MainActivity.this,"Please Enter Username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(MainActivity.this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show();
                    return;
                }




                if(pass.length()<=5 || pass.length()>=20){
                    Toast.makeText(MainActivity.this,"Password should have 5 to 20 characters",Toast.LENGTH_LONG).show();

                }

                if(pass.equals(cpass)){
                    firebaseauth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        pd.cancel();
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = firebaseUser.getUid();
                                        //String id=databaseref.push().getKey();
                                        UserDetails U=new UserDetails(
                                                user,
                                                email,
                                                phoneNumber
                                        );


                                        databaseref.child(uid).setValue(U, new DatabaseReference.CompletionListener() {
                                            public  void onComplete(DatabaseError error, DatabaseReference ref) {
                                                Toast.makeText(MainActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        USERID=uid;


                                        Intent intent=new Intent(MainActivity.this,Login.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        pd.cancel();
                                        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                                    }

                                }
                            });
                }

            }
        });

   }
   protected String getUserID(){
        return USERID;
   }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(MainActivity.this,Login.class);
        startActivity(in);
        finish();
    }
}