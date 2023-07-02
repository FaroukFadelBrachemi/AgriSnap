package com.example.agrisnap;


import static com.example.agrisnap.TownsActivity.preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Controller.DatabaseHelper;
import Models.City;
import Models.Town;
import Utils.Utils;


public class CityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    boolean isCreated,isCityCreated;
    File externalStorageDir,dcimDir;
    String laghouat,path;
    private int cityId;
    private int WRITE_PERMISSION_CODE =2 ;
    private int READ_PERMISSION_CODE = 5;



    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Spinner spinner = findViewById(R.id.spinner);
        TextView wilayatv = findViewById(R.id.wilayatv);
        Button cityBtn = findViewById(R.id.citybtn);


        databaseHelper = new DatabaseHelper(CityActivity.this);

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if(!isCityCreated){
            laghouat=getString(R.string.Laghouat);
            databaseHelper.addCity(new City(laghouat));
            externalStorageDir = Environment.getExternalStorageDirectory();
            dcimDir = new File(externalStorageDir.getAbsolutePath() + "/DCIM/AgriSnap/"+laghouat);
            dcimDir.mkdir();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isCityCreated",true);
            editor.apply();
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/AgriSnap/" + getString(R.string.Laghouat);
        }



        askForPermission();


        cityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String[] laghouatTowns= {"الأغواط", "قصر الحيران", "بن ناصر بن شهرة", "سيدي مخلوف", "حاسي الدلاعة", "حاسي الرمل", "عين ماضي", "تاجموت", "الخنق", "قلتة سيدي سعد", "عين سيدي علي", "البيضاء", "بريدة", "الغيشة", "الحاج المشري", "سبقاق", "تاويالة", "تاجرونة", "أفلو", "العسافية", "وادي مرة", "وادي مزي", "الحويطة", "سيدي بوزيد"};

                for(int i=0;i<laghouatTowns.length;i++){
                    databaseHelper.addTown(new Town(laghouatTowns[i]),1);
                    externalStorageDir = Environment.getExternalStorageDirectory();
                    dcimDir = new File(externalStorageDir.getAbsolutePath() + "/DCIM/AgriSnap/"+laghouat+"/"+laghouatTowns[i]);
                    dcimDir.mkdir();

                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstLaunch", false);
                editor.putString("path", path);
                editor.putInt("cityId", cityId);
                editor.apply();

                Intent intent = new Intent(CityActivity.this, TownsActivity.class);
                startActivity(intent);
                finish();


            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, databaseHelper.getCities());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(CityActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_PERMISSION_CODE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        City city=databaseHelper.getCity(selectedItem);
        cityId=city.getCityId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onResume() {
        super.onResume();
        if(!Utils.isPermissionGranted(this)){
            takePermission();
        }
    }

    @Override
    public void onBackPressed() {
        // If the back button is pressed, quit the app
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            // If request is cancelled, the grantResults array is empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, you can launch the camera or perform other operations that require the camera here
            }
            else {

            }
            return;
        } else{
            if (requestCode == 5) {
                // If request is cancelled, the grantResults array is empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted, you can launch the camera or perform other operations that require the camera here
                }
                else {
                    takePermission();
                }
                return;
            }
        }
    }

    private void takePermission()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R)
        {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri= Uri.fromParts( "package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent,  101);
            } catch (Exception e)
            {
                e.printStackTrace();
                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,  101);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_PERMISSION_CODE);
        }
    }
}