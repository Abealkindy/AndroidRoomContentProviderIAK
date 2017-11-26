package com.abraham.androidroomiak;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abraham.androidroomiak.data.ConfigureTableKontak;
import com.abraham.androidroomiak.provider.ContentProviderKontak;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_contentprovider)
    RecyclerView recyclerContentprovider;

    private static final int LOADER_KONTAK = 1;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerContentprovider.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter();
        recyclerContentprovider.setAdapter(recyclerAdapter);
        getSupportLoaderManager().initLoader(LOADER_KONTAK, null, loaderCallBack);
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id){
                case LOADER_KONTAK:
                    return new CursorLoader(getApplicationContext(),
                            ContentProviderKontak.URL_CONTENT_PROVIDER,
                            new String[]{ConfigureTableKontak.COLUMN_NAME},
                            null, null, null);
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            switch (loader.getId()){
                case LOADER_KONTAK:
                    recyclerAdapter.setRecycler(data);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            switch (loader.getId()){
                case LOADER_KONTAK:
                    recyclerAdapter.setRecycler(null);
                    break;
            }
        }
    };
    private class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.ViewHolder>{
        private Cursor cursor;
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent) ;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (cursor.moveToPosition(position)){
                holder.textRecycler.setText(cursor.getString(
                        cursor.getColumnIndexOrThrow(ConfigureTableKontak.COLUMN_NAME)));
            }
        }

        @Override
        public int getItemCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        public void setRecycler(Cursor data) {
            cursor = data;
            notifyDataSetChanged();
        }
         class ViewHolder extends RecyclerView.ViewHolder{
            TextView textRecycler;
            public ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
                textRecycler = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
