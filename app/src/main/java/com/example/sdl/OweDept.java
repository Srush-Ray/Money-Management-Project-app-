package com.example.sdl;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OweDept extends AppCompatActivity implements  OweDialog.OweDialogListener{
    Button btn_owe;
    RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference1;
    List<DataOD> dataOD;
    private String uid=null,key=null,datakey=null;
    DataOD oddata;
    Date dateod;
    String dateSys=null,type;
    int amtod=0;
    Integer amont=0,a=0;

    Recycler_View_Adapter_od adapter;
    ArrayList<DataOD> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owe_dept);
        setTitle("OWE/DEPT");
        FloatingActionButton fab = findViewById(R.id.addbtn);
        dateSys= new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        dateod=new Date();
        Log.i("dds",dateSys.format("dd-MM-YYYY",dateod));
        //dataOD = fill_with_data();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view)
            {

                openD();
            }


        });


//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
       // Recycler_View_Adapter_od adapter = new Recycler_View_Adapter_od(dataOD, getApplication());
        //recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        getlist();


    }

    private void getlist() {
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.keepSynced(true);
        list=new ArrayList<>();
        databaseReference.child(uid).child("Owe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                       final String key = childDataSnapshot.getKey();
                        databaseReference.child(uid).child("Owe").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotc) {
                                if (dataSnapshotc.exists()) {
                                    String name=dataSnapshotc.child("OD").getValue(String.class);
                                    Integer cost=dataSnapshotc.child("amount").getValue(Integer.class);
                                    oddata=new DataOD();
                                    oddata.OD=name;
                                    oddata.amount=cost;
                                    oddata.type="Owe";
                                    oddata.id=key;
                                   list.add(oddata);
                                }
                                adapter=new Recycler_View_Adapter_od(OweDept.this,list);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        databaseReference.child(uid).child("Debt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                       final String key = childDataSnapshot.getKey();
                        databaseReference.child(uid).child("Debt").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotc) {
                                if (dataSnapshotc.exists()) {
                                    String name=dataSnapshotc.child("OD").getValue(String.class);
                                    Integer cost=dataSnapshotc.child("amount").getValue(Integer.class);
                                    oddata=new DataOD();
                                    oddata.OD=name;
                                    oddata.amount=cost;
                                    oddata.type="Debt";
                                    oddata.id=key;
                                    list.add(oddata);
                                }
                                adapter=new Recycler_View_Adapter_od(OweDept.this,list);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
//
//    public List<DataOD> fill_with_data() {
//
//        List<DataOD> data1 = new ArrayList<>();
//
//        data1=loadData(data1);
//        return data1;
//
//
//    }

//    private List<DataOD> loadData(List<DataOD> data1) {
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        uid = firebaseUser.getUid();
//        SharedPreferences sharedPreferences=getSharedPreferences(uid,MODE_PRIVATE);
//        Gson gson=new Gson();
//        String json=sharedPreferences.getString("task list",null);
//        Type type=new TypeToken<ArrayList<DataOD>>(){}.getType();
//        data1=gson.fromJson(json,type);
//        if(data1==null){
//            data1=new ArrayList<DataOD>();
//        }
//        return data1;
//    }


//    private void saveData(){
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        uid = firebaseUser.getUid();
//        SharedPreferences sharedPreferences=getSharedPreferences(uid,MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        String json=gson.toJson(dataOD);
//        editor.putString("task list",json);
//        editor.apply();
//
//
//    }
//

    public void openD(){
        OweDialog exampleDialog=new OweDialog();
        exampleDialog.show(getSupportFragmentManager(),"ExampleDialog");

    }


    public void applyTexts(String Person,String Amount, String od){

        a=Integer.parseInt(Amount);
        type=od;
        final Integer amt=Integer.parseInt(Amount);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.keepSynced(true);
        key =  databaseReference.child(uid).child(od).push().getKey();
//        DataOD d=new DataOD(Person,amt);
//        databaseReference.child(uid).child(od).child(key).setValue(d, new DatabaseReference.CompletionListener() {
//            public  void onComplete(DatabaseError error, DatabaseReference ref) {
//                Toast.makeText(OweDept.this,"Data Entered",Toast.LENGTH_SHORT).show();
//            }
//        });
        DataOD d=new DataOD(Person,amt);
        databaseReference.child(uid).child(od).child(key).setValue(d, new DatabaseReference.CompletionListener() {
            public  void onComplete(DatabaseError error, DatabaseReference ref) {
                Toast.makeText(OweDept.this,"Data Entered",Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.keepSynced(true);
        databaseReference.child(uid).child("DailyTotal").child(dateSys).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    amont=dataSnapshot.getValue(Integer.class);
                 if(type.equals("Owe"))
                    amont=amont+a;
                 else if(type.equals("Debt"))
                    amont=amont-a;
                }else{
                }
                databaseReference.child(uid).child("DailyTotal").child(dateSys).child("amount").setValue(amont);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OweDept.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        getlist();
    }


}
