package eum.backed.server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
@Slf4j
@Configuration
public class FirebaseInitializer {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        log.info("Initializing Firebase.");

        // 현재 작업 디렉토리를 얻어옵니다.
        String currentDirectory = System.getProperty("user.dir");

        // 상대 경로로 `firebase.json` 파일을 찾을 수 있는 경로를 설정합니다.
        String relativePath = "src/main/resources/firebasekey.json"; // 상대 경로
        String fullPath = currentDirectory + "/" + relativePath;

        FileInputStream serviceAccount = new FileInputStream(fullPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("eum-app.appspot.com")
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
}