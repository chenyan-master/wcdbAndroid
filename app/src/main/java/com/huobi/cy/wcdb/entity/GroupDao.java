package com.huobi.cy.wcdb.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM hgroup")
    List<HGroup> getAll();

    @Query("SELECT * FROM hgroup WHERE groupid = :groupId")
    HGroup getById(String groupId);

    @Insert
    void insert(HGroup... groups);

    @Delete
    void delete(HGroup group);

    @Query("DELETE FROM hgroup")
    void deleteAll();

    @Update
    void update(HGroup group);
}
