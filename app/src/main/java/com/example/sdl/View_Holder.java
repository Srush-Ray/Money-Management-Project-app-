package com.example.sdl;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class View_Holder extends RecyclerView.ViewHolder{

    CardView cv;
    TextView req;
    TextView ct;

    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card);
        req = (TextView) itemView.findViewById(R.id.cat);
        ct = (TextView) itemView.findViewById(R.id.amount);


    }
}
