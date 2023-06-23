package Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.ContextCompat;

public class Utils {


    public static final String TABLE_AGENT = "AGENT";
    public static final String AGENT_ID = "AGENT_ID";
    public static final String AGENT_NAME = "AGENT_NAME";
    public static final String AGENT_LASTNAME = "AGENT_LASTNAME";
    public static final String AGENT_USERNAME = "AGENT_USERNAME";
    public static final String AGENT_PASSWORD = "AGENT_PASSWORD";
    public static final String AGENT_DEVICE = "AGENT_DEVICE";
    public static final String AGENT_PICTURE = "AGENT_PICTURE";
    public static final String AGENT_BIRTH = "AGENT_BIRTH";
    public static final String TABLE_IMAGE = "IMAGE";
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String IMAGE_NAME = "IMAGE_NAME";
    public static final String IMAGE_PATH = "IMAGE_PATH";
    public static final String IMAGE_DEVICENAME = "IMAGE_DEVICENAME";
    public static final String IMAGE_TIME = "IMAGE_TIME";
    public static final String IMAGE_DATE = "IMAGE_DATE";
    public static final String IMAGE_LAT = "IMAGE_LAT";
    public static final String IMAGE_LON = "IMAGE_LON";
    public static final String TABLE_LAND = "LAND";
    public static final String LAND_ID = "LAND_ID";
    public static final String LAND_NAME = "LAND_NAME";
    public static final String TABLE_FARMER = "FARMER";
    public static final String FARMER_ID = "FARMER_ID";
    public static final String FARMER_NAME = "FARMER_NAME";
    public static final String FARMER_LASTNAME = "FARMER_LASTNAME";
    public static final String FARMER_BIRTH = "FARMER_BIRTH";
    public static final String TABLE_SITE = "SITE";
    public static final String SITE_ID = "SITE_ID";
    public static final String SITE_NAME = "SITE_NAME";
    public static final String TABLE_TOWN = "TOWN";
    public static final String TOWN_ID = "TOWN_ID";
    public static final String TOWN_NAME = "TOWN_NAME";
    public static final String TABLE_CITY = "CITY";
    public static final String CITY_ID = "CITY_ID";
    public static final String CITY_NAME = "CITY_NAME";

    public static final String AGENT_ID_FK = "AGENT_ID_FK";
    public static final String LAND_ID_FK = "LAND_ID_FK";
    public static final String FARMER_ID_FK = "FARMER_ID_FK";
    public static final String SITE_ID_FK = "SITE_ID_FK";
    public static final String CITY_ID_FK = "CITY_ID_FK";
    public static final String TOWN_ID_FK = "TOWN_ID_FK";

    public  static final  int DATABASE_VERSION = 1;
    public  static final  String DATABASE_NAME = "ASDB";

    public static boolean isPermissionGranted (Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();
        }
        else
        {
            int readExtStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExtStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

}
