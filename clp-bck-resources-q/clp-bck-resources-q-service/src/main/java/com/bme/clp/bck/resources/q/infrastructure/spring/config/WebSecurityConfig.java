package com.bme.clp.bck.resources.q.infrastructure.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.bme.security.infrastructure.spring.jwt.authentication.JwtWesTokenConverter;
import com.bme.security.infrastructure.spring.jwt.authentication.JwtWesTokenDecoder;
import com.sixgroup.fate.accesstokenprocessor.token.ITokenProcessor;
import com.sixgroup.fate.accesstokenprocessor.token.model.WesBMEAccessToken;

import reactor.core.publisher.Mono;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@ComponentScan(basePackages = { "com.sixgroup.fate.accesstokenprocessor" })
public class WebSecurityConfig {

  @Value("${roles}")
  protected String roles;

  @Value("${access-token-processor.signature.symmetric.secretKey}")
  private String secretKey;

  @Autowired
  private ITokenProcessor<WesBMEAccessToken> tokenProcessor;

 @Bean
public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {

    http.authorizeExchange()
      .pathMatchers("/health/**", "/info/**", "/metrics/**", "/mappings/**",
          "/prometheus/**").permitAll();

    http.authorizeExchange().pathMatchers(HttpMethod.POST, "/refresh/**").permitAll();

    http.authorizeExchange(exchanges -> exchanges.anyExchange().hasAnyAuthority(roles.split(",")))
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter())));
    return http.build();
  }

 @Bean
 public ReactiveJwtDecoder jwtDecoder() {
   return new JwtWesTokenDecoder(tokenProcessor, secretKey);
 }

 private Converter<Jwt, Mono<AbstractAuthenticationToken>> customJwtConverter() {
   return new ReactiveJwtAuthenticationConverterAdapter(new JwtWesTokenConverter());
 }

}
