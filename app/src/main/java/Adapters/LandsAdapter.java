package Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisnap.ImagesActivity;
import com.example.agrisnap.R;
import com.example.agrisnap.LandsActivity;
import com.example.agrisnap.LandsActivity;

import java.util.ArrayList;

import Controller.DatabaseHelper;
import Models.Land;
import Models.Land;

public class LandsAdapter extends RecyclerView.Adapter<LandViewHolder>{

    Context context;
    ArrayList<Land> Lands;
    DatabaseHelper databaseHelper;
    int id;
    String path;


    public LandsAdapter(Context context, ArrayList<Land> Lands) {
        this.context = context;
        this.Lands = Lands;
    }

    public LandsAdapter(Context context, ArrayList<Land> Lands, DatabaseHelper databaseHelper) {
        this.context = context;
        this.Lands = Lands;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    public LandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new LandViewHolder(view);
    }



    public void onBindViewHolder(@NonNull final LandViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Land data = Lands.get(position);
        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);

        holder.text.setText(data.getLandName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=Lands.get(position).getLandId();
                path=LandsActivity.getPath()+"/"+Lands.get(position).getLandName()+"/";
                Intent intent = new Intent(context, ImagesActivity.class);
                intent.putExtra("landId", id);
                intent.putExtra("path", path);
                context.startActivity(intent);

            }
        });
    }


    public int getItemCount() {
        return Lands.size();
    }
}



class LandViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView text;
    public LandViewHolder(View view) {
        super(view);
        this.text = itemView.findViewById(R.id.file_name_tv);
        this.imageView=itemView.findViewById(R.id.icon_view);
    }
}