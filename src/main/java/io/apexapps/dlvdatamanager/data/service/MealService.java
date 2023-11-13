package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Meal;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Optional<Meal> get(Long id) {
        return repository.findById(id);
    }

    public Meal update(Meal entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Meal> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Meal> list(Pageable pageable, Example<Meal> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
