package com.abraham.androidroomiak.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;

import static com.abraham.androidroomiak.data.ConfigureTableKontak.COLUMN_ID;
import static com.abraham.androidroomiak.data.ConfigureTableKontak.COLUMN_NAME;

/**
 * Created by KOCHOR on 11/26/2017.
 */
@Entity(tableName = ConfigureTableKontak.TABLE_KONTAK )
public class ConfigureTableKontak {
    //penamaan table database
    public static final String TABLE_KONTAK = "kontak";
    //membuat kolom
    //membuat kolom ID
    public static final String COLUMN_ID = BaseColumns._ID;
    //membuat kolom nama
    public static final String COLUMN_NAME = "nama";

    //konfigurasi/pengaturan table
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID )
    public long id;

    @ColumnInfo(name = COLUMN_NAME)
    public String nama;

    public static ConfigureTableKontak fromContentValues(ContentValues contentValues){
        final ConfigureTableKontak configureTableKontak = new ConfigureTableKontak();
        if (contentValues.containsKey(COLUMN_ID)){
            configureTableKontak.id = contentValues.getAsLong(COLUMN_ID);
        }
        if (contentValues.containsKey(COLUMN_NAME)){
            configureTableKontak.nama = contentValues.getAsString(COLUMN_NAME);
        }
       return configureTableKontak;
    }
    static final String[] KONTAKARRAY = {
            "Android",
            "IOS",
            "Tizen",
            "Windows Phone"
    };

}
