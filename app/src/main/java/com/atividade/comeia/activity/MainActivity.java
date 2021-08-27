package com.atividade.comeia.activity;

import androidx.appcompat.app.AlertDialog;
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
            if (search.getText().toString().isEmpty() || search.getText().toString().equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Ops... Algum campo se encontra vazio");
                alertDialog.setMessage("Campo de pesquisa vazio, digite algo e tente novamente!");
                alertDialog.create().show();
            }else{
                getSupportFragmentManager().popBackStack();
                viewModel.clearAllItems();
                viewModel.setTextFromSearch(search.getText().toString());
                viewModel.doRequest();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_layout_content, new RepositoriesListFragment())
                        .addToBackStack(null)
                        .commit();
            }

        });
    }
}