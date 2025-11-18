package edu.guvi.kaddysShop.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtk1qrqbj",
                "api_key", "796874836321896",
                "api_secret", "RSvKC3GgzO2KqwMNoVxYNmMPkbY"
        ));
    }
}

