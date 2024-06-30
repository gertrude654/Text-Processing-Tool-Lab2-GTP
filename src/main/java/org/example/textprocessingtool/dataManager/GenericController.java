package org.example.textprocessingtool.dataManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GenericController<T> {
    void addData(String collectionType, T data);
    void updateData(String collectionType, T oldData, T newData);
    void deleteData(String collectionType, T data);
    List<T> searchData(String collectionType, String regex);
    Collection<T> getDataCollection();
    Set<T> getDataSet();
    Map<Integer, T> getDataMap();
}

