package com.example.wumeng.testjava.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WuMeng on 2018/8/9.
 */

public class DbOperator {
    private static DbOperator _instance;
    private static AtomicInteger mOpenCounter = new AtomicInteger();//自增长类
    private DBHelper dbHelper;
    private static SQLiteDatabase db;
    public void setDb(SQLiteDatabase db){
        this.db = db;
    }
    private static ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder();
        }
    };
    private DbOperator(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }
    public static DbOperator getInstance(DBHelper dbHelper) {
        if (_instance == null) {
            synchronized (DbOperator.class) {
                if (_instance == null) {
                    _instance = new DbOperator(dbHelper);
                }
            }
        }
        return _instance;
    }
    public  List<StringBuilder> create(List tables){
        List<StringBuilder> list = new ArrayList<>();
        for(int i = 0;i<tables.size();i++){
            StringBuilder sb = create(tables.get(i));
            if (sb == null||TextUtils.isEmpty(sb.toString())){
                continue;
            }
            list.add(sb);
        }

        return list;
    }
    public <T> StringBuilder create(T table){
        StringBuilder sql = new StringBuilder();
        sql.append("create table IF NOT EXISTS ");
        String tableName = getTableName(table);
        if(tableName==null)return null;
        sql.append(tableName);
        sql.append(" ( ");
        Map<String,String> columns = getColumn(table);
        if(columns==null||columns.size()<=0)return null;
        boolean isfirst = true;
        for (Map.Entry<String, String> entry: columns.entrySet()) {
            if(!isfirst)sql.append(", ");
            if(isfirst)isfirst = false;
            sql.append(entry.getKey());
            sql.append(" ");
            sql.append(entry.getValue());
        }
        sql.append(" )");
       return sql;
    }
    public <T> DbOperator  insert(T table){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        String tableName = getTableName(table);
        if(tableName==null||TextUtils.isEmpty(tableName))return null;
        sql.append(tableName);
        Map<String,Object> columns = getColumnValue(table);
        if(columns==null||columns.size()<=0)return null;
        sql.append(" (");
        List<String> names = new ArrayList<>();
        boolean isfirst = true;
        for (String key : columns.keySet()) {
            names.add(key);
            if(!isfirst)sql.append(", ");
            if(isfirst)isfirst = false;
            sql.append(key);
        }
        sql.append(") VALUES (");
        isfirst = true;
        for(int i = 0;i<names.size();i++){
            if(!isfirst)sql.append(", ");
            if(isfirst)isfirst = false;
            sql.append("\"");
            sql.append(columns.get(names.get(i)));
            sql.append("\"");
        }
        sql.append(")");
        threadLocal.set(sql);
        return this;
    }
    public <T> DbOperator delete(T table){
        StringBuilder sql = new StringBuilder();
        String tableName = getTableName(table);
        if(tableName==null||TextUtils.isEmpty(tableName))return null;
        sql.append("DELETE FROM ");
        sql.append(tableName);
        threadLocal.set(sql);
        return this;
    }
    public <T> DbOperator query(T table){
        StringBuilder sql = new StringBuilder();
        String tableName = getTableName(table);
        if(tableName==null||TextUtils.isEmpty(tableName))return null;
        sql.append("SELECT * FROM ");
        sql.append(tableName);
        threadLocal.set(sql);
        return this;
    }
    public <T> DbOperator where(String str){
        StringBuilder sql = threadLocal.get();
        String iscontains = sql.toString().toUpperCase();
        if(!iscontains.contains("SELECT")&&!iscontains.contains("DELETE")&&!iscontains.contains("UPDATE"))return null;
        sql.append(" WHERE ");
        sql.append(str);
        threadLocal.set(sql);
        return this;
    }
    public <T> DbOperator and(String str){
        StringBuilder sql = threadLocal.get();
        if(!sql.toString().toUpperCase().contains("WHERE"))return null;
        sql.append("AND ");
        sql.append(str);
        threadLocal.set(sql);
        return this;
    }
    public <T> DbOperator update(T table){
        StringBuilder sql = new StringBuilder();
        String tableName = getTableName(table);
        if(tableName==null||TextUtils.isEmpty(tableName))return null;
        sql.append("UPDATE ");
        sql.append(tableName);
        Map<String,Object> columns = getColumnValue(table);
        if(columns==null||columns.size()<=0)return null;
        sql.append(" SET ");
        boolean isfirst = true;
        for (Map.Entry<String, Object> entry: columns.entrySet()) {
            if(!isfirst)sql.append(", ");
            if(isfirst)isfirst = false;
            sql.append(entry.getKey());
            sql.append("=");
            sql.append("\"");
            sql.append(entry.getValue());
            sql.append("\"");
        }
        threadLocal.set(sql);
        return this;
    }
    public <T> DbOperator drop(T table){
        StringBuilder sql = new StringBuilder();
        String tableName = getTableName(table);
        if(tableName==null||TextUtils.isEmpty(tableName))return null;
        sql.append("DROP TABLE ");
        sql.append(tableName);
        threadLocal.set(sql);
        return this;
    }
    //其他数据库操作符待补充

    private <T> String getTableName(T o){
        //获取对应的类类型
        Class<?> oClass = o.getClass();
        //判断指定类型的注释是否存在于此元素上
        boolean isHaveTable = oClass.isAnnotationPresent(TableName.class);
        if(!isHaveTable) return null;//不存在就不需要获取其表名
        TableName table = oClass.getAnnotation(TableName.class);//拿到对应的表格注解类型
        return table.value();//返回注解中的值，也就是表名
    }
    private <T> Map<String, String> getColumn(T o){
        //拿到对应的类类型
        Class<?> oClass = o.getClass();
        //获取对象中所有的属性
        Field[] fields = oClass.getDeclaredFields();
        //遍历所有属性
        Map<String,String> cols = new HashMap<>();
        for (Field field : fields) {
            //如果属性上有对应的列注解类型则获取这个注解类型
            Column column = field.getAnnotation(Column.class);
            if(column != null){
                //如果属性上有对应的主键ID注解类型则获取这个注解类型
                TablePrimaryId tpid = field.getAnnotation(TablePrimaryId.class);
                if(tpid != null) {
                    //判断主键是否为自动增长
                    if(tpid.isAutoIncrease()){
                        cols.put(tpid.value(),"integer primary key autoincrement");
                    }else{
                        cols.put(tpid.value(),"integer primary key");
                    }
                }else{
                    //拿到对应属性的类型，然后根据对应的类型去声明字段类型
                    Class<?> type = field.getType();
                    if(type == String.class){
                        cols.put(column.value(),"varchar(50)");
                    }else if(type == int.class){
                        cols.put(column.value(),"int");
                    }else if(type == double.class || type == float.class){
                        cols.put(column.value(),"double");
                    }
                }
            }
        }
        return cols;
    }
    public <T> Map<String,Object> getColumnValue(T o){
        //拿到对应的类类型
        Class<?> oClass = o.getClass();
        //获取对象中所有的属性
        Field[] fields = oClass.getDeclaredFields();
        //遍历所有属性
        Map<String,Object> cols = new HashMap<>();
        Object columnValue = null;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
               columnValue = field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(columnValue!=null){
            //如果属性上有对应的列注解类型则获取这个注解类型
            Column column = field.getAnnotation(Column.class);
            if(column != null){
                //如果属性上有对应的主键ID注解类型则获取这个注解类型
                TablePrimaryId tpid = field.getAnnotation(TablePrimaryId.class);
                if(tpid != null) {
                    //判断主键是否为自动增长
                    if(tpid.isAutoIncrease()){
                        continue;
                    }else{
                        cols.put(tpid.value(),columnValue);
                    }
                }else{
                    cols.put(column.value(),columnValue);
                }
            }
        }
        }
        return cols;
    }
    public void excute(StringBuilder sql){
       try {
           openDatabase();
        if(db!=null){
            db.beginTransaction();
            db.execSQL(sql.toString());
            db.setTransactionSuccessful();
        }
       }catch (Exception e) {
            e.printStackTrace();
        } finally {
           if(db!=null)
           db.endTransaction();
           closeDatabase();
        }
    }
    public void excute(){
        try {
            openDatabase();
            if(db!=null){
                db.beginTransaction();
                StringBuilder sql = threadLocal.get();
                db.execSQL(sql.toString());
                db.setTransactionSuccessful();
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            closeDatabase();
        }
    }
    public <T> List<T> excuteQuery(T bean){
        List<T> list = null;
        Cursor cursor;
        try {
            openDatabase();
            if(db!=null){
                db.beginTransaction();
                StringBuilder sql = threadLocal.get();
                cursor = db.rawQuery(sql.toString(),null);
                list = cursor2VOList(cursor,bean);
                db.setTransactionSuccessful();
                return list;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            closeDatabase();
        }
        return null;
    }
    //打开数据库方法
    private synchronized void openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1||db == null) {//incrementAndGet会让mOpenCounter自动增长1
            // Opening new database
            try {
                db = dbHelper.getWritableDatabase();
            } catch (Exception e) {
                db = dbHelper.getReadableDatabase();
            }
        }
    }

    //关闭数据库方法
    private synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0&&db!=null) {//decrementAndGet会让mOpenCounter自动减1
            // Closing database
            db.close();
        }
    }
    private static List cursor2VOList(Cursor c, Object bean) {
        if (c == null) {
            return null;
        }
        List list = new LinkedList();
        Object obj;
        try {
            while (c.moveToNext()) {
                obj = setValues2Fields(c, bean);
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR @：cursor2VOList");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 把值设置进类属性里
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static Object setValues2Fields(Cursor c, Object bean )
            throws Exception {
        String[] columnNames = c.getColumnNames();// 字段数组
        Object obj = bean;
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field _field : fields) {
            Column column = _field.getAnnotation(Column.class);
            if(column != null){
                _field.setAccessible(true);
            Class<? extends Object> typeClass = _field.getType();// 属性类型
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                typeClass = getBasicClass(typeClass);
                boolean isBasicType = isBasicType(typeClass);
                if (isBasicType) {
                    if (columnName.equalsIgnoreCase(column.value())) {// 是基本类型
                        String _str = c.getString(c.getColumnIndex(columnName));
                        if (_str == null) {
                            break;
                        }
                        _str = _str == null ? "" : _str;
                        Constructor<? extends Object> cons = typeClass
                                .getConstructor(String.class);
                        Object attribute = cons.newInstance(_str);
                        _field.setAccessible(true);
                        _field.set(obj, attribute);
                        break;
                    }
                }
            }
        }}
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        if (typeClass.equals(Integer.class) || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class)) {

            return true;

        } else {
            return false;
        }
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<Class, Class>();
    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }

}
