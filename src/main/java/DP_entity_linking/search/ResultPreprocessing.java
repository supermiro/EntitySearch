package DP_entity_linking.search;

import java.util.*;

/**
 * Created by miroslav.kudlac on 3/20/2016.
 */
public class ResultPreprocessing {

    private List<String> newList;
    private List<Integer> indexes;

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

    public ResultPreprocessing() {

        newList = new ArrayList<String>();
        indexes = new ArrayList<Integer>();
    }
    public List<String> getNewList() {
        return newList;
    }

    public void setNewList(List<String> newList) {
        this.newList = newList;
    }

    public List<List<String>> results(String question, List<String> list){
        String canonic = list.get(0);
        List<List<String>> result = new ArrayList<>();
        List<String> canonicList = new ArrayList<String>();
        canonicList.add(canonic);
        result.add(canonicList);
        question = question.replaceAll("[^a-zA-Z0-9]+", " ").toLowerCase().trim();
        this.setNewList(list);
        this.deleteCorrespondingEntities(canonic);
        for (String l : newList) {
            boolean checkAlreadyExist = false;
            for (List<String> res : result){
                if (res.contains(l)){
                    checkAlreadyExist = true;
                    continue;
                }
            }
            if (checkAlreadyExist){
                continue;
            }
            List<String> grouped = new ArrayList<String>();
            List<Integer> indexesFirst = this.findIndexes(question, l);
            if (indexes != null){
                for (String indexList : newList) {
                    checkAlreadyExist = false;
                    for (List<String> res : result){
                        if (res.contains(indexList)){
                            checkAlreadyExist = true;
                            continue;
                        }
                    }
                    if (checkAlreadyExist){
                        continue;
                    }
                    List<Integer> comparedIndexes = this.findIndexes(question, indexList);
                    if (indexesFirst != null) {
                        int size = indexesFirst.size();
                        indexesFirst.retainAll(comparedIndexes);
                        if (size !=  (size - indexesFirst.size())){
                            grouped.add(indexList);
                        }
                    }
                }
                if (grouped.size() > 0) {
                    result.add(grouped);
                }
            }
            this.setIndexes(indexesFirst);


        }
        return result;
    }

    /**
     * Find indexes of backmapped words in question
     * @param question
     * @param query
     * @return
     */
    public List<Integer> findIndexes(String question, String query){
        List<Integer> indexes = new ArrayList<Integer>();
        String[] canonicArray =  query.split("_");
        for (String s : canonicArray) {
            int index = question.indexOf(s.toLowerCase());
            if (index > -1){
                indexes.add(index);
            }
        }
        return indexes;
    }

    /**
     * delete corresponding entities to canonic entity
     * @param canonic
     */
    private void deleteCorrespondingEntities(String canonic){
        String[] canonicArray =  canonic.split("_");
        //List<String> list = this.getNewList();
        Collections.sort(Arrays.asList(canonicArray), new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return t1.length() - s.length();
            }
        });
        //delete entities which contains same words as canonic entity
        for (int i = 0; i < newList.size(); i++) {
            int containCount = 0;
            boolean delete = false;
            String item = newList.get(i);
            if (item.toLowerCase().contains(canonic.toLowerCase())){
                delete = true;
            } else {
                if (canonicArray.length <= 2){
                    if ((item.toLowerCase()).contains(canonicArray[0].toLowerCase()) && !Character.isUpperCase(canonicArray[0].charAt(0))){
                        delete = true;
                    }
                } else {
                    String[] itemArray =  canonic.split("_");
                    int countItemUpperCase = 0;
                    for (String it : itemArray) {
                        if (Character.isUpperCase(it.charAt(0))){
                            countItemUpperCase++;
                        }
                    }
                    if (countItemUpperCase == itemArray.length){
                        delete = false;
                    }
                    else {
                        for (String s : canonicArray) {
                            if ((item.toLowerCase()).contains(s.toLowerCase())) {
                                containCount++;
                            }
                        }

                        if (containCount >= 2) {
                            delete = true;
                        }
                    }
                }
            }
            if (delete) {
                newList.remove(i);
                i--;
            }
        }
    }
}
