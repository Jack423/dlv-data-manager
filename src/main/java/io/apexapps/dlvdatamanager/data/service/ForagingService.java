package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.entity.Foraging;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForagingService {

    private final ForagingRepository repository;

    public ForagingService(ForagingRepository repository) {
        this.repository = repository;
    }

    public Optional<Foraging> get(Long id) {
        return repository.findById(id);
    }

    public Foraging update(Foraging entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Foraging> list() {
        return repository.findAll();
    }

    public Page<Foraging> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Foraging> list(Pageable pageable, Example<Foraging> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
