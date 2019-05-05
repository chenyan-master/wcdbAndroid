package com.huobi.cy.wcdb.entity;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class, MessageBody.class, HGroup.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract MessageDao messageDao();

    public abstract GroupDao groupDao();
}
