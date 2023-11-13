package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.CraftingItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CraftingItemRepository extends MongoRepository<CraftingItem, Long> {

}
