package com.ibm.sec.util;

import com.ibm.sec.model.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.lang.String.format;

/**
 * For the GET api calls, spring reactive WebClient is used
 * For the POST api calls, spring RestTemplate is used because we were not able to pass multipart requests using WebClient
 */
@Component
@Slf4j
public class RestUtil {

    @Autowired
    private WebClient webClient;

    @Value("${service_auth_token}")
    private String serviceAuthToken;

    /**
     * GET api calls are invoked using spring reactive webclient
     *
     * @param urlTemplate
     * @param responseType
     * @param urlTemplateParams
     * @param <T>
     * @return
     */
    public <T> Mono<T> makeGetCallToMSSAPIs(UserSession userSession, boolean callAsAdmin, String urlTemplate, Class<T> responseType, String... urlTemplateParams) {
        log.info("UserId: {} SessionId: {} Calling: {}", userSession.getUserName(), userSession.getThreadUuid(), urlTemplate);
        if (callAsAdmin) {
            return webClient.get().uri(urlTemplate, urlTemplateParams).header(HttpHeaders.AUTHORIZATION, getBasicAuthorizationToken(IConstant.MSS_TOKEN, serviceAuthToken)).retrieve().bodyToMono(responseType);
        } else {
            return webClient.get().uri(urlTemplate, urlTemplateParams).header(HttpHeaders.AUTHORIZATION, userSession.getAuthHeaderValue()).retrieve().bodyToMono(responseType);
        }
    }

    public <T> Mono<T> makeGetCallToAlgosecAPIs(UserSession userSession, String urlTemplate, Class<T> responseType, Map<String, String> headerMap, String... urlTemplateParams) {
        log.info("UserId: {} SessionId: {} Calling: {}", userSession.getUserName(), userSession.getThreadUuid(), urlTemplate);
        return webClient.get().uri(urlTemplate, urlTemplateParams).headers(httpHeaders -> { headerMap.forEach((k, v) -> {httpHeaders.add(k, v);});}).retrieve().bodyToMono(responseType);
    }

    public Mono<ClientResponse> makePostCallToAlgosecAPIs(UserSession userSession, String urlTemplate, Map<String, String> headerMap, Object body, String... urlTemplateParams) {
        log.info("UserId: {} SessionId: {} Calling: {}", userSession.getUserName(), userSession.getThreadUuid(), urlTemplate);
        return webClient.post().uri(urlTemplate, urlTemplateParams).headers(headers -> {
            if(headerMap != null) {
                headerMap.forEach((k, v) -> headers.add(k, v));
            }
        }).bodyValue(body).exchange();
    }

    public Mono<ClientResponse> makePutCallToAlgosecAPIs(UserSession userSession, String urlTemplate, Map<String, String> headerMap, Object body, String... urlTemplateParams) {
        log.info("UserId: {} SessionId: {} Calling: {}", userSession.getUserName(), userSession.getThreadUuid(), urlTemplate);
        return webClient.put().uri(urlTemplate, urlTemplateParams).headers(headers -> {
            if(headerMap != null) {
                headerMap.forEach((k, v) -> headers.add(k, v));
            }
        }).bodyValue(body).exchange();
    }

    public <T> Mono<T> makePutCallToMSSAPIs(UserSession userSession, boolean callAsAdmin, String urlTemplate, Object body, Class<T> responseType, String... urlTemplateParams) {

        if (callAsAdmin) {
            return webClient.put().uri(urlTemplate, urlTemplateParams).header(HttpHeaders.AUTHORIZATION, getBasicAuthorizationToken(IConstant.MSS_TOKEN, serviceAuthToken)).bodyValue(body).retrieve().bodyToMono(responseType);
        } else {
            return webClient.put().uri(urlTemplate, urlTemplateParams).header(HttpHeaders.AUTHORIZATION, userSession.getAuthHeaderValue()).bodyValue(body).retrieve().bodyToMono(responseType);
        }
    }

    /**
     * Method to retrieve Authorization token from the admin username and password
     *
     * @param userName
     * @param password
     * @return
     */
    private String getBasicAuthorizationToken(String userName, String password) {
        byte[] credentials = Base64.encodeBase64((format("%s:%s", userName, password)).getBytes(StandardCharsets.UTF_8));
        return format("Basic %s", new String(credentials, StandardCharsets.UTF_8));
    }
}