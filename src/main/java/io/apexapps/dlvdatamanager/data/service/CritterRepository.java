package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Critter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CritterRepository extends MongoRepository<Critter, Long> {

}
