package com.ensa.comptesoap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ensa.comptesoap.adapter.CompteAdapter;
import com.ensa.comptesoap.models.Compte;
import com.ensa.comptesoap.models.TypeCompte;
import com.ensa.comptesoap.viewmodel.CompteViewModel;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private CompteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(CompteViewModel.class);

        adapter = new CompteAdapter(this, viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getComptes().observe(this, comptes -> {
            adapter.setComptes(comptes);
        });

        findViewById(R.id.addButton).setOnClickListener(v -> showAddCompteDialog());
    }

    private void showAddCompteDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_compte, null);
        Spinner typeSpinner = dialogView.findViewById(R.id.typeSpinner);
        EditText soldeEditText = dialogView.findViewById(R.id.soldeEditText);

        ArrayAdapter<TypeCompte> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, TypeCompte.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);

        new AlertDialog.Builder(this)
                .setTitle("Ajouter un compte")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    try {
                        String type = ((TypeCompte) typeSpinner.getSelectedItem()).name();
                        double solde = Double.parseDouble(soldeEditText.getText().toString());
                        viewModel.addCompte(type, solde);
                        Toast.makeText(this, "Compte ajouté avec succès", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create()
                .show();
    }
}
