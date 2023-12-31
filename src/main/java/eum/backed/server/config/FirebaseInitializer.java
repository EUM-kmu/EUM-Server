package eum.backed.server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseInitializer {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        log.info("Initializing Firebase.");

        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("eum-app.appspot.com")
                .setDatabaseUrl("https://k-eum2023-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

       FirebaseApp app = FirebaseApp.initializeApp(options);
        log.info("FirebaseApp initialized: " + app.getName());
        return app;
    }

    @Bean
    public FirebaseAuth getFirebaseAuth() throws IOException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp());
        return firebaseAuth;
    }

    @Bean
    public DatabaseReference firebaseDatabaseReference() throws IOException {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebaseApp()).getReference();
        return databaseReference;
    }
}
