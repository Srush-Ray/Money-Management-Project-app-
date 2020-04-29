package com.example.sdl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

public class Dashboard extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase,firebaseDatabase1;
    private DatabaseReference databaseReference,databaseReference1,databaseReference2;
    String uid=null,name=null,databasedate=null;
    public LineChart mpLineChart1;
    private String dateSys=null;
    LineDataSet lineDataSet=new LineDataSet(null,null);
    ArrayList<ILineDataSet>ilineDataSets=new ArrayList<>();
    LineData lineData;
    int day,month,year,Sysmonth,Sysyear,Damount=0;
    String[] outdate;
    Integer[] days,amts;
    TextView da;
    NotificationManagerCompat notificationManager;

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                Toast.makeText(Dashboard.this,"Information",Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(Dashboard.this,PopupWindow.class);
                startActivity(intent1);
                break;
            case R.id.logoutop:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Dashboard.this,"Logout",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Dashboard.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        da=(TextView)findViewById(R.id.DailyAmount);
        mpLineChart1 = (LineChart) findViewById(R.id.line_chart1);
        //sendNotification();
        dateSys= new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        String[] separate=dateSys.split("-");
        Sysmonth=Integer.parseInt(separate[1]);
        Sysyear=Integer.parseInt(separate[2]);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dateSys= new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        uid= firebaseUser.getUid();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.keepSynced(true);
        databaseReference.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                   name=dataSnapshot.getValue(String.class);
                   setTitle(name);

                }else{
                    setTitle("DASHBOARD");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setTitle("DASHBOARD");
            }
        });
        databaseReference.child(uid).child("DailyTotal").child(dateSys).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Damount=dataSnapshot.getValue(Integer.class);
                    da.setText("Today's Total : "+Integer.toString(Damount));
                }else{
                    da.setText("Today's Total : "+"0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        insertGraph();

    }

    private void insertGraph() {
        firebaseDatabase1= FirebaseDatabase.getInstance();
        databaseReference1=firebaseDatabase1.getReference("Users");
        databaseReference1.child(uid).child("DailyTotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final ArrayList<Entry>dataVals=new ArrayList<Entry>();
                if (dataSnapshot.exists()) {
                    for(DataSnapshot childShot:dataSnapshot.getChildren()){
                        databasedate = childShot.getKey();
                        outdate=databasedate.split("-");
                        day=Integer.parseInt(outdate[0]);
                        month=Integer.parseInt(outdate[1]);
                        year=Integer.parseInt(outdate[2]);
                        DailyDetails D=new DailyDetails();
                        D=childShot.getValue(DailyDetails.class);
                        Damount=D.amount;
                        if(month==Sysmonth){
                        dataVals.add(new Entry(day,Damount));
                        }else{
                        }
                    }
                    showChart(dataVals);
                }else {
                 mpLineChart1.clear();
                 mpLineChart1.invalidate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }
    private void  showChart(ArrayList<Entry> dataVals){
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Expenditure");
//      Expenditure.setColor(Color.rgb(16, 40, 235));
        ilineDataSets.clear();
        ilineDataSets.add(lineDataSet);
        lineData= new LineData(ilineDataSets);
        mpLineChart1.clear();
        mpLineChart1.setData(lineData);
        mpLineChart1.invalidate();
        mpLineChart1.animateY(1000);
        mpLineChart1.setBackgroundColor(Color.rgb(138, 218, 255));
        mpLineChart1.setBorderColor(Color.rgb(16, 40, 235));
        mpLineChart1.setDrawBorders(true);
        lineDataSet.setLineWidth(4);
    }
    public void DailyActivity(View view){
            Intent intent=new Intent(Dashboard.this,DailyDetails_Activity.class);
            startActivity(intent);
        }
        public void owedept(View view){
        Intent intent=new Intent(Dashboard.this,OweDept.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.keepSynced(true);
        databaseReference.child(uid).child("DailyTotal").child(dateSys).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Damount=dataSnapshot.getValue(Integer.class);
                    da.setText("Today's Total : "+Integer.toString(Damount));
                }else{
                    da.setText("Today's Total : "+"0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        insertGraph();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}
