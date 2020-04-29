package com.example.sdl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class Recycler_View_Adapter_od extends RecyclerView.Adapter<View_Holder_od>{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference1;
    private String uid=null;
    Recycler_View_Adapter_od adapter;
    String Dtype;
    ArrayList<DataOD> list=new ArrayList<DataOD>() ;
    Context context;
    String dateSys= new SimpleDateFormat("dd-MM-YYYY",Locale.getDefault()).format(new Date());
    Integer amt=0,money=0;
    public Recycler_View_Adapter_od( Context context,ArrayList<DataOD> list){
        this.context=context;
        this.list=list;
    }

    public View_Holder_od onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owedeptsingle, parent, false);
        View_Holder_od holder = new View_Holder_od(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_od holder, final int position) {
        holder.textview1.setText(list.get(position).OD);
        Integer p=list.get(position).amount;
        holder.textview2.setText(Integer.toString(p));
        if(list.get(position).type.equals("Debt")) {
            holder.typeview.setText("Debt of");
            holder.typeview.setTextColor(Color.parseColor("#ff0000"));
        }
        else if(list.get(position).type.equals("Owe")) {
            holder.typeview.setText("Owes you");
            holder.typeview.setTextColor(Color.parseColor("#008744"));
        }
        final int currentPosition=position;
        final DataOD Info=list.get(position);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
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

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, DataOD data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

        // Remove a RecyclerView item containing a specified Data object
    public void remove(DataOD dod) {
        amt=dod.amount;
        Dtype=dod.type;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.child(uid).child(dod.type).child(dod.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                    databaseReference1=firebaseDatabase.getReference("Users");
                    databaseReference1.child(uid).child("DailyTotal").child(dateSys).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                money = dataSnapshot.getValue(Integer.class);
                                if(Dtype.equals("Owe")){
                                money=money-amt;
                                }else if(Dtype.equals("Debt")){
                                    money=money+amt;
                                }
                            }else{
                            }
                            databaseReference1.child(uid).child("DailyTotal").child(dateSys).child("amount").setValue(money);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {        }
                    });
                }else{

                }
            }
        });


        int position = list.indexOf(dod);
        list.remove(position);
        //accessanddelete(position);
        notifyItemRemoved(position);
    }

//    public void accessanddelete(int p)
//    {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        uid = firebaseUser.getUid();
//        List<DataOD>data1=new ArrayList<>();
//        SharedPreferences sharedPreferences=context.getSharedPreferences(uid,Context.MODE_PRIVATE);
//        Gson gson=new Gson();
//        String json=sharedPreferences.getString("task list",null);
//        Type type=new TypeToken<ArrayList<DataOD>>() {}.getType();
//        data1=gson.fromJson(json,type);
//        if(data1==null)
//        {
//            data1=new ArrayList<>();
//        }
//
//        data1.remove(p);
//
//        SharedPreferences sharedPreferences1=context.getSharedPreferences(uid,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences1.edit();
//        Gson gson1=new Gson();
//        String json1=gson1.toJson(data1);
//        editor.putString("task list",json1);
//        editor.apply();
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView od;
        TextView amt;

        public MyViewHolder(View itemview){
            super(itemview);
            TextView name=(TextView) itemview.findViewById(R.id.OD);
            TextView amt=(TextView)itemview.findViewById(R.id.amou);
        }
    }
}

