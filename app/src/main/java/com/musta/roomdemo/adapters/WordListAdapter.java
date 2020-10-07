package com.musta.roomdemo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musta.roomdemo.R;
import com.musta.roomdemo.models.Word;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private static final String TAG = WordListAdapter.class.getSimpleName();
    private final LayoutInflater mInflater;
    private List<Word> wordList;

    public WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.rv_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (wordList != null && wordList.size() > 0) {
            Word word = wordList.get(position);
            holder.tvWord.setText(word.getWord());
        } else {
            holder.tvWord.setText("No word");
        }
    }

    public void setList(List<Word> wordList) {
        Log.i(TAG, "setList: " + wordList.size());
        this.wordList = wordList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return wordList != null ? wordList.size() : 0;
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvWord;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tv_word);
        }
    }

}
