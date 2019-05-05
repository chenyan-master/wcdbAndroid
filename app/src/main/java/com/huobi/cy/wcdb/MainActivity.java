package com.huobi.cy.wcdb;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.huobi.cy.wcdb.databinding.ActivityMainBinding;
import com.huobi.cy.wcdb.entity.AppDatabase;
import com.huobi.cy.wcdb.entity.GroupDao;
import com.huobi.cy.wcdb.entity.HGroup;
import com.huobi.cy.wcdb.entity.MessageBody;
import com.huobi.cy.wcdb.entity.MessageDao;
import com.huobi.cy.wcdb.entity.User;
import com.huobi.cy.wcdb.entity.UserDao;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private MessageDao messageDao;
    private GroupDao groupDao;

    private final int DATA_COUNT = 1;//写库数量

    ProgressDialog progressDialog = null;

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
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


        //系统原生sqlite
//        mAppDB = Room.databaseBuilder(this, AppDatabase.class, "app-oridb")
//                .allowMainThreadQueries()
//                .build();

        mainBinding.btnReaddb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testReadDB();
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

        mainBinding.btnSecondWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownWriteDB();
            }
        });

    }

    /**
     * read database
     */
    public void testReadDB() {
        Observable.create(new ObservableOnSubscribe<Pair<String, Long>>() {
            @Override
            public void subscribe(ObservableEmitter<Pair<String, Long>> emitter) throws Exception {
                long start = System.currentTimeMillis();
                final String users = readMessages();
                final long cost = System.currentTimeMillis() - start;
                emitter.onNext(new Pair<String, Long>(users, cost));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<String, Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if(!progressDialog.isShowing()) {
                            progressDialog.setMessage("waiting...");
                            progressDialog.show();
                        }
                    }

                    @Override
                    public void onNext(Pair<String, Long> stringIntegerPair) {
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        mainBinding.btnReaddb.setText("读数据库 " + String.format("花费时间: %d ms", stringIntegerPair.second));
                        mainBinding.tvData.setText(stringIntegerPair.first);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * write database
     */
    public void testWriteDB() {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                long start = System.currentTimeMillis();
                writeMessages();
                final long cost = System.currentTimeMillis() - start;
                emitter.onNext(cost);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                if(!progressDialog.isShowing()) {
                    progressDialog.setMessage("waiting...");
                    progressDialog.show();
                }
            }

            @Override
            public void onNext(Long aLong) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                mainBinding.btnWritedb.setText("写数据库 " + String.format("花费时间: %d ms", aLong));
            }

            @Override
            public void onError(Throwable e) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * delete database
     */
    public void delDBData() {
        if(!progressDialog.isShowing()) {
            progressDialog.setMessage("waiting...");
            progressDialog.show();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                deleteMessageDB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    /**
     * 写用户表
     */
    public void writeUsers() {
        mUsers = mAppDB.userDao();
        User user = new User();
        int radomid = new Random().nextInt();
        user.firstName = "YAN" + radomid;
        user.lastName = "CHEN";
        user.mobile = "18310417569";
        user.account = String.valueOf(radomid);
        user.email = "chenyandkfakdks@163.com";
        user.summary = "hello world!";
        user.legalizetag = "good";
        user.logo = "www.baidu.com";
        user.biglogo = "www.baidu.com";
        user.fullspell = "chenyan";
        user.initspell = "chenyan";
        user.crowntype = 0;
        user.authtype = 1;
        user.champflag = 1;
        user.isfriend = 1;
        mUsers.insert(user);
    }

    /**
     * 读用户表
     * @return
     */
    public String readUsers() {
        mUsers = mAppDB.userDao();
        List<User> userList = mUsers.getAll();

        StringBuilder sb = new StringBuilder();
        if(userList.size() > 100) {
            userList = userList.subList(0,100);
        }
        for(User user1 : userList) {
            sb.append(user1.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 删除用户表
     */
    public void deleteUserDB() {
        mUsers = mAppDB.userDao();
        mUsers.deleteAll();
    }


    /**
     * 写消息表
     */
    public void writeMessages() {
        messageDao = mAppDB.messageDao();
        List<MessageBody> messageBodyList = new ArrayList<>();
        for(int i = 0;i < DATA_COUNT;i ++) {
            MessageBody messageBody = new MessageBody();
            int radomid = new Random().nextInt();
            messageBody.msgid = radomid;
            messageBody.msgtype = 0;
            messageBody.content = "我是一条测试消息" + radomid;
            messageBody.fromgroup = "dlsfasfda sdfaf ";
            messageBody.fromuser = "chenyan";
            messageBody.remark = "hello world!";
            messageBody.sendtime = "2019-04-25 14:19";
            messageBody.sendstate = 1;
            messageBody.sendflag = 1;
            messageBody.totype = 0;
            messageBody.localpath = "www.baidu.com";
            messageBody.fileurl = "www.baidu.com";
            messageBody.orilocalpath = "www.baidu.com";
            messageBody.revokeflag = 1;
            messageBody.atflag = 0;
            messageBodyList.add(messageBody);
        }
        messageDao.insertAll(messageBodyList);
    }

    /**
     * 读消息表
     * @return
     */
    public String readMessages() {
        messageDao = mAppDB.messageDao();
        List<MessageBody> messageBodyList = messageDao.getAll();

        StringBuilder sb = new StringBuilder();
        sb.append("size: ").append(messageBodyList.size()).append("->");
        if(messageBodyList.size() > 100) {
            messageBodyList = messageBodyList.subList(0, 100);
        }
        for(MessageBody messageBody : messageBodyList) {
            sb.append(messageBody.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 删除消息表
     */
    public void deleteMessageDB() {
        messageDao = mAppDB.messageDao();
        messageDao.deleteAll();
    }


    /**
     * 写群组表
     */
    public void writeGroups() {
        groupDao = mAppDB.groupDao();
        HGroup group = new HGroup();
        int radomid = new Random().nextInt();
        group.groupid = String.valueOf(radomid);
        group.groupname = "我创建的群" + radomid;
        group.groupinfo = "我是一个群 helloworld";
        group.craccount = "chenyan";
        group.grouplogo = "www.baidu.com";
        group.groupstate = 1;
        group.groupboard = "我是一条群公告";
        group.maxmember = 10000;
        group.entrytime = "2019-04-25 14:19";
        group.role = 1;
        group.path = "path1/asfsadfda/wwweeeeee";
        group.surl = "group1.surl.com";
        group.detail = "北京市某区某村某个角落";
        group.title = 1;
        group.topmsg = "我的一条置顶消息";
        group.banchat = 1;
        group.banmsg = 0;
        group.banurl = 1;
        group.banpic = 1;
        group.banscreenshot = 1;
        group.banall = 1;
        group.banuser = 1;
        groupDao.insert(group);
    }

    /**
     * 读群组表
     * @return
     */
    public String readGroups() {
        groupDao = mAppDB.groupDao();
        List<HGroup> hGroupList = groupDao.getAll();

        StringBuilder sb = new StringBuilder();
        if(hGroupList.size() > 100) {
            hGroupList = hGroupList.subList(0, 100);
        }
        for(HGroup hGroup : hGroupList) {
            sb.append(hGroup.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 删除群组表
     */
    public void deleteGroupDB() {
        groupDao = mAppDB.groupDao();
        groupDao.deleteAll();
    }

    public void multiThreadWriteDB() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long start = System.currentTimeMillis();
                    writeMessages();
                    final long cost = System.currentTimeMillis() - start;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mainBinding.btnMthreadWrite.setText("写数据库 " + String.format("花费时间: %d ms", cost));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void multiThreadReadDB() {
        for(int i = 0;i < 5;i ++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                        long start = System.currentTimeMillis();
                        final String result = readMessages();
                        final long cost = System.currentTimeMillis() - start;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                mainBinding.btnMthreadRead.setText("读数据库 " + String.format("花费时间: %d ms", cost));
                                System.out.println("花费时间: " + cost);
                                mainBinding.tvData.setText(result);
                            }
                        });
                }
            });
        }
    }

    /**
     * 固定时长测试写数据量
     */
    public void countDownWriteDB() {
        if(!progressDialog.isShowing()) {
            progressDialog.setMessage("waiting...");
            progressDialog.show();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                while (true) {
                    writeMessages();
                    final long cost = System.currentTimeMillis() - start;
                    if(cost >= 3000) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                mainBinding.btnSecondWrite.setText("写数据库 " + String.format("花费时间: %d ms", cost));
                            }
                        });
                        break;
                    }
                }
            }
        });
    }

}
