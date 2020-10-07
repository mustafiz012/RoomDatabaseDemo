package com.musta.roomdemo.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.musta.roomdemo.daos.WordDao;
import com.musta.roomdemo.db.WordRoomDatabase;
import com.musta.roomdemo.models.Word;

import java.util.List;

public class WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> allWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        wordDao = db.wordDao();
        allWords = wordDao.getAlphabetizedWords();
    }

    //Observed LiveData notifies the observer :: queries will be executed in separate thread
    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    //This will be run in a non-UI thread or will be thrown an exception
    public void insert(Word word) {
        WordRoomDatabase.databaseWriterExecutor.execute(() -> {
            wordDao.insert(word);
        });
    }
}
