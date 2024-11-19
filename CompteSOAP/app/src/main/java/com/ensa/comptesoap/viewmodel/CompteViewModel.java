package com.ensa.comptesoap.viewmodel;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ensa.comptesoap.models.Compte;
import com.ensa.comptesoap.service.CompteService;
public class CompteViewModel extends ViewModel {
    private static final String TAG = "CompteViewModel";
    private final MutableLiveData<List<Compte>> comptes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final CompteService compteService;

    public CompteViewModel() {
        compteService = new CompteService();
        Log.d(TAG, "ViewModel initialized");
        loadComptes();
    }

    public LiveData<List<Compte>> getComptes() {
        return comptes;
    }

    public LiveData<String> getError() {
        return error;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadComptes() {
        Log.d(TAG, "Loading comptes...");
        new AsyncTask<Void, Void, List<Compte>>() {
            private Exception exception;

            @Override
            protected List<Compte> doInBackground(Void... voids) {
                try {
                    List<Compte> result = compteService.getComptes();
                    Log.d(TAG, "Loaded " + (result != null ? result.size() : 0) + " comptes");
                    return result;
                } catch (Exception e) {
                    Log.e(TAG, "Error loading comptes", e);
                    exception = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Compte> result) {
                if (result != null) {
                    Log.d(TAG, "Setting comptes to LiveData");
                    comptes.setValue(result);
                } else {
                    error.setValue("Erreur lors du chargement des comptes: " + exception.getMessage());
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void addCompte(String type, double solde) {
        Log.d(TAG, "Adding compte: type=" + type + ", solde=" + solde);
        new AsyncTask<Void, Void, Compte>() {
            private Exception exception;

            @Override
            protected Compte doInBackground(Void... voids) {
                try {
                    Compte result = compteService.createCompte(type, solde);
                    Log.d(TAG, "Created compte: " + (result != null ? result.getId() : "null"));
                    // If the creation date is null, assign a valid date
                    if (result != null && result.getDateCreation() == null) {
                        result.setDateCreation(new Date());  // Ensure a valid date is set
                    }
                    return result;
                } catch (Exception e) {
                    Log.e(TAG, "Error creating compte", e);
                    exception = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Compte result) {
                if (result != null) {
                    List<Compte> currentList = comptes.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }
                    currentList.add(result);
                    Log.d(TAG, "Adding new compte to list. New size: " + currentList.size());
                    comptes.setValue(currentList);
                } else {
                    error.setValue("Erreur lors de la création du compte: " + exception.getMessage());
                }
            }
        }.execute();
    }




    @SuppressLint("StaticFieldLeak")
    public void deleteCompte(Long id) {
        Log.d(TAG, "Deleting compte: id=" + id);
        new AsyncTask<Void, Void, Boolean>() {
            private Exception exception;

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    boolean success = compteService.deleteCompte(id);
                    Log.d(TAG, "Delete result: " + success);
                    return success;
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting compte", e);
                    exception = e;
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    List<Compte> currentList = comptes.getValue();
                    if (currentList != null) {
                        currentList.removeIf(compte -> compte.getId().equals(id));
                        Log.d(TAG, "Removed compte from list. New size: " + currentList.size());
                        comptes.setValue(currentList);
                    }
                } else {
                    error.setValue("Erreur lors de la suppression du compte: " +
                            (exception != null ? exception.getMessage() : "unknown error"));
                }
            }
        }.execute();
    }

    public void refreshComptes() {
        loadComptes();
    }

}