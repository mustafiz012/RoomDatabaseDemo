package com.musta.roomdemo.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.musta.roomdemo.daos.WordDao;
import com.musta.roomdemo.models.Word;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREAD = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
    private static volatile WordRoomDatabase instance;
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            /*databaseWriterExecutor.execute(() -> {
                WordDao dao = instance.wordDao();
                dao.deleteAll();

                Word word = new Word("Musta");
                dao.insert(word);
                word = new Word("Room");
                dao.insert(word);
                word = new Word("Database");
                dao.insert(word);
            });*/
        }
    };

    public static WordRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (WordRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), WordRoomDatabase.class, "word_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract WordDao wordDao();
}
