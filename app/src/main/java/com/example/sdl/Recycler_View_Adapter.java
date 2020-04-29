package com.example.sdl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Locale;

public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference1;
    private String uid=null;
    ArrayList<Data> list=new ArrayList<Data>() ;
    Context context;
    int p;
    private String dateSys;
    Integer money=0;
    Integer c=0;
    public Recycler_View_Adapter( Context context,ArrayList<Data>list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initiali  ze the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, final int position) {
        holder.req.setText(list.get(position).requirement);
        String req=list.get(position).requirement;
        Integer p1=list.get(position).cost;
        String p=Integer.toString(p1);
        holder.ct.setText(p);

        int currentPosition=position;
        final Data Info=list.get(position);
        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(Info);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(final Data data) {

        c=data.cost;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        dateSys= new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users").child(uid).child("DailyDetails").child(dateSys).child(data.id);
        databaseReference.keepSynced(true);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                }else{
                }
            }
        });
        databaseReference1=firebaseDatabase.getReference("Users");
        databaseReference1.child(uid).child("DailyTotal").child(dateSys).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    money = dataSnapshot.getValue(Integer.class);
                    money=money-c;
                }else{
                }
                databaseReference1.child(uid).child("DailyTotal").child(dateSys).child("amount").setValue(money);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        int position = list.indexOf(data);
        list.remove(position);
        //accessanddelete(position);
        notifyItemRemoved(position);
    }

//    private void accessanddelete(int p) {
//        List<Data>data1=new ArrayList<>();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        uid = firebaseUser.getUid();
//        SharedPreferences sharedPreferences=context.getSharedPreferences(uid,Context.MODE_PRIVATE);
//        Gson gson=new Gson();
//        String json=sharedPreferences.getString("task list1",null);
//        Type type=new TypeToken<ArrayList<Data>>() {}.getType();
//        data1=gson.fromJson(json,type);
//        if(data1==null)
//        {
//            data1=new ArrayList<>();
//        }
//        data1.remove(p);
//        SharedPreferences sharedPreferences1=context.getSharedPreferences(uid,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences1.edit();
//        Gson gson1=new Gson();
//        String json1=gson1.toJson(data1);
//        editor.putString("task list1",json1);
//        editor.apply();
//    }

}
