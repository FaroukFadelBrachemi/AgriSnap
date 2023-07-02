package com.example.agrisnap;

import static com.example.agrisnap.ImagesActivity.landId;
import static com.example.agrisnap.ImagesActivity.path;
import static com.example.agrisnap.TownsActivity.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.DecimalFormat;

import Controller.DatabaseHelper;
import Models.Image;
import Models.Land;
import Models.Site;


public class ImageFrag extends AppCompatDialogFragment {

    public ImageView imageView;
    public TextView latitudeTextView, longitudeTextView, nameTextView, pathTextView, timeTextView, dateTextView, deviceNameTextView;
    public ImageButton deleteBtn, compareBtn;
    private DatabaseHelper databaseHelper;
    private String imagePath, latitude, longitude, name, time, date, deviceName;


    public ImageFrag( String imagePath, String latitude, String longitude,String deviceName,  String time, String date,String name ) {
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.time = time;
        this.date = date;
        this.deviceName = deviceName;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_image, null);

        imageView = view.findViewById(R.id.imageView);
        latitudeTextView = view.findViewById(R.id.latitudeTextView);
        longitudeTextView = view.findViewById(R.id.longitudeTextView);
        nameTextView = view.findViewById(R.id.nameTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        deviceNameTextView = view.findViewById(R.id.deviceNameTextView);
        deleteBtn = view.findViewById(R.id.deletebtn);
        compareBtn= view.findViewById(R.id.comparebtn);


        Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        if(imageBitmap!=null){
            Bitmap rotatedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }


        latitudeTextView.setText(getString(R.string.latitude)+": "+latitude);
        longitudeTextView.setText(getString(R.string.longitude)+": "+longitude);
        nameTextView.setText(name);
        timeTextView.setText(time);
        dateTextView.setText(date);
        deviceNameTextView.setText(deviceName);

        databaseHelper = new DatabaseHelper(getContext());



        builder.setView(view);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(ImagesActivity.selectedImage);
                dismiss();
            }
        });
        compareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),CameraActivity.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("compare",true);
                editor.apply();
                i.putExtra("selectedLatitude",latitude);
                i.putExtra("selectedLongitude",longitude);
                i.putExtra("path",path);
                i.putExtra("landId",landId);
                startActivity(i);
                dismiss();
            }

        });

        return builder.create();
    }

    private void deleteImage(Image selectedImage) {
        databaseHelper.deleteImage(selectedImage);
        ImagesActivity.notifydeleteAdapter(selectedImage);
        File imageFile = new File(selectedImage.getImagePath());
        imageFile.delete();
    }


}






