package com.ibm.sec.config;

import com.ibm.sec.filter.ClaimsFilter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.servlet.Filter;
import java.util.concurrent.Executor;

/**
 * Application configuration
 */
@Configuration
@Slf4j
@EnableAsync
public class ApplicationConfig {

	@Bean
	public WebClient webClient() throws SSLException {
	    SslContext sslContext = SslContextBuilder
	            .forClient()
	            .trustManager(InsecureTrustManagerFactory.INSTANCE)
	            .build();
	    HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
	    final int size = 16 * 1024 * 1024;
	    final ExchangeStrategies strategies = ExchangeStrategies.builder()
	            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
	            .build();
	    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).exchangeStrategies(strategies).build();
	}

    @Bean
    public FilterRegistrationBean registerFilters() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(claimsFilter());
        registration.addUrlPatterns("/algosec-integration/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter claimsFilter() {
        return new ClaimsFilter();
    }

    @Bean("controllerAsyncTaskExecutor")
    public Executor asyncThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("ControllerAsyncTasks-");
        executor.initialize();
        return executor;
    }
}