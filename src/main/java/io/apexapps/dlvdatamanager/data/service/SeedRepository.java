package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Seed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeedRepository extends MongoRepository<Seed, Long> {

}
