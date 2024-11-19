package com.ensa.comptesoap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ensa.comptesoap.R;
import com.ensa.comptesoap.models.Compte;
import com.ensa.comptesoap.viewmodel.CompteViewModel;

import java.util.ArrayList;
import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {
    private List<Compte> comptes = new ArrayList<>();
    private final Context context;
    private final CompteViewModel viewModel;

    public CompteAdapter(Context context, CompteViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.soldeTextView.setText("Solde: " + compte.getSolde());
        holder.typeTextView.setText("Type: " + compte.getType());

        // Remove the edit functionality

        // Handle delete functionality
        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(compte));
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    private void showDeleteConfirmationDialog(Compte compte) {
        new AlertDialog.Builder(context)
                .setTitle("Supprimer le compte")
                .setMessage("Voulez-vous vraiment supprimer ce compte ?")
                .setPositiveButton("Oui", (dialog, which) -> viewModel.deleteCompte(compte.getId()))
                .setNegativeButton("Non", null)
                .create()
                .show();
    }

    static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView soldeTextView, typeTextView;
        ImageButton deleteButton;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            soldeTextView = itemView.findViewById(R.id.soldeTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
