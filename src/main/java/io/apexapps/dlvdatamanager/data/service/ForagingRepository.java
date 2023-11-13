package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Foraging;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ForagingRepository extends MongoRepository<Foraging, Long> {

}
