package me.stav.taskhub.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import me.stav.taskhub.HomeActivity;
import me.stav.taskhub.R;
import me.stav.taskhub.databinding.FragmentHomeBinding;
import me.stav.taskhub.ui.boards.BoardsFragment;
import me.stav.taskhub.utilities.Board;
import me.stav.taskhub.utilities.FirebaseHandler;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseHandler firebaseHandler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Fragment defaults
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Sets the context
        Context context = root.getContext();

        // Firebase handler initialization
        firebaseHandler = new FirebaseHandler(context);

        // Updating the board view
        updateBoardView(context, root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Function gets context and view, and adds the board buttons
    private void updateBoardView(Context context, View root) {
        // Gets the layout to add the board buttons to the layout
        LinearLayout layout = root.findViewById(R.id.layoutForButtons);

        // Gets user boards
        ArrayList<Board> boards = firebaseHandler.getUserBoards();

        // Adds board buttons to view
        for (int i = 0; i < boards.size(); i++) {
            // Creates new btn
            Button btn = new Button(context);
            // Sets board background to the square with option to add a name
            btn.setBackground(AppCompatResources.getDrawable(context, R.drawable.square_with_name_text));
            // Sets the name text
            btn.setText(boards.get(i).getBoardName());
            // Sets the text size
            btn.setTextSize(12);
            // Sets the text color
            btn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            // Sets btn width
            btn.setWidth(100);
            // Sets btn height
            btn.setHeight(100);
            // Gets btn layout params to add the margins
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
            // Adding the margins
            params.setMargins(5, 0, 0, 5);
            // sets the new layout params with the margin
            btn.setLayoutParams(params);
            // Clicking on the btn
            Board currentBoard = boards.get(i);
            btn.setOnClickListener(v -> goToBoard(currentBoard));

            // Adds the btn to the btn layout
            layout.addView(btn);
        }

        // Adds the default btn to create new board
        // Creates new btn
        Button btn = new Button(context);
        // Sets board background to the square with option to add a name
        btn.setBackground(AppCompatResources.getDrawable(context, R.drawable.dashed_square_with_plus));
        // Gets btn layout params to add the margins
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        // Adding the margins
        params.setMargins(10, 0, 0, 10);
        // sets the new layout params with the margin
        btn.setLayoutParams(params);
        // Clicking on the btn
        btn.setOnClickListener(v -> {
            // Opens the alert dialog with the needed details
            openAlertDialog(context);
        });

        // Adds the btn to the btn layout
        layout.addView(btn);
    }

    private void openAlertDialog(Context context) {
        // Builds the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Sets title and description of dialo
        builder.setTitle("Create new board");
        builder.setMessage("Fill the requirements in order to create a new board:");

        // Creates a linear layout to add the edit texts
        LinearLayout linearLayout = new LinearLayout(context);

        // Default layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // Sets layout params
        linearLayout.setLayoutParams(params);
        // Sets the orientation as vertical
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // Sets default margin
        params.setMargins(5, 0, 0, 5);

        // Creates new EditText
        final EditText boardNameEditText = new EditText(context);
        // Sets EditText layout params
        boardNameEditText.setLayoutParams(params);
        // Sets hint
        boardNameEditText.setHint("Board name");
        // Sets background
        boardNameEditText.setBackground(AppCompatResources.getDrawable(context, R.drawable.blue_border_rounded_cornwe));
        // Sets the cursor color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boardNameEditText.setTextCursorDrawable(AppCompatResources.getDrawable(context, R.drawable.cursor_color));
        }

        // Adds the EditText the layout
        linearLayout.addView(boardNameEditText);

        // Creates new EditText
        final EditText boardDescriptionEditText = new EditText(context);
        // Sets EditText layout params
        boardDescriptionEditText.setLayoutParams(params);
        // Sets hint
        boardDescriptionEditText.setHint("Board description");
        // Sets background
        boardDescriptionEditText.setBackground(AppCompatResources.getDrawable(context, R.drawable.blue_border_rounded_cornwe));
        // Sets the cursor color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boardDescriptionEditText.setTextCursorDrawable(AppCompatResources.getDrawable(context, R.drawable.cursor_color));
        }

        // Adds the EditText the layout
        linearLayout.addView(boardDescriptionEditText);

        // Adds the linear layout with the edit texts
        builder.setView(linearLayout);

        // Adds button handles
        builder.setPositiveButton("Create", (dialog, which) -> {
            Board board = new Board(boardNameEditText.getText().toString(), boardDescriptionEditText.getText().toString(), firebaseHandler.getAuth().getCurrentUser().getUid(), context);
            firebaseHandler.createNewBoard(board, success -> {
                if (success) {
                    goToBoard(board);
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
           dialog.dismiss();
        });

        // Shows the builder
        builder.create();
        builder.show();
    }

    private void goToBoard(Board board) {
        Fragment fragment = new BoardsFragment(board);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}