package com.atividade.comeia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.atividade.comeia.R;
import com.atividade.comeia.fragment.RepositoriesListFragment;
import com.atividade.comeia.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setupSearchBtn();

    }

    private void setupSearchBtn(){
        EditText search = findViewById(R.id.search_repositories);
        Button button = findViewById(R.id.search_btn);

        button.setOnClickListener(view -> {
            viewModel.clearAllItems();
            viewModel.setTextFromSearch(search.getText().toString());
            viewModel.doRequest( "1");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_content, new RepositoriesListFragment()).commit();

        });
    }
}