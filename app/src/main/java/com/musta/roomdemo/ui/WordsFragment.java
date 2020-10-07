package com.musta.roomdemo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musta.roomdemo.R;
import com.musta.roomdemo.adapters.WordListAdapter;
import com.musta.roomdemo.databinding.FragmentWordsBinding;
import com.musta.roomdemo.vms.WordViewModel;

public class WordsFragment extends Fragment {

    private FragmentWordsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWordsBinding.inflate(inflater);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WordViewModel wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        binding.fabAdd.setOnClickListener(view1 -> NavHostFragment.findNavController(WordsFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));

        //RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        wordViewModel.getAllWords().observe(getViewLifecycleOwner(), adapter::setList);
    }
}