package com.example.agrisnap;

import static com.example.agrisnap.TownsActivity.preferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Adapters.ImagesAdapter;
import Adapters.SitesAdapter;
import Controller.DatabaseHelper;
import Models.Image;
import Models.Site;

public class ImagesActivity extends AppCompatActivity {

    RecyclerView RecyclerView;

    static StaggeredGridLayoutManager manager;
    static ImagesAdapter adapter;
    FloatingActionButton AddPicBtn;
    Toolbar toolbar;
    TextView textview;
    DatabaseHelper databaseHelper;
    static int landId;
    static String path;
    static Image image;
    static Image selectedImage;
    static int pos;
    public static ArrayList<Image> AllImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        RecyclerView = findViewById(R.id.imagesrecyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textview = findViewById(R.id.toolbarv4);

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        Intent i = getIntent();
        landId = i.getIntExtra("landId", -1);
        path = i.getStringExtra("path");



        AddPicBtn = findViewById(R.id.addpicbtn);
        AddPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImagesActivity.this, CameraActivity.class);
                intent.putExtra("path",path);
                intent.putExtra("landId",landId);
                startActivity(intent);

            }
        });
        databaseHelper = new DatabaseHelper(this);
        AllImages = databaseHelper.getAllImages(landId);

        adapter = new ImagesAdapter(this, AllImages, databaseHelper);
        RecyclerView.setAdapter(adapter);

        manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
    }

    public void showFrag(Image data,int position) {
        selectedImage=data;
        pos=position;
        ImageFrag imageFrag=new ImageFrag(data.getImagePath(),data.getImageLat(),data.getImageLon(),data.getImageDeviceName(),data.getImageTime(),data.getImageDate(),data.getImageName());
        imageFrag.show(this.getSupportFragmentManager(), "Frag");
    }

    public static void notifyAdapter(Image image){
        AllImages.add(image);
        adapter.notifyItemInserted(adapter.getItemCount());
    }

    public static void notifydeleteAdapter(Image image){
        AllImages.remove(image);
        adapter.notifyItemRemoved(ImagesActivity.pos);

    }

}