package com.gustavo.urlshortener.repository;

import com.gustavo.urlshortener.entities.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {



}
