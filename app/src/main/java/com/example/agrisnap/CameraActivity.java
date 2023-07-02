package com.example.agrisnap;

import static com.example.agrisnap.TownsActivity.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.media.MediaActionSound;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.LocalTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import android.Manifest;


import Controller.DatabaseHelper;
import Models.Image;
import Utils.Utils;


public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_SETTINGS = 1001;
    private int READ_PERMISSION_CODE = 5;

    private int MANAGE_PERMISSION_CODE = 6;
    Uri imageUri;
    Button capturebtn;
    TextView rolltext, pitchtext, longitudetv, latitudetv;
    private int CAMERA_PERMISSION_CODE = 1;
    private int WRITE_PERMISSION_CODE = 2;

    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    private int GPS_PERMISSION_CODE = 4;
    private DatabaseHelper databaseHelper;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;


    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    int pitch, roll;

    String imagePath, imageName;
    private double latitude, longitude;
    private double selectedLatitude, selectedLongitude;
    static boolean compare;
    static SharedPreferences preferences;
    File file;

    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];
    private int landId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.preview_view);
        capturebtn = (Button) findViewById(R.id.capturebtn);
        rolltext = findViewById(R.id.rolltext);
        pitchtext = findViewById(R.id.pitchtext);
        latitudetv = findViewById(R.id.latitudetv);
        longitudetv = findViewById(R.id.longitudetv);


        if (checkGPS()) {
            requestGPS();
        }
        displayLocationSettingsRequest(this);
        getLocation();

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        compare = preferences.getBoolean("compare", false);

        imagePath = getIntent().getStringExtra("path");
        landId = getIntent().getIntExtra("landId", -1);
        databaseHelper = new DatabaseHelper(this);

        if (compare) {
            selectedLongitude = Double.parseDouble(getIntent().getStringExtra("selectedLongitude"));
            selectedLatitude = Double.parseDouble(getIntent().getStringExtra("selectedLatitude"));
        }



        if (ContextCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(CameraActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_PERMISSION_CODE);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorEventListener = new SensorEventListener() {

            public void onSensorChanged(SensorEvent event) {
                if (event.sensor == accelerometerSensor) {
                    System.arraycopy(event.values, 0, accelerometerValues, 0, 3);
                } else if (event.sensor == magnetometerSensor) {
                    System.arraycopy(event.values, 0, magnetometerValues, 0, 3);
                }
                SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues);
                SensorManager.getOrientation(rotationMatrix, orientationAngles);
                pitch = (int) Math.toDegrees(orientationAngles[1]);
                roll = (int) Math.toDegrees(orientationAngles[2]);
                updateUI(pitch, roll);
                if (verifyPitch()) pitchtext.setTextColor(getColor(R.color.green));
                else pitchtext.setTextColor(getColor(R.color.red));

                if (verifyRoll()) rolltext.setTextColor(getColor(R.color.green));
                else rolltext.setTextColor(getColor(R.color.red));

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        capturebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkGPS()) {
                    if (Utils.isPermissionGranted(CameraActivity.this)) {
                        if (isLocationEnabled(CameraActivity.this)) {
                            if (verifyOrientation()) {
                                 if (compare) {
                                    if (checkPosition()) {
                                        capturephoto();
                                    } else {
                                        Toast.makeText(CameraActivity.this, getString(R.string.falselocation), Toast.LENGTH_SHORT).show();
                                    }
                                 } else {
                                    capturephoto();
                                }
                            } else {
                            Toast.makeText(CameraActivity.this, getString(R.string.falseangle), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        displayLocationSettingsRequest(CameraActivity.this);
                    }
                } else {
                    takeMediaPermission();
                }

            }else
                {
                    requestGPS();
                }

            }

        });


        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, getExecutor());
    }

    private void takeMediaPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_PERMISSION_CODE);
        }

    }



    private boolean checkPosition(){
         float DISTANCE = 2.0f;

        Location currentLocation = new Location(LocationManager.GPS_PROVIDER);
        currentLocation.setLatitude((float)latitude);
        currentLocation.setLongitude((float)longitude);

        Location selectedLocation = new Location(LocationManager.GPS_PROVIDER);
        selectedLocation.setLatitude((float)selectedLatitude);
        selectedLocation.setLongitude((float)selectedLongitude);

        float distance = selectedLocation.distanceTo(currentLocation);
        return distance <= DISTANCE;
    }



    private boolean checkGPS(){
        return ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGPS(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                GPS_PERMISSION_CODE);
    }



    private void updateUI(int pitch, int roll) {
        rolltext.setText("roll " + String.valueOf(roll));

        pitchtext.setText("pitch " + String.valueOf(pitch));
    }

    private boolean verifyOrientation() {
       return  (verifyRoll() && verifyPitch()) ;
    }

    private boolean verifyLocation() {
        return (verifyOrientation() && checkPosition()) ;}
    private boolean verifyPitch() { return (pitch > -5 && pitch < 5);}
    private boolean verifyRoll() { return (roll > -100 && roll < -80);}

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                DecimalFormat decimalFormat = new DecimalFormat("#.######");
                String roundedlatitude = decimalFormat.format(latitude);
                String roundedlongitude = decimalFormat.format(longitude);
                latitudetv.setText(roundedlatitude);
                longitudetv.setText(roundedlongitude);

                boolean verify;

                if(compare) {
                    verify=verifyLocation();

                    if(checkPosition()){
                        longitudetv.setTextColor(getColor(R.color.green));
                        latitudetv.setTextColor(getColor(R.color.green));
                    }
                    else{
                        longitudetv.setTextColor(getColor(R.color.red));
                        latitudetv.setTextColor(getColor(R.color.red));
                    }

                }
                else {
                    verify=verifyOrientation();
                    latitudetv.setTextColor(getColor(R.color.black));
                    longitudetv.setTextColor(getColor(R.color.black));
                }

                if (verify){
                    capturebtn.setForeground(getDrawable(R.drawable.camera_abled));
                }
                else{
                    capturebtn.setForeground(getDrawable(R.drawable.camera_disabled));
                }



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_PERMISSION_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static void displayLocationSettingsRequest(Context context) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());

        task.addOnCompleteListener(task1 -> {
            try {
                LocationSettingsResponse response = task1.getResult(ApiException.class);
            } catch (ApiException exception) {
                if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        resolvable.startResolutionForResult((Activity) context, REQUEST_LOCATION_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                    }
                }
            }
        });
    }

    private void capturephoto() {
        imageName="IMG_" + System.currentTimeMillis() + ".jpg";
        file = new File(imagePath, imageName);
        MediaActionSound mediaActionSound = new MediaActionSound();
        mediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(file).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        saveImg();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getBaseContext(), "Erorr Captured : " + exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }


        );

    }


    private void saveImg() {
            databaseHelper = new DatabaseHelper(this);
            Image image=new Image();

            image.setImageLat(String.valueOf(latitude));
            image.setImageLon(String.valueOf(longitude));

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);
            image.setImageTime(String.valueOf(formattedTime));

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = currentDate.format(dateFormatter);
            image.setImageDate(formattedDate);

            String deviceName = Build.BRAND+" "+Build.MODEL;
            image.setImageDeviceName(deviceName);

            image.setImageName(imageName);
            image.setImagePath(file.getPath());

            databaseHelper.addImage(image,landId);
            ImagesActivity.notifyAdapter(image);
    }

    private Executor getExecutor() {
    return ContextCompat.getMainExecutor(this);
    }



    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector=new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());


         imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner)this,cameraSelector, preview, imageCapture);
    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
            return;
        } else {
            if (requestCode == 2) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;

            } else {
                    if (requestCode == 4) {
                        if (grantResults.length > 0
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        } else {

                        }
                        return;

                    }
                    else {
                        if (requestCode == 5) {
                            if (grantResults.length > 0
                                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                            } else {

                            }
                            return;

                        }
                        else{
                            if (requestCode == 6) {
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                                } else {

                                }
                                return;

                            }
                        }
                    }
                }
            }
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("compare",false);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

}