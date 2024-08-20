package com.gustavo.urlshortener.controller;

import com.gustavo.urlshortener.dto.ShortenUrlREquest;
import com.gustavo.urlshortener.dto.ShortenUrlResponse;
import com.gustavo.urlshortener.entities.Url;
import com.gustavo.urlshortener.repository.UrlRepository;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class UrlController {

    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    @PostMapping(value = "shorten/url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlREquest request,
                                                         HttpServletRequest httpServletRequest) {

        String id;

        do {
            id = RandomStringUtils.randomAlphanumeric(5, 10);
        } while (urlRepository.existsById(id));


        urlRepository.save(new Url(id, request.url(), LocalDateTime.now().plusMinutes(1)));

        var redirectUrl = httpServletRequest.getRequestURL().toString().replace("shorten/url", id);


        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }

    @GetMapping("{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id) {

        var url = urlRepository.findById(id);
        if (url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();

    }


}
