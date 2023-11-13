package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.RefinedMaterial;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefinedMaterialRepository extends MongoRepository<RefinedMaterial, Long> {

}
