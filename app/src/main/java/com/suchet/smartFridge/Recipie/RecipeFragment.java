package com.suchet.smartFridge.Recipie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.suchet.smartFridge.R;


public class RecipeFragment extends Fragment {

        private static final String ARG_NAME = "name";
        private static final String ARG_DESC = "description";
        private static final String ARG_INSTRUCTION = "instruction";

        public static RecipeFragment newInstance(String name, String description, String instruction) {
            RecipeFragment fragment = new RecipeFragment();
            Bundle args = new Bundle();
            args.putString(ARG_NAME, name);
            args.putString(ARG_DESC, description);
            args.putString(ARG_INSTRUCTION, instruction);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_recipe, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            TextView title = view.findViewById(R.id.recipieNameTextView);
            TextView desc = view.findViewById(R.id.recipieDescriptionTextView);
            TextView instruction = view.findViewById(R.id.recipieInstructionTextView);

            Bundle args = getArguments();
            if (args != null) {
                title.setText(args.getString(ARG_NAME));
                desc.setText(args.getString(ARG_DESC));
                instruction.setText(args.getString(ARG_INSTRUCTION));
            }

        }
    }

