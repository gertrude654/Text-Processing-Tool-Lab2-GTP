package org.example.textprocessingtool.dataManager;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GenericRepositoryImpl<T> implements GenericRepository<T> {
    private Collection<T> dataCollection;
    private Set<T> dataSet;
    private Map<Integer, T> dataMap;
    private Map<Integer, T> dataHashMap;
    private int nextMapKey = 1;
    private int nextHashMapKey = 1;

    public GenericRepositoryImpl() {
        dataCollection = new ArrayList<>();
        dataSet = new HashSet<>();
        dataMap = new LinkedHashMap<>();
        dataHashMap = new HashMap<>();
    }

    @Override
    public Collection<T> getDataCollection() {
        return dataCollection;
    }

    @Override
    public Set<T> getDataSet() {
        return dataSet;
    }

    @Override
    public Map<Integer, T> getDataHashMap() {
        return dataHashMap;
    }

    @Override
    public void addData(String collectionType, T data) {
        switch (collectionType) {
            case "ArrayList":
                dataCollection.add(data);
                break;
            case "HashSet":
                dataSet.add(data);
                break;
            case "HashMap":
                dataHashMap.put(nextHashMapKey++, data);
                break;
        }
    }

    @Override
    public void updateData(String collectionType, T oldData, T newData) {
        switch (collectionType) {
            case "ArrayList":
                dataCollection.remove(oldData);
                dataCollection.add(newData);
                break;
            case "HashSet":
                dataSet.remove(oldData);
                dataSet.add(newData);
                break;

            case "HashMap":
                for (Map.Entry<Integer, T> entry : dataHashMap.entrySet()) {
                    if (entry.getValue().equals(oldData)) {
                        dataHashMap.put(entry.getKey(), newData);
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void deleteData(String collectionType, T data) {
        switch (collectionType) {
            case "ArrayList":
                dataCollection.remove(data);
                break;
            case "HashSet":
                dataSet.remove(data);
                break;

            case "HashMap":
                dataHashMap.values().remove(data);
                break;
        }
    }

    @Override
    public List<T> searchData(String collectionType, String regex) {
        Pattern pattern = Pattern.compile(regex);
        switch (collectionType) {
            case "ArrayList":
                return dataCollection.stream().filter(data -> pattern.matcher(data.toString()).find()).collect(Collectors.toList());
            case "HashSet":
                return dataSet.stream().filter(data -> pattern.matcher(data.toString()).find()).collect(Collectors.toList());
            case "HashMap":
                return dataHashMap.values().stream().filter(data -> pattern.matcher(data.toString()).find()).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }
}
