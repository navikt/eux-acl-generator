package eux.rina.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import java.net.URI;
import jakarta.servlet.http.Cookie;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTestUtils {

  static Cookie getOidcTestCookie(TestRestTemplate restTemplate, URI baseUrl, String userName) {
    URI uri = UriComponentsBuilder
        .fromUri(baseUrl)
        .path("local/cookie")
        .queryParam("subject", userName)
        .queryParam("cookiename", "localhost-idtoken")
        .build()
        .toUri();
    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
    String cookieHeader = response
        .getHeaders()
        .getValuesAsList(HttpHeaders.SET_COOKIE)
        .stream()
        .filter(h -> h.contains("localhost-idtoken"))
        .findFirst()
        .get();
    String[] cookie = cookieHeader.split("=");
    return new Cookie(cookie[0], cookie[1]);
  }

}
