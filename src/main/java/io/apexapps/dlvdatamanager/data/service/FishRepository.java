package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Fish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FishRepository extends MongoRepository<Fish, Long> {

}
