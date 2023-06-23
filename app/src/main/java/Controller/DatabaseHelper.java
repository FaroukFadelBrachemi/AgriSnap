package Controller;

import static Utils.Utils.TABLE_AGENT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static Utils.Utils.*;

import Models.City;
import Models.Image;
import Models.Land;
import Models.Site;
import Models.Town;
import Utils.Utils;


public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public DatabaseHelper( @Nullable Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMAGE_NAME + " TEXT, "
                + IMAGE_PATH + " TEXT, "
                + IMAGE_DEVICENAME + " TEXT, "
                + IMAGE_TIME + " TEXT, "
                + IMAGE_DATE + " TEXT, "
                + IMAGE_LAT + " TEXT, "
                + IMAGE_LON + " TEXT, "
                + LAND_ID_FK + " INTEGER, "
                + "FOREIGN KEY (LAND_ID_FK) REFERENCES " + TABLE_LAND + "(" + LAND_ID + ") "
                + ");";

        String CREATE_LAND_TABLE = "CREATE TABLE " + TABLE_LAND + "("
                + LAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LAND_NAME + " TEXT, "
                + SITE_ID_FK + " INTEGER, "
                + "FOREIGN KEY (SITE_ID_FK) REFERENCES " + TABLE_SITE + "(" + SITE_ID + ")  "
                + ");";

        String CREATE_SITE_TABLE = "CREATE TABLE " + TABLE_SITE + "("
                + SITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SITE_NAME + " TEXT, "
                + TOWN_ID_FK + " INTEGER, "
                + "FOREIGN KEY (TOWN_ID_FK) REFERENCES " + TABLE_TOWN + "(" + TOWN_ID + ") "
                + ");";

        String CREATE_TOWN_TABLE = "CREATE TABLE " + TABLE_TOWN + "("
                + TOWN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TOWN_NAME + " TEXT, "
                + CITY_ID_FK + " INTEGER, "
                + "FOREIGN KEY (CITY_ID_FK) REFERENCES " + TABLE_CITY + "(" + CITY_ID + ") "
                + ");";

        String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_CITY + "("
                + CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CITY_NAME + " TEXT "
                + ");";

        db.execSQL(CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_LAND_TABLE);
        db.execSQL(CREATE_SITE_TABLE);
        db.execSQL(CREATE_TOWN_TABLE);
        db.execSQL(CREATE_CITY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOWN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);

        onCreate(db);
    }
    public void addLand(Land land,int SITEIDFK){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LAND_NAME , land.getLandName());
        contentValues.put(SITE_ID_FK , SITEIDFK);
        database.insert(TABLE_LAND , null,contentValues);
        database.close();

    }
    public void addSite(Site site,int TOWNIDFK){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SITE_NAME , site.getSiteName());
        contentValues.put(TOWN_ID_FK , TOWNIDFK);
        database.insert(TABLE_SITE , null,contentValues);
        database.close();
    }
    public void addTown(Town town,int CITYIDFK){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOWN_NAME , town.getTownName());
        contentValues.put(CITY_ID_FK , CITYIDFK);
        database.insert(TABLE_TOWN , null,contentValues);
        database.close();
    }
    public void addImage(Image image, int LANDIDFK){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_NAME, image.getImageName());
            contentValues.put(IMAGE_PATH, image.getImagePath());
            contentValues.put(IMAGE_DEVICENAME, image.getImageDeviceName());
            contentValues.put(IMAGE_TIME, image.getImageTime());
            contentValues.put(IMAGE_DATE, image.getImageDate());
            contentValues.put(IMAGE_LON, image.getImageLon());
            contentValues.put(IMAGE_LAT, image.getImageLat());
            contentValues.put(LAND_ID_FK, LANDIDFK);
            database.insert(TABLE_IMAGE, null, contentValues);
            database.close();

        }catch (SQLiteException e) {
            String errorMessage = e.getMessage();
            Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show();
        }


    }
    public void addCity(City city){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_NAME , city.getCityName());
        database.insert(TABLE_CITY , null,contentValues);
        database.close();
    }

    public  City getCity(String name){
        SQLiteDatabase database = this.getReadableDatabase() ;
        Cursor cursor = database.query(TABLE_CITY ,
                new String[]{CITY_ID,CITY_NAME,
                },
                CITY_NAME + "=?",new String[]{name},
                null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();
        City City = new City(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        return City;
    }

    public  Land getLand(int id){
        SQLiteDatabase database = this.getReadableDatabase() ;
        Cursor cursor = database.query(TABLE_LAND ,
                new String[]{LAND_ID,LAND_NAME,
                },
                LAND_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();
        Land Land = new Land(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        return Land;
    }

    public  Town getTown(int id){
        SQLiteDatabase database = this.getReadableDatabase() ;
        Cursor cursor = database.query(TABLE_TOWN ,
                new String[]{TOWN_ID,TOWN_NAME,
                },
                TOWN_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();
        Town Town = new Town(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        return Town;
    }

    public  Site getSite(int id){
        SQLiteDatabase database = this.getReadableDatabase() ;
        Cursor cursor = database.query(TABLE_SITE ,
                new String[]{SITE_ID,SITE_NAME,
                },
                SITE_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();
        Site Site = new Site(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        return Site;
    }

    public ArrayList<Town> getAllTowns(int cityId){
        SQLiteDatabase database = this.getReadableDatabase() ;
        ArrayList<Town> TownList = new ArrayList<>();

        String getAll = "SELECT * FROM "+ TABLE_TOWN +" WHERE "+ CITY_ID_FK+ "="+ cityId +";";

        Cursor cursor = database.rawQuery(getAll ,null );

        if (cursor.moveToFirst())

            do {
                Town Town = new Town();

                Town.setTownId(cursor.getInt(0));
                Town.setTownName(cursor.getString(1));

                TownList.add(Town);

            } while (cursor.moveToNext());

        database.close();


        return  TownList;

    }

    public ArrayList<Land> getAllLands(int siteId) {
        SQLiteDatabase database = this.getReadableDatabase() ;
        ArrayList<Land> LandList = new ArrayList<>();

        String getAll = "SELECT * FROM "+ TABLE_LAND +" WHERE "+ SITE_ID_FK+ "="+ siteId +";";

        Cursor cursor = database.rawQuery(getAll ,null );

        if (cursor.moveToFirst())

            do {
                Land Land = new Land();

                Land.setLandId(cursor.getInt(0));

                Land.setLandName(cursor.getString(1));

                LandList.add(Land);

            } while (cursor.moveToNext());
        database.close();


        return  LandList;
    }

    public ArrayList<Site> getAllSites(int townId) {
        SQLiteDatabase database = this.getReadableDatabase() ;
        ArrayList SiteList = new ArrayList<>();

        String getAll = "SELECT * FROM "+ TABLE_SITE +" WHERE "+ TOWN_ID_FK+ "="+ townId +";";

        Cursor cursor = database.rawQuery(getAll ,null );

        if (cursor.moveToFirst())

            do {
                Site Site = new Site();

                Site.setSiteId(cursor.getInt(0));
                Site.setSiteName(cursor.getString(1));

                SiteList.add(Site);

            } while (cursor.moveToNext());


        return  SiteList;
    }

    public ArrayList<Image> getAllImages(int landId) {
        SQLiteDatabase database = this.getReadableDatabase() ;
        ArrayList ImageList = new ArrayList<>();

        String getAll = "SELECT * FROM "+ TABLE_IMAGE +" WHERE "+ LAND_ID_FK+ "="+ landId +";";

        Cursor cursor = database.rawQuery(getAll ,null );

        if (cursor.moveToFirst())

            do {
                Image Image = new Image();

                Image.setImageId(cursor.getInt(0));
                Image.setImageName(cursor.getString(1));
                Image.setImagePath(cursor.getString(2));
                Image.setImageDeviceName(cursor.getString(3));
                Image.setImageTime(cursor.getString(4));
                Image.setImageDate(cursor.getString(5));
                Image.setImageLat(cursor.getString(6));
                Image.setImageLon(cursor.getString(7));

                ImageList.add(Image);

            } while (cursor.moveToNext());

        return  ImageList;
    }
    public int countImages(int landId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_IMAGE ,
                new String[]{IMAGE_ID,LAND_ID_FK
                },
                LAND_ID_FK + "=?",new String[]{String.valueOf(landId)},
                null,null,null,null);

        int result=cursor.getCount();
        cursor.close();
        database.close();

        return result;
    }

    @SuppressLint("Range")
    public Image getFirstImage(int landId) {
        SQLiteDatabase database = this.getReadableDatabase() ;
        Cursor cursor = database.query(TABLE_IMAGE ,
                new String[]{IMAGE_ID,IMAGE_NAME,IMAGE_PATH,IMAGE_DEVICENAME,IMAGE_TIME,IMAGE_DATE,IMAGE_LAT,IMAGE_LON,LAND_ID_FK
                },
                LAND_ID_FK + "=?",new String[]{String.valueOf(landId)},
                null,null,null,String.valueOf(1));

        Image Image = new Image();

        if (cursor.moveToFirst())
            do {
                Image.setImageId(cursor.getInt(cursor.getColumnIndex(IMAGE_ID)));
                Image.setImageName(cursor.getString(cursor.getColumnIndex(IMAGE_NAME)));
                Image.setImagePath(cursor.getString(cursor.getColumnIndex(IMAGE_PATH)));
                Image.setImageDeviceName(cursor.getString(cursor.getColumnIndex(IMAGE_DEVICENAME)));
                Image.setImageTime(cursor.getString(cursor.getColumnIndex(IMAGE_TIME)));
                Image.setImageDate(cursor.getString(cursor.getColumnIndex(IMAGE_DATE)));
                Image.setImageLat(cursor.getString(cursor.getColumnIndex(IMAGE_LAT)));
                Image.setImageLon(cursor.getString(cursor.getColumnIndex(IMAGE_LON)));

            } while (cursor.moveToNext());

        cursor.close();
        database.close();

        return  Image;
    }


    public  List<String> getCities(){
        SQLiteDatabase database = this.getReadableDatabase() ;
        Cursor cursor = database.query(Utils.TABLE_CITY, new String[]{Utils.CITY_NAME}, null, null, null, null, null);

        List<String> citiesList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String cityName = cursor.getString(cursor.getColumnIndexOrThrow(Utils.CITY_NAME));
            citiesList.add(cityName);
        }

        cursor.close();

        return citiesList;
    }

       public void deleteImage(Image image){
        SQLiteDatabase database = this.getWritableDatabase() ;
        database.delete(TABLE_IMAGE , IMAGE_ID + "=?",
                new String[]{ String.valueOf(image.getImageId())});
        database.close();

    }

}
