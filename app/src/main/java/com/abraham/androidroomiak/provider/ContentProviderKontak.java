package com.abraham.androidroomiak.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.abraham.androidroomiak.data.ConfigureTableKontak;
import com.abraham.androidroomiak.data.DBKontak;
import com.abraham.androidroomiak.data.KontakDao;

import java.util.ArrayList;

/**
 * Created by KOCHOR on 11/26/2017.
 */

public class ContentProviderKontak extends ContentProvider {

    public static final String NAMA_PACKAGE = "com.abraham.androidroomiak.provider";
    public static final Uri URL_CONTENT_PROVIDER = Uri.parse(
      "content://" + NAMA_PACKAGE + "/" + ConfigureTableKontak.TABLE_KONTAK
    );

    private static final int CODE_KONTAK_DIR = 1;
    private static final int CODE_KONTAK_ITEM = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(NAMA_PACKAGE, ConfigureTableKontak.TABLE_KONTAK, CODE_KONTAK_DIR);
        MATCHER.addURI(NAMA_PACKAGE, ConfigureTableKontak.TABLE_KONTAK + "/#", CODE_KONTAK_ITEM);
    }
    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] stringarray, @Nullable String singlestring, @Nullable String[] stringarray2, @Nullable String singlestring2) {

        final int code = MATCHER.match(uri);
        if (code == CODE_KONTAK_DIR || code == CODE_KONTAK_ITEM){
            final Context context = getContext();
            if (context == null){
                return null;
            }
            KontakDao kontakDao = DBKontak.getDbKontak(context).kontakDao();
            final Cursor cursor;
            if (code == CODE_KONTAK_DIR){
                cursor = kontakDao.selectAll();
            } else {
                cursor = kontakDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)){
            case CODE_KONTAK_DIR:
                return "vnd.android.cursor.dir/" + NAMA_PACKAGE + "." + ConfigureTableKontak.TABLE_KONTAK;
            case CODE_KONTAK_ITEM:
                return "vnd.android.cursor.item/" + NAMA_PACKAGE + "." + ConfigureTableKontak.TABLE_KONTAK;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (MATCHER.match(uri)){
            case CODE_KONTAK_DIR:
                final Context context = getContext();
                if (context == null){
                    return null;
                }
                final long id = DBKontak.getDbKontak(context).kontakDao().insert(ConfigureTableKontak.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_KONTAK_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (MATCHER.match(uri)){
            case CODE_KONTAK_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            case CODE_KONTAK_ITEM:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }
                final int count = DBKontak.getDbKontak(context).kontakDao().deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (MATCHER.match(uri)){
            case CODE_KONTAK_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            case CODE_KONTAK_ITEM:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }
                final ConfigureTableKontak configureTableKontak = ConfigureTableKontak.fromContentValues(contentValues);
                configureTableKontak.id = ContentUris.parseId(uri);
                final int count = DBKontak.getDbKontak(context).kontakDao().update(configureTableKontak);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (MATCHER.match(uri)){
            case CODE_KONTAK_DIR:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }
                final DBKontak dbKontak = DBKontak.getDbKontak(context);
                final ConfigureTableKontak[] configureTableKontaks = new ConfigureTableKontak[values.length];
                for (int i = 0; i < values.length; i++){
                    configureTableKontaks[i] = ConfigureTableKontak.fromContentValues(values[i]);
                }
                return dbKontak.kontakDao().insertAll(configureTableKontaks).length;
            case CODE_KONTAK_ITEM:
                throw new IllegalArgumentException("Unknown URI: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
       final Context context = getContext();
        if (context == null){
            return new ContentProviderResult[0];
        }
        final DBKontak dbKontak = DBKontak.getDbKontak(context);
        dbKontak.beginTransaction();
        try{
            final ContentProviderResult[]results = super.applyBatch(operations);
            dbKontak.setTransactionSuccessful();
            return results;
        } finally {
            dbKontak.endTransaction();
        }
    }
}
