package com.huobi.cy.wcdb.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messagebody")
    List<MessageBody> getAll();

    @Query("SELECT * FROM messagebody WHERE msgid = :msgId")
    MessageBody getById(int msgId);

    @Insert
    void insert(MessageBody... messageBodies);

    @Insert
    void insertAll(List<MessageBody> messageList);

    @Delete
    void delete(MessageBody messageBody);

    @Query("DELETE FROM messagebody")
    void deleteAll();

    @Update
    void update(MessageBody messageBody);
}
