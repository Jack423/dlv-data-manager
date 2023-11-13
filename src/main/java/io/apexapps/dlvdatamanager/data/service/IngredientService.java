package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public Optional<Ingredient> get(Long id) {
        return repository.findById(id);
    }

    public Ingredient update(Ingredient entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Ingredient> list() {
        return repository.findAll();
    }

    public Page<Ingredient> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Ingredient> list(Pageable pageable, Example<Ingredient> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
