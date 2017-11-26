package com.abraham.androidroomiak.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

/**
 * Created by KOCHOR on 11/26/2017.
 */
@Dao
public interface KontakDao {
// mengambil banyaknya data dari table kontak
    @Query("SELECT COUNT(*) FROM " + ConfigureTableKontak.TABLE_KONTAK)
    int count();
    //anotasi insert untuk memasukkan data
    @Insert
    long insert(ConfigureTableKontak configureTableKontak);
    //insert data array
    @Insert
    long[] insertAll(ConfigureTableKontak[]configureTableKontaks);
    //query untuk ngambil data perbaris
    @Query("SELECT*FROM " + ConfigureTableKontak.TABLE_KONTAK + " WHERE " + ConfigureTableKontak.COLUMN_ID + " = :id")
    Cursor selectById(long id);
    //query select all
    @Query("SELECT*FROM " + ConfigureTableKontak.TABLE_KONTAK)
    Cursor selectAll();
    //query delete by ID
    @Query("DELETE FROM " + ConfigureTableKontak.TABLE_KONTAK + " WHERE " + ConfigureTableKontak.COLUMN_ID + " = :id")
    int deleteById(long id);
    //query untuk update data
    @Update
    int update(ConfigureTableKontak configureTableKontak);
}