package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisnap.AdditionDialog;
import com.example.agrisnap.ImageFrag;
import com.example.agrisnap.ImagesActivity;
import com.example.agrisnap.ImagesActivity;
import com.example.agrisnap.R;

import java.util.ArrayList;

import Controller.DatabaseHelper;
import Models.Image;

public class ImagesAdapter extends RecyclerView.Adapter<ImageViewHolder>{


    Context context;
    ArrayList<Image> Images;
    DatabaseHelper databaseHelper;

    private FragmentManager fragmentManager;

    public ImagesAdapter(Context context, ArrayList<Image> Images) {
        this.context = context;
        this.Images = Images;
    }

    public ImagesAdapter(Context context, ArrayList<Image> Images, DatabaseHelper databaseHelper) {
        this.context = context;
        this.Images = Images;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(view);
    }



    public void onBindViewHolder(@NonNull final ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Image data = Images.get(position);

        Bitmap imageBitmap = BitmapFactory.decodeFile(data.getImagePath());
        Matrix matrix = new Matrix();
        matrix.postRotate(-90); // Negative angle for left rotation
        if(imageBitmap!=null){
            Bitmap rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            holder.imageView.setImageBitmap(rotatedBitmap);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImagesActivity imagesActivity=(ImagesActivity) context;
                imagesActivity.showFrag(data,position);
            }
        });
    }

    public int getItemCount() {
        return Images.size();
    }

}



class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public ImageViewHolder(View view) {
        super(view);

        this.imageView=itemView.findViewById(R.id.image_view);
    }
}