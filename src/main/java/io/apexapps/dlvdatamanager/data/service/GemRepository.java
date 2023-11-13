package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Gem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GemRepository extends MongoRepository<Gem, Long> {

}
