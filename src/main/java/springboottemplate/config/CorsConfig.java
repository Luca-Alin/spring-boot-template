package springboottemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    private final static List<String> ALLOWED_DOMAINS = List.of("http://localhost:5173");
    private final static List<String> ALLOWED_HEADERS = List.of("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers");
    private final static List<String> EXPOSED_HEADERS = List.of("Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
    private final static List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");

    @Bean
    public CorsFilter corsFilter() {

        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);

        corsConfig.setAllowedOrigins(ALLOWED_DOMAINS);
        corsConfig.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfig.setExposedHeaders(EXPOSED_HEADERS);
        corsConfig.setAllowedMethods(ALLOWED_METHODS);

        var url = new UrlBasedCorsConfigurationSource();
        url.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(url);
    }
}
