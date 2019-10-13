package com.example.musicplayer;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    private List<Music> musicList;

    public MusicAdapter(List<Music> musicList)
    {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        final Music music = musicList.get(position);
        holder.getText_name().setText(music.getName());
        holder.getText_artist().setText(music.getArtist());
        holder.getLinearLayout().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(Background.playMusicNow(music))
                    Background.updateMainActivityUI(music.getName(), true);
            }
        });
        holder.getBtn_options().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_of_all_music_list, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        if(menuItem.getItemId() == R.id.item_add_to_play_list)
                            Background.musicPlayer.addToList(musicList.get(position));
                        else if(menuItem.getItemId() == R.id.item_play_next)
                            Background.musicPlayer.addToNext(musicList.get(position));
                        else
                        {
                            File file = new File(musicList.get(position).getPath());
                            file.delete();
                            Background.mainActivity.refreshRecyclerView();
                        }
                        return true;
                    }

                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return musicList.size();
    }
}
