package rudra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.support.io.ResourceLoaderBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazonaws.services.s3.AmazonS3Client;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run( Application.class, args );
    }
    
    @Bean
    @Autowired
    public static ResourceLoaderBeanPostProcessor resourceLoaderBeanPostProcessor(
                AmazonS3Client amazonS3EncryptionClient) {
            return new ResourceLoaderBeanPostProcessor(amazonS3EncryptionClient);
    }
}
