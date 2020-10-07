package com.musta.roomdemo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.musta.roomdemo.R;
import com.musta.roomdemo.databinding.FragmentAddWordBinding;
import com.musta.roomdemo.models.Word;
import com.musta.roomdemo.utils.Singling;
import com.musta.roomdemo.vms.WordViewModel;

public class AddWordFragment extends Fragment {

    private FragmentAddWordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddWordBinding.inflate(inflater);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WordViewModel wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        binding.btnBack.setOnClickListener(view1 -> NavHostFragment.findNavController(AddWordFragment.this).navigateUp());

        binding.fabSave.setOnClickListener(v -> {
            String wordString = binding.etWord.getText().toString();
            if (wordString.isEmpty()) {
                Toast.makeText(requireContext(), R.string.word_cannnot_be_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            Word word = new Word(wordString);
            wordViewModel.insert(word);
            NavHostFragment.findNavController(AddWordFragment.this).navigateUp();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Singling.hideKeyboard(requireActivity());
    }
}