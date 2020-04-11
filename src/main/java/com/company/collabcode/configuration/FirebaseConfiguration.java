package com.company.collabcode.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfiguration {
    private final String FIREBASE_DATABASE_URL = "https://collabcode-4bce8.firebaseio.com";

    private void init() throws Exception{
        File serviceAccountFile = ResourceUtils.getFile( "classpath:/config/collabcode-4bce8-firebase-adminsdk-p9q47-cfd49970e7.json");

        FileInputStream serviceAccount =
                new FileInputStream(serviceAccountFile);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(FIREBASE_DATABASE_URL)
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Bean
    public DatabaseReference getFirebaseDatabaseReference() throws Exception{
        init();
        return FirebaseDatabase.getInstance().getReference();
    }
}
