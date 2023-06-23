package com.example.agrisnap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Adapters.LandsAdapter;
import Adapters.TownsAdapter;
import Controller.DatabaseHelper;
import Models.Land;


public class LandsActivity extends AppCompatActivity  {
    RecyclerView RecyclerView;
    static LinearLayoutManager linearmanager;
    static LandsAdapter adapter;
    FloatingActionButton AddLandBtn;
    Toolbar toolbar;
    TextView textview;
    DatabaseHelper databaseHelper;
    static int siteId;
    static String path;
    public static ArrayList<Land> AllLands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lands);

        RecyclerView = findViewById(R.id.landsrecyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textview = findViewById(R.id.toolbartv3);


        Intent i=getIntent();
        siteId=i.getIntExtra("siteId",-1);
        path=i.getStringExtra("path");
//        Toast.makeText(this, path, Toast.LENGTH_LONG).show();

        AddLandBtn=findViewById(R.id.addlandbtn);
        AddLandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdditionDialog dialog = new AdditionDialog();
                dialog.show(getSupportFragmentManager(), "MyDialogFragment");
            }
        });


        databaseHelper = new DatabaseHelper(this);
        AllLands = databaseHelper.getAllLands(siteId);

        adapter = new LandsAdapter(this, AllLands, databaseHelper);
        RecyclerView.setAdapter(adapter);

        linearmanager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(linearmanager);
        adapter.notifyDataSetChanged();
    }

    public static void notifyAdapter(Land land){
        AllLands.add(land);
        adapter.notifyItemInserted(AllLands.size());
    }

    public static String getPath(){ return path;}



}
