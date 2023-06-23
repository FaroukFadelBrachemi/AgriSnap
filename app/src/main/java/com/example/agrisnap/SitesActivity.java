package com.example.agrisnap;


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
import Adapters.SitesAdapter;
import Controller.DatabaseHelper;
import Models.Land;
import Models.Site;

public class SitesActivity extends AppCompatActivity  {
    RecyclerView RecyclerView;
    static LinearLayoutManager linearmanager;
    static SitesAdapter adapter;
    FloatingActionButton AddSiteBtn;
    Toolbar toolbar;
    TextView textview;
    DatabaseHelper databaseHelper;
    static int townId;
    static String path;
    public static ArrayList<Site> AllSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

        RecyclerView =findViewById(R.id.sitesrecyclerview);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textview=findViewById(R.id.toolbartv2);

        Intent i=getIntent();
        townId=i.getIntExtra("townId",-1);
        path=i.getStringExtra("path");
//        Toast.makeText(this, path, Toast.LENGTH_LONG).show();

        AddSiteBtn=findViewById(R.id.addsitebtn);
        AddSiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdditionDialog dialog = new AdditionDialog();
                dialog.show(getSupportFragmentManager(), "MyDialogFragment");


            }
        });
        databaseHelper = new DatabaseHelper(this);
        AllSites = databaseHelper.getAllSites(townId);

        adapter = new SitesAdapter(this, AllSites, databaseHelper);
        RecyclerView.setAdapter(adapter);

        linearmanager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(linearmanager);
        adapter.notifyDataSetChanged();
    }

    public static void notifyAdapter(Site site){
        AllSites.add(site);
        adapter.notifyItemInserted(AllSites.size());
    }
    public static String getPath(){ return path;}

}