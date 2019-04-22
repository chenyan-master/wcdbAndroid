package com.huobi.cy.wcdb;

import android.arch.persistence.room.Room;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.huobi.cy.wcdb.databinding.ActivityMainBinding;
import com.huobi.cy.wcdb.entity.AppDatabase;
import com.huobi.cy.wcdb.entity.User;
import com.huobi.cy.wcdb.entity.UserDao;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory;

import java.util.List;
import java.util.Random;

/**
 * @author chenyan@huobi.com
 * @date 2019/4/19 下午7:24
 * @desp
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding = null;

    private static final SQLiteCipherSpec sCipherSpec = new SQLiteCipherSpec()
            .setPageSize(4096)
            .setKDFIteration(64000);

    private AppDatabase mAppDB;
    private UserDao mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAppDB = Room.databaseBuilder(this, AppDatabase.class, "app-db")
                .allowMainThreadQueries()
                //使用wcdb的api,如果注释掉factory则是使用系统sqlite api
                .openHelperFactory(new WCDBOpenHelperFactory()
                        .passphrase("passphrase".getBytes())
                        .cipherSpec(sCipherSpec)
                        .writeAheadLoggingEnabled(true)
                        .asyncCheckpointEnabled(true)
                )
//                .addCallback(new RoomDatabase.Callback() {
//                    @Override
//                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                        SQLiteDatabase realDb = ((WCDBDatabase) db).getInnerDatabase();
//
//                        realDb.addExtension(MMFtsTokenizer.EXTENSION);
//                    }
//                })
                .build();

        mainBinding.btnReaddb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.tvData.setText(testReadDB());
            }
        });

        mainBinding.btnWritedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testWriteDB();
            }
        });

        mainBinding.btnDeldb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delDBData();
            }
        });

        mainBinding.btnMthreadRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiThreadReadDB();
            }
        });

        mainBinding.btnMthreadWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiThreadWriteDB();
            }
        });

    }

    /**
     * read database
     */
    public String testReadDB() {
        mUsers = mAppDB.userDao();
        List<User> userList = mUsers.getAll();

        StringBuilder sb = new StringBuilder();
        for(User user1 : userList) {
            sb.append(user1.lastName + " " + user1.firstName).append("\n");
        }

        return sb.toString();
    }

    /**
     * write database
     */
    public void testWriteDB() {
        mUsers = mAppDB.userDao();
        User user = new User();
        user.firstName = "YAN" + new Random().nextInt();
        user.lastName = "CHEN";
        mUsers.insert(user);
    }

    /**
     * delete database
     */
    public void delDBData() {
        mUsers = mAppDB.userDao();
        mUsers.deleteAll();
    }

    public void multiThreadWriteDB() {
        final long start = System.currentTimeMillis();
        for(int i = 0;i < 100;i ++) {
            new Thread() {
                @Override
                public void run() {
                    testWriteDB();
                    Log.d(MainActivity.class.getSimpleName(), "multiThreadWriteDB: " + (System.currentTimeMillis() - start));
                }
            }.start();
        }
    }

    public void multiThreadReadDB() {
        final long start = System.currentTimeMillis();
        for(int i = 0;i < 100;i ++) {
            new Thread() {
                @Override
                public void run() {
                    final String data = testReadDB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainBinding.tvData.setText(data);
                        }
                    });
                    Log.d(MainActivity.class.getSimpleName(), "multiThreadReadDB: " + (System.currentTimeMillis() - start));
                }
            }.start();
        }
    }

}
