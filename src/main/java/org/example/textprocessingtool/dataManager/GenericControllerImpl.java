package org.example.textprocessingtool.dataManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GenericControllerImpl<T> implements GenericController<T> {
    private final GenericRepository<T> repository;

    public GenericControllerImpl(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void addData(String collectionType, T data) {
        repository.addData(collectionType, data);
    }

    @Override
    public void updateData(String collectionType, T oldData, T newData) {
        repository.updateData(collectionType, oldData, newData);
    }

    @Override
    public void deleteData(String collectionType, T data) {
        repository.deleteData(collectionType, data);
    }

    @Override
    public List<T> searchData(String collectionType, String regex) {
        return repository.searchData(collectionType, regex);
    }

    @Override
    public Collection<T> getDataCollection() {
        return repository.getDataCollection();
    }

    @Override
    public Set<T> getDataSet() {
        return repository.getDataSet();
    }

    @Override
    public Map<Integer, T> getDataHashMap() {
        return repository.getDataHashMap();
    }
}
