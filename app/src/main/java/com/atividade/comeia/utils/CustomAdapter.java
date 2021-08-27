package com.atividade.comeia.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.atividade.comeia.R;
import com.atividade.comeia.model.entity.Repository;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;
    private List<Repository> repositories;

    public CustomAdapter(Context context, List<Repository> repositories){
        this.context = context;
        this.repositories = repositories;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recicler_view_custom_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Repository repository = repositories.get(position);

        //set Description
        holder.getRepositoryName().setText(repository.getName());
        if(repository.getDescription() != null){
            if (repository.getDescription().length() > 50){
                holder.getDescription().setText(repository.getDescription().substring(0, 50));
            }else{
                holder.getDescription().setText(repository.getDescription());
            }
        }else{
            holder.getDescription().setText("No description...");
        }

        //Set Owner name
        holder.getOwnerName().setText(repository.getOwner().getLogin());

        //Set language
        if (repository.getLanguage() != null){
            holder.getLanguage().setText(repository.getLanguage());
        }else{
            holder.getLanguage().setText("No language");
        }

        //Set stars count
        if (repository.getStars() != null){
            if (repository.getStars().toString().length() > 3 && repository.getStars().toString().length() < 6){
                String stars = repository.getStars().toString();
                holder.getLike().setText(stars.substring(0, stars.length() - 3)+ "k");
            }else if (repository.getStars().toString().length() > 6){
                String stars = repository.getStars().toString();
                holder.getLike().setText(stars.substring(0, stars.length() - 6) + "M");
            }else{
                holder.getLike().setText(repository.getStars().toString());
            }
        }else{
            holder.getLike().setText("...");
        }

        //Set onClickListener for each reciclerView item
        holder.getMainLayout().setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(repositories.get(position).getHtmlUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (repositories.get(position) == null){
            return 0;
        }else{
            return 1;
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        private TextView repositoryName;
        private ConstraintLayout mainLayout;
        private TextView ownerName;
        private TextView description;
        private TextView like;
        private TextView language;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            repositoryName = itemView.findViewById(R.id.repository_name);
            mainLayout = itemView.findViewById(R.id.main_layout);
            ownerName = itemView.findViewById(R.id.owner);
            description = itemView.findViewById(R.id.description);
            like = itemView.findViewById(R.id.like);
            language = itemView.findViewById(R.id.language);
        }

        public ConstraintLayout getMainLayout(){return mainLayout;}

        public TextView getRepositoryName(){return repositoryName;}

        public TextView getOwnerName() {
            return ownerName;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getLike() {
            return like;
        }

        public TextView getLanguage() {
            return language;
        }
    }

}
