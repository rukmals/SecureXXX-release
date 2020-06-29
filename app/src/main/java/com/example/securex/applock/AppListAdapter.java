package com.example.securex.applock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securex.R;

import java.util.List;
import java.util.Set;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    Context context;
    List<Application> s;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public AppListAdapter(Context context,List<Application> s){
        this.context= context;
        this.s=s;
        pref = context.getSharedPreferences("com.android.app.users",context.MODE_PRIVATE);
        editor=pref.edit();}


    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.recycleviewcard,parent,false);
        return new AppViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final AppViewHolder holder, final int position) {
        holder.description.setText(s.get(position).getName());
        holder.icon.setImageDrawable(s.get(position).getIcon());
        String status = pref.getString(s.get(position).getPackage(),"unlocked");
        boolean mark=false;
        if(status.equals("locked")){
            mark=true;
        }
        holder.switch1.setChecked(mark);
        holder.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.switch1.isChecked()){
                    editor.putString(s.get(position).getPackage(),"locked");
                    editor.commit();
                    holder.switch1.setChecked(true);
                }
                else {
                    editor.putString(s.get(position).getPackage(),"unlocked");
                    editor.commit();
                    holder.switch1.setChecked(false);
                }
                ForegroundToastService.stop(context);
                ForegroundToastService.start(context);


            }
        });


    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        ImageView icon;
        Switch switch1;

        public AppViewHolder(@Nullable View itemView){
            super(itemView);
            description =itemView.findViewById(R.id.description);
            icon=itemView.findViewById(R.id.icon);
            switch1=itemView.findViewById(R.id.switch1);
        }

        }

}
