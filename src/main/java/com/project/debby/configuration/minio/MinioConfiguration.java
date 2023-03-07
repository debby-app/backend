package com.project.debby.configuration.minio;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class MinioConfiguration {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;


    @Bean
    public MinioClient getMinioClient() {
        MinioClient client = null;
        try{
           client = MinioClient.builder()
                    .endpoint(url)
                    .credentials(accessKey, secretKey)
                    .build();
        }
        catch (Throwable t){
            t.printStackTrace();
            log.error(t.getMessage());
            log.error("Minio Service is unavailable.");
        }
        return client;
    }

}
