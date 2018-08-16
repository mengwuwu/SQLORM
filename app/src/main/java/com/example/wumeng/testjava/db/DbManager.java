package com.example.wumeng.testjava.db;

import android.content.Context;
import android.util.Log;

/**
 * Created by WuMeng on 2018/8/9.
 */

public class DbManager {
    private String TAG = DbManager.class.getCanonicalName();
    private String db_name = "";
    private int version = 0;
    private ICreateDB iCreateDB;
    private IUpdateDB iUpdateDB;
    private Context context;
    private DBHelper dbHelper;
    private static DbManager dbManager;
    private DbOperator dbOperator;
    private DbManager(Context context){
        this.context = context;
    }
    public static DbManager with(Context context){
        if(dbManager == null){
            synchronized (DbManager.class) {
                if(dbManager == null)
                    dbManager = new DbManager(context) ;
            }
        }
        return dbManager;
    }
    public DbManager setName(String db_name){
        this.db_name = db_name;
        return this;
    }
    public DbManager setVersion(int version){
        this.version = version;
        return this;
    }
    public DbManager create(ICreateDB iCreateDB){
        this.iCreateDB = iCreateDB;
        return this;
    }
    public DbManager update(IUpdateDB iUpdateDB){
        this.iUpdateDB = iUpdateDB;
        return this;
    }
    public DbOperator getDbOperator(){
        dbHelper = DBHelper.getInstance(context,db_name,version,iCreateDB,iUpdateDB);
        if(dbHelper!=null){
            dbOperator = DbOperator.getInstance(dbHelper);
            return dbOperator;
        }else{
            Log.i(TAG,"DBhelper为null，打开数据库失败");
            return null;
        }
    }

}
