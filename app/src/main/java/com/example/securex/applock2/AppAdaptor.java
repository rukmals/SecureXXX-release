package com.example.securex.applock2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securex.R;

import java.util.List;

class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private Context context;
    private List<AppItem> appItems;
    private Utils utils;
    public AppAdapter(Context context, List<AppItem> appItems) {
        this.context = context;
        this.appItems = appItems;
        this.utils= new Utils(context);
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_layout,parent,false);
        return  new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppViewHolder holder, int position) {
        holder.name_app.setText(appItems.get(position).getName());
        holder.icon_app.setImageDrawable(appItems.get(position).getIcon());

        final String pk = appItems.get(position).getPackageName();

        if(utils.isLock(pk)){
            holder.lock_app.setImageResource(R.drawable.ic_lock_black_24dp);
        }
        else{
            holder.lock_app.setImageResource(R.drawable.ic_lock_open_black_24dp);
        }

        holder.setListener(new AppOnClickListener() {
            @Override
            public void selectApp(int pos) {
                if (utils.isLock(pk)){
                    holder.lock_app.setImageResource(R.drawable.ic_lock_open_black_24dp);
                    utils.unLock(pk);
                }
                else {
                    holder.lock_app.setImageResource(R.drawable.ic_lock_black_24dp);
                    utils.lock(pk);
                }

            }
        });

    }



    @Override
    public int getItemCount() {
        return appItems.size();
    }
}
