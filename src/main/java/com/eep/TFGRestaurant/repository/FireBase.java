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

@Repository("fireBase")
public class FireBase {

    @PostConstruct
    private void initFireStore() throws IOException {

        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("key.json");
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl("https://tfgrestaurant.firebaseio.com").build();

        if (FirebaseApp.getApps().isEmpty()){

            FirebaseApp.initializeApp(options);

        }

    }

    public Firestore getFirestore(){

        return FirestoreClient.getFirestore();

    }

}
