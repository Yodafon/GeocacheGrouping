package com.gg.loader.cache;

import com.gg.generated.Nuts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestEndpoint {

  @Value("${nuts.rest.url}")
  private String url;

  Logger LOGGER = LoggerFactory.getLogger(RestEndpoint.class);

  private RestTemplate restTemplate = new RestTemplateBuilder()
          .messageConverters(new NUTSConverter()).requestFactory(SimpleClientHttpRequestFactory.class).build();

  public Nuts getNuts() {
    return restTemplate.getForObject(url, Nuts.class);
  }


}
