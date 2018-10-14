package app;

import app.data.HeapLogbookStorage;
import app.data.LogbookStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LogbookStorage logbookStorage() {
        return new HeapLogbookStorage();
    }

}
