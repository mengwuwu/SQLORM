package com.example.wumeng.testjava.db;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wumeng.testjava.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by WuMeng on 2018/8/13.
 */

public class TestAcitvity extends Activity implements View.OnClickListener{
    private String TAG = TestAcitvity.class.getCanonicalName();
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private Button create,insert,update,query,delete,drop;
    private TextView tv;
    private List<Book> books = new ArrayList<>();
    private DbOperator dbOperator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_test);
        initView();
        initDate();
    }
    private void initView(){
        create =(Button)findViewById(R.id.create);
        insert = (Button)findViewById(R.id.insert);
        update = (Button)findViewById(R.id.update);
        query = (Button)findViewById(R.id.query);
        delete = (Button)findViewById(R.id.delete);
        drop = (Button)findViewById(R.id.drop);
        tv = (TextView)findViewById(R.id.show);

        create.setOnClickListener(this);
        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
        delete.setOnClickListener(this);
        drop.setOnClickListener(this);

    }
    private void initDate(){
        for(int i = 0;i<4;i++){
            Book book = new Book();
            book.setAuthor("刘同");
            book.setBookName("青茫");
            book.setVersion(3);
            books.add(book);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"创建表线程");
                        dbOperator =  DbManager.with(TestAcitvity.this).setName("testorm").setVersion(1).create(new ICreateDB() {
                            @Override
                            public void onCreate(DbOperator dbOperator) {
                             Log.i(TAG,"创建表开始");
                             dbOperator.excute(dbOperator.create(books.get(0)));
                            }
                        }).getDbOperator();
                    }
                });
                break;
            case R.id.insert:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"插入表线程");
                        if(dbOperator == null)return;
                        for(Book book : books)
                        dbOperator.insert(book).excute();
                    }
                });
                break;
            case R.id.update:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"更新表线程");
                        if(dbOperator == null)return;
                        Book book = books.get(3);
                        book.setAuthor("马尔克斯");
                        book.setBookName("百年孤独");
                        dbOperator.update(book).where("id = \"3\"").excute();
                    }
                });
                break;
            case R.id.query:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"查询表线程");
                        if(dbOperator == null)return;
                       final List<Book> list = dbOperator.query(new Book()).where("author = \"马尔克斯\"").excuteQuery(new Book());
                            tv.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(list!=null&&list.size()>0){
                                        Book book = list.get(0);
                                        tv.setText(book.getAuthor()+"："+
                                                book.getBookName());
                                    }
                                }
                            });

                    }
                });
                break;
            case R.id.delete:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"删除表内容线程");
                        if(dbOperator == null)return;
                        dbOperator.delete(new Book()).where("author = \"刘同\"").excute();
                    }
                });
                break;
            case R.id.drop:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"删除表线程");
                        if(dbOperator == null)return;
                        dbOperator.drop(new Book()).excute();
                    }
                });
                break;

        }

    }
}

