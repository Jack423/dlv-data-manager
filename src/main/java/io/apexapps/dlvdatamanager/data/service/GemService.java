package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Gem;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GemService {

    private final GemRepository repository;

    public GemService(GemRepository repository) {
        this.repository = repository;
    }

    public Optional<Gem> get(Long id) {
        return repository.findById(id);
    }

    public Gem update(Gem entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Gem> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Gem> list(Pageable pageable, Example<Gem> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
