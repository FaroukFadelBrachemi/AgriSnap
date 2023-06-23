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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agrisnap.SitesActivity;
import com.example.agrisnap.TownsActivity;
import com.example.agrisnap.R;

import java.util.ArrayList;

import Controller.DatabaseHelper;
import Models.Town;

public class TownsAdapter extends RecyclerView.Adapter<TownViewHolder>{

    Context context;
    ArrayList<Town> Towns;
    DatabaseHelper databaseHelper;
    private int id;
    private String path;

    public TownsAdapter(Context context, ArrayList<Town> Towns) {
        this.context = context;
        this.Towns = Towns;
    }

    public TownsAdapter(Context context, ArrayList<Town> Towns, DatabaseHelper databaseHelper) {
        this.context = context;
        this.Towns = Towns;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    public TownViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new TownViewHolder(view);
    }



    public void onBindViewHolder(@NonNull final TownViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Town data = Towns.get(position);

        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);

        holder.text.setText(data.getTownName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=Towns.get(position).getTownId();
                path=TownsActivity.getPath()+"/"+Towns.get(position).getTownName();
                Intent intent = new Intent(context, SitesActivity.class);
                intent.putExtra("townId", id);
                intent.putExtra("path", path);
                context.startActivity(intent);

            }
        });



//        holder.text.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                AlertDialog.Builder Dialog=new AlertDialog.Builder(context);
//                Dialog.setTitle("Delete Town");
//                Dialog.setMessage("are you sure you want to delete this Town ?");
//
//                Dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        databaseHelper.deleteTown(data);
//                        Towns.remove(position);
//                        ArchiveFragment.notifyAdapter();
//                    }
//                });

//                Dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
//
//                Dialog.show();
//                return false;
//            }
//        });

    }


    public int getItemCount() {
        return Towns.size();
    }
}



class TownViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView text;
    public TownViewHolder(View view) {
        super(view);
        this.text = itemView.findViewById(R.id.file_name_tv);
        this.imageView=itemView.findViewById(R.id.icon_view);
    }
}

