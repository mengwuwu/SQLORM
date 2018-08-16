package com.example.wumeng.testjava.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private String TAG = DBHelper.class.getCanonicalName();
    private static String DATABASE_NAME="default.db";
    private static int DATABASE_VERSION = 0;
    private static DBHelper _instance = null;
    public final static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String PATH_DB = SDPATH + File.separator
            + "wmtestorm" + File.separator + "db" + File.separator;
    private static boolean checkDirExist() {
        File file = new File(PATH_DB);

        if (!file.exists()) {
            file.mkdirs();
        }
        return true;
    }
    private ICreateDB iCreateDB;
    private IUpdateDB iUpdateDB;

    private DBHelper(Context context, String name,int version,ICreateDB iCreateDB,IUpdateDB iUpdateDB) {
        super(context, PATH_DB + name +".db", null, version);
        DATABASE_NAME = name;
        DATABASE_VERSION = version;
        this.iCreateDB = iCreateDB;
        this.iUpdateDB = iUpdateDB;
    }

    public static DBHelper getInstance(Context context,String name,int version,ICreateDB iCreateDB,IUpdateDB iUpdateDB) {
        if (_instance == null) {
            synchronized (DBHelper.class) {
                if (_instance == null) {
                    checkDirExist();//检查目录是否存在
                    _instance = new DBHelper(context,name,version,iCreateDB,iUpdateDB);
                }
            }
        }
        return _instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  db.close();
        DbOperator dbOperator = DbOperator.getInstance(this);
        dbOperator.setDb(db);
        if(iCreateDB!=null){
            iCreateDB.onCreate(dbOperator);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  db.close();
        DbOperator dbOperator = DbOperator.getInstance(this);
        if(iUpdateDB!=null){
            iUpdateDB.onUpgrade(dbOperator);
        }
    }
}
