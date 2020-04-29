package com.example.sdl;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class View_Holder_od extends RecyclerView.ViewHolder {
    CardView cardView;
    TextView textview1;
    TextView textview2;
    TextView typeview;
    public View_Holder_od(@NonNull View itemView) {
        super(itemView);
        cardView=(CardView)itemView.findViewById(R.id.card1);
        textview1=(TextView)itemView.findViewById(R.id.OD);
        textview2=(TextView)itemView.findViewById(R.id.amou);
        typeview=(TextView)itemView.findViewById(R.id.type);
    }
}

