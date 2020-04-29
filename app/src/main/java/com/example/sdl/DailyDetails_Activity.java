package com.example.sdl;

import android.os.Bundle;
import android.view.View;
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

public class DailyDetails_Activity extends AppCompatActivity implements dailydailog.dailydailogListener {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<Data> data;
    Data DD;
    private String uid=null,datakey=null;
    Integer amt=0;
    Integer money=0;
    private String dateSys;
    Recycler_View_Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_details_);
        //data = fill_with_data();
        dateSys= new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        setTitle(dateSys);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = (RecyclerView) findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

            }
        });
//        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, getApplication());
//        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        getlistN();
    }

    private void getlistN() {
        {
            firebaseDatabase= FirebaseDatabase.getInstance();
            databaseReference=firebaseDatabase.getReference("Users");
            databaseReference.keepSynced(true);
            list=new ArrayList<>();
            databaseReference.child(uid).child("DailyDetails").child(dateSys).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            final String key = childDataSnapshot.getKey();

                            databaseReference.child(uid).child("DailyDetails").child(dateSys).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotc) {
                                    if (dataSnapshotc.exists()) {
                                        String req=dataSnapshotc.child("requirement").getValue(String.class);
                                        Integer co=dataSnapshotc.child("cost").getValue(Integer.class);
                                        DD=new Data();
                                        DD.requirement=req;
                                        DD.cost=co;
                                        DD.id=key;
                                        list.add(DD);
                                    }
                                    adapter = new Recycler_View_Adapter(DailyDetails_Activity.this, list);
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
    }

//    private List<Data> fill_with_data() {
//
//        List<Data> data1 = new ArrayList<>();
//
//        data1=loadData1(data1);
//        return data1;
//    }
//
//    private List<Data> loadData1(List<Data> data1) {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        uid = firebaseUser.getUid();
//        SharedPreferences sharedPreferences=getSharedPreferences(uid,MODE_PRIVATE);
//        Gson gson=new Gson();
//        String json=sharedPreferences.getString("task list1",null);
//        Type type=new TypeToken<ArrayList<Data>>(){}.getType();
//        data1=gson.fromJson(json,type);
//        if(data1==null){
//            data1=new ArrayList<>();
//
//        }
//        return data1;
//    }


//    private void saveData(){
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        uid = firebaseUser.getUid();
//        SharedPreferences sharedPreferences=getSharedPreferences(uid,MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        String json=gson.toJson(data);
//        editor.putString("task list1",json);
//        editor.apply();
//    }

    public void openDialog() {
        dailydailog exampleDialog = new dailydailog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    public void applyTexts(final String req, final Integer cost)
    {
        amt=cost;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");

        databaseReference.keepSynced(true);
        databaseReference.child(uid).child("DailyTotal").child(dateSys).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    money = dataSnapshot.getValue(Integer.class);
                    amt=money+amt;
                }else{
                }
                databaseReference.child(uid).child("DailyTotal").child(dateSys).child("amount").setValue(amt);
                datakey =  databaseReference.child(uid).child("DailyDetails").child(dateSys).push().getKey();
                DD=new Data(req,cost);
                databaseReference.child(uid).child("DailyDetails").child(dateSys).child(datakey).setValue(DD ,new DatabaseReference.CompletionListener() {
                    public  void onComplete(DatabaseError error, DatabaseReference ref) {
                        Toast.makeText(DailyDetails_Activity.this,"Added",Toast.LENGTH_SHORT).show();
                    }
                });
                getlistN();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DailyDetails_Activity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


}
}
