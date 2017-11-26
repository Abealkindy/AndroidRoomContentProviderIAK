package com.abraham.androidroomiak.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

/**
 * Created by KOCHOR on 11/26/2017.
 */
@Database(entities = {ConfigureTableKontak.class}, version = 1)
public abstract class DBKontak extends RoomDatabase {

    public abstract KontakDao kontakDao();
    private static DBKontak dbKontak;

    public static synchronized DBKontak getDbKontak(Context context){
        if (dbKontak == null){
            dbKontak = Room.databaseBuilder(context.getApplicationContext(),DBKontak.class,"kontak" )
                    .build();
            dbKontak.populateInitialData();
        }
        return dbKontak;
    }

    @VisibleForTesting
    public static void switchToMemory(Context context){
        dbKontak = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),DBKontak.class).build();
    }
    private void populateInitialData() {
        if (kontakDao().count() == 0){
            ConfigureTableKontak configureTableKontak = new ConfigureTableKontak();
            beginTransaction();
            try{
             for (int i = 0; i<ConfigureTableKontak.KONTAKARRAY.length; i++){
                 configureTableKontak.nama = ConfigureTableKontak.KONTAKARRAY[i];
                 kontakDao().insert(configureTableKontak);
             }
             setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
