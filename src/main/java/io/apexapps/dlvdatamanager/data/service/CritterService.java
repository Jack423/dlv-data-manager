package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Critter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CritterService {

    private final CritterRepository repository;

    public CritterService(CritterRepository repository) {
        this.repository = repository;
    }

    public Optional<Critter> get(Long id) {
        return repository.findById(id);
    }

    public Critter update(Critter entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Critter> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Critter> list(Pageable pageable, Example<Critter> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
