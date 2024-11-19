package com.ensa.comptesoap.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ensa.comptesoap.models.Compte;
import com.ensa.comptesoap.utils.Constants;
public class CompteService {
    public List<Compte> getComptes() throws Exception {
        // Création de la requête SOAP
        SoapObject request = new SoapObject("http://ws.soap.ensa.com/", "getComptes");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(Constants.URL);
        transport.debug = true;
        transport.call(null, envelope);

        // Récupération de la réponse
        SoapObject response = (SoapObject) envelope.bodyIn;
        List<Compte> comptes = new ArrayList<>();

        if (response != null) {
            for (int i = 0; i < response.getPropertyCount(); i++) {
                SoapObject soapCompte = (SoapObject) response.getProperty(i);
                Compte compte = new Compte();

                // Récupérer les champs du compte
                compte.setId(Long.parseLong(soapCompte.getPropertySafelyAsString("id")));
                compte.setSolde(Double.parseDouble(soapCompte.getPropertySafelyAsString("solde")));
                compte.setType(soapCompte.getPropertySafelyAsString("type"));

                // Récupérer la date de création
                String dateCreationStr = soapCompte.getPropertySafelyAsString("dateCreation");
                if (dateCreationStr != null && !dateCreationStr.isEmpty()) {
                    // Use the correct SimpleDateFormat to parse the date string
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    dateFormat.setLenient(false);  // Avoid lenient parsing which might lead to incorrect parsing
                    try {
                        Date dateCreation = dateFormat.parse(dateCreationStr);
                        compte.setDateCreation(dateCreation);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                comptes.add(compte);
            }
        }

        return comptes;
    }

    public Compte getCompteById(Long id) throws Exception {
        SoapObject request = new SoapObject("", Constants.METHOD_GET_COMPTE_BY_ID);
        request.addProperty("id", id);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(Constants.URL);
        transport.call("", envelope);

        SoapObject response = (SoapObject) envelope.getResponse();
        Compte compte = new Compte();
        compte.setId(Long.parseLong(response.getProperty("id").toString()));
        compte.setSolde(Double.parseDouble(response.getProperty("solde").toString()));
        compte.setType(response.getProperty("type").toString());

        return compte;
    }


    public Compte createCompte(String type, double solde) throws Exception {
        // Namespace and method setup
        SoapObject request = new SoapObject("http://ws.soap.ensa.com/", Constants.METHOD_CREATE_COMPTE);
        request.addProperty("type", type); // Assuming `type` matches `TypeCompte` or is a valid string representation
        request.addProperty("solde", String.valueOf(solde)); // Convert `double` to `String`

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false; // Ensure this is set to false for Java-based SOAP services
        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(Constants.URL);
        transport.call(null, envelope);

        // Check for a SOAP fault
        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault fault = (SoapFault) envelope.bodyIn;
            throw new Exception("SOAP Fault: " + fault.faultstring);
        }

        // Parse the response
        SoapObject response = (SoapObject) envelope.bodyIn;
        if (response != null) {
            SoapObject soapCompte = (SoapObject) response.getProperty(0);
            Compte compte = new Compte();
            compte.setId(Long.parseLong(soapCompte.getPropertySafelyAsString("id")));
            compte.setSolde(Double.parseDouble(soapCompte.getPropertySafelyAsString("solde")));
            compte.setType(soapCompte.getPropertySafelyAsString("type"));
            return compte;
        }

        throw new Exception("Failed to create compte: Response is null");
    }
    public boolean deleteCompte(Long id) throws Exception {
        // Create the SOAP request
        SoapObject request = new SoapObject("http://ws.soap.ensa.com/", Constants.METHOD_DELETE_COMPTE);
        request.addProperty("id", id);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false; // Adjust based on your server configuration
        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(Constants.URL);

        // Make the SOAP call
        try {
            transport.call(null, envelope);
        } catch (Exception e) {
            throw new Exception("Error communicating with the server: " + e.getMessage());
        }

        // Check for SOAP fault
        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault fault = (SoapFault) envelope.bodyIn;
            throw new Exception("SOAP Fault: " + fault.faultstring);
        }

        // Parse the response
        if (envelope.bodyIn instanceof SoapObject) {
            SoapObject response = (SoapObject) envelope.bodyIn;
            return Boolean.parseBoolean(response.getProperty(0).toString());
        }

        // Default case: unexpected response
        throw new Exception("Unexpected response from the server");
    }
}
