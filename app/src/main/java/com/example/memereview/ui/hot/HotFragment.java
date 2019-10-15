package com.example.memereview.ui.hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.memereview.R;

public class HotFragment extends Fragment {

    private HotViewModel hotViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hotViewModel =
                ViewModelProviders.of(this).get(HotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        final TextView textView = root.findViewById(R.id.text_hot);
        hotViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}