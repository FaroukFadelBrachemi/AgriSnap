package Adapters;



import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agrisnap.LandsActivity;
import com.example.agrisnap.R;
import com.example.agrisnap.SitesActivity;
import com.example.agrisnap.TownsActivity;


import java.util.ArrayList;

import Controller.DatabaseHelper;
import Models.Site;


public class SitesAdapter extends RecyclerView.Adapter<SiteViewHolder>{

    Context context;
    ArrayList<Site> Sites;
    DatabaseHelper databaseHelper;
    int id;
    String path;

    public SitesAdapter(Context context, ArrayList<Site> Sites) {
        this.context = context;
        this.Sites = Sites;
    }

    public SitesAdapter(Context context, ArrayList<Site> Sites, DatabaseHelper databaseHelper) {
        this.context = context;
        this.Sites = Sites;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new SiteViewHolder(view);
    }



    public void onBindViewHolder(@NonNull final SiteViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Site data = Sites.get(position);
        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);

        holder.text.setText(data.getSiteName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=Sites.get(position).getSiteId();
                path=SitesActivity.getPath()+"/"+Sites.get(position).getSiteName();
                Intent intent = new Intent(context, LandsActivity.class);
                intent.putExtra("siteId", id);
                intent.putExtra("path", path);
                context.startActivity(intent);

            }
        });

    }


    public int getItemCount() {
        return Sites.size();
    }
}



class SiteViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView text;
    public SiteViewHolder(View view) {
        super(view);
        this.text = itemView.findViewById(R.id.file_name_tv);
        this.imageView=itemView.findViewById(R.id.icon_view);
    }
}