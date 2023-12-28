package me.stav.taskhub.ui.boards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import me.stav.taskhub.databinding.FragmentBoardsBinding;
import me.stav.taskhub.utilities.Board;

public class BoardsFragment extends Fragment {

    private FragmentBoardsBinding binding;
    private Board board;

    public BoardsFragment(Board board) {
        this.board = board;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Fragment defaults
        BoardsViewModel boardsViewModel =
                new ViewModelProvider(this).get(BoardsViewModel.class);

        binding = FragmentBoardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        boardsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}