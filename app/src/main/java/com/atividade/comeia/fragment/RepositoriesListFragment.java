package com.atividade.comeia.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atividade.comeia.R;
import com.atividade.comeia.utils.CustomAdapter;
import com.atividade.comeia.viewmodel.MainActivityViewModel;

public class RepositoriesListFragment extends Fragment {

    private View fragment;
    private MainActivityViewModel viewModel;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;

    public RepositoriesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_repositories_list, container, false);

        setupList();
        setupResultCounts();

        return fragment;
    }

    private void setupList(){

        CustomAdapter customAdapter = new CustomAdapter(getActivity(), viewModel.getResult().getValue());
        RecyclerView recyclerView = fragment.findViewById(R.id.repositories_list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        ProgressBar progressBar =fragment.findViewById(R.id.progress_bar);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Limite de requisições atingido");
        alertDialog.setMessage("Aguarde um momento e tente novamente");


        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(manager);

        viewModel.getResult().observe(getActivity(), result ->{
            customAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        });

        viewModel.getMessage().observe(getActivity(), message -> {
            progressBar.setVisibility(View.GONE);
            alertDialog.create().show();
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    viewModel.doRequest(viewModel.getNextPage().getValue().toString());
                }
            }
        });
    }

    private void setupResultCounts(){

        TextView resultsCount = fragment.findViewById(R.id.repositories_count);

        viewModel.getResultsCount().observe(getActivity(), count->{
            resultsCount.setText(count+" resultados encontrados...");
        });

    }
}