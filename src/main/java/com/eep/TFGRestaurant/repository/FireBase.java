package com.eep.TFGRestaurant.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

//repositorio para la conexion con la base de datos de firebase
@Repository("fireBase")
public class FireBase {

    /**
     * conexion con la base de datos
     * @throws IOException excepcion por si no conecta con la base de datos
     */
    @PostConstruct
    private void initFireStore() throws IOException {

        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("key.json");
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl("https://tfgrestaurant.firebaseio.com").build();

        if (FirebaseApp.getApps().isEmpty()){

            FirebaseApp.initializeApp(options);

        }

    }

    /**
     * metodo para el llamado de la base de datos
     * @return base de datos
     */
    public Firestore getFirestore(){

        return FirestoreClient.getFirestore();

    }

}
