package com.example.agrisnap;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import Adapters.TownsAdapter;
import Controller.DatabaseHelper;
import Models.Town;

public class TownsActivity extends AppCompatActivity {


    static RecyclerView RecyclerView;

    static LinearLayoutManager linearmanager;
    Toolbar toolbar;
    TextView textview,filenametv;
    static TownsAdapter adapter;
    static int cityId;
    static boolean isFirstLaunch;
    static SharedPreferences preferences;

    DatabaseHelper databaseHelper;

    static String path;
    public static ArrayList<Town> AllTowns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towns);

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isFirstLaunch = preferences.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            startActivity(new Intent(TownsActivity.this, CityActivity.class));
        }else{
            cityId=preferences.getInt("cityId", 0);
            path=preferences.getString("path",null);
        }


        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textview=findViewById(R.id.toolbartv1);



        RecyclerView =findViewById(R.id.towns_reycler_view);

//        Toast.makeText(this, path, Toast.LENGTH_LONG).show();

        databaseHelper=new DatabaseHelper(this);
        AllTowns=databaseHelper.getAllTowns(cityId);

        adapter = new TownsAdapter(this, AllTowns,databaseHelper);
        RecyclerView.setAdapter(adapter);

        linearmanager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(linearmanager);
        notifyAdapter();
    }



    public static void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    public static String getPath(){ return path;}

    @Override
    public void onBackPressed() {
        // If the back button is pressed, quit the app
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}