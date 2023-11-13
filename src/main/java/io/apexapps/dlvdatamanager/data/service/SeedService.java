package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Seed;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeedService {

    private final SeedRepository repository;

    public SeedService(SeedRepository repository) {
        this.repository = repository;
    }

    public Optional<Seed> get(Long id) {
        return repository.findById(id);
    }

    public Seed update(Seed entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Seed> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Seed> list(Pageable pageable, Example<Seed> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
