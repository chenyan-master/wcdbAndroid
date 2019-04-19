package com.huobi.cy.wcdb;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.huobi.cy.wcdb.entity.AppDatabase;
import com.huobi.cy.wcdb.entity.User;
import com.huobi.cy.wcdb.entity.UserDao;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory;

import java.util.List;

/**
 * @author chenyan@huobi.com
 * @date 2019/4/19 下午7:24
 * @desp
 */
public class MainActivity extends AppCompatActivity {

    private static final SQLiteCipherSpec sCipherSpec = new SQLiteCipherSpec()
            .setPageSize(4096)
            .setKDFIteration(64000);

    private AppDatabase mAppDB;
    private UserDao mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppDB = Room.databaseBuilder(this, AppDatabase.class, "app-db")
                .allowMainThreadQueries()
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

        mUsers = mAppDB.userDao();

        User user = new User();
        user.firstName = "John";
        user.lastName = "He";
        mUsers.insert(user);

        user.firstName = "Sanhua";
        user.lastName = "Zhang";
        mUsers.insert(user);

        List<User> userList = mUsers.getAll();

        StringBuilder sb = new StringBuilder();
        for(User user1 : userList) {
            sb.append(user1.firstName + " " + user1.lastName).append("\n");
        }

        TextView tv_data = findViewById(R.id.tv_data);
        tv_data.setText(sb.toString());
    }
}
