package com.example.musicplayer;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder
{
    private TextView text_name;
    private TextView text_artist;
    private LinearLayout linearLayout;
    private Button btn_options;

    public MyViewHolder(@NonNull View itemView)
    {
        super(itemView);
        text_name = (TextView) itemView.findViewById(R.id.text_name);
        text_artist = (TextView) itemView.findViewById(R.id.text_artist);
        btn_options = (Button) itemView.findViewById(R.id.btn_options);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout_left);
    }

    public TextView getText_name() {
        return text_name;
    }

    public TextView getText_artist() {
        return text_artist;
    }

    public Button getBtn_options() {
        return btn_options;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }
}
