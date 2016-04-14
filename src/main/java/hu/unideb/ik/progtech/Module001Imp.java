package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.Person;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by joey on 4/11/16.
 */
public class Module001Imp {

    public Map<String, Integer> countListContent(List<String> list) {
        Map<String, Integer> identity = new HashMap<String, Integer>();
        BiFunction<Map<String, Integer>, String, Map<String, Integer>> accumulator =
                (map, str) -> {
                    if (map.containsKey(str))
                        map.put(str, map.get(str) + 1);
                    else
                        map.put(str, 1);

                    return map;
                };

        BinaryOperator<Map<String, Integer>> combiner = (mapLeft, mapRight) -> {
            Set<String> allKeys = mapLeft.keySet();
            allKeys.addAll(mapRight.keySet());

            for (String key : allKeys) {
                if (mapRight.containsKey(key))
                    if (mapLeft.containsKey(key))
                        mapLeft.put(key, mapLeft.get(key) + mapRight.get(key));
                    else
                        mapLeft.put(key, mapRight.get(key));
            }//for

            return mapLeft;
        };

        return list.stream().reduce(identity, accumulator, combiner);
    }//countListContent


    public Integer mostAppearance(List<String> list) {
        if (list == null || list.size() < 1)
            return 0;

        return list.stream()
                .collect(Collectors.groupingBy(s -> s))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size())
                .findFirst()
                .get()
                .getValue().size();
    }


    public Set<Person> overwhelmedPersons(Set<Person> persons) {
        return persons.stream()
                .filter(p -> p.getToDoMap().size() >= 5)
                .collect(Collectors.toSet());
    }

    public Integer mostPopulatedStatus(Map<String, Map<String, Set<Person>>> employees) {
        return employees.entrySet().stream()
                .mapToInt(e -> {
                    Map<String, Set<Person>> innerMap = e.getValue();
                    int maxSize = -1;
                    for (Map.Entry<String, Set<Person>> entry : innerMap.entrySet()) {
                        int innerSetSize = entry.getValue().size();
                        if (innerSetSize > maxSize)
                            maxSize = innerSetSize;
                    }

                    System.out.println("maxSize = " + maxSize);
                    return maxSize;
                }).max().getAsInt();
    }

    public List<String> oldEmployeesOfCompany(Map<String, Map<String, Set<Person>>> employees, String companyName,
                                              LocalDate boundary) {
        return employees.entrySet().stream()
                .filter(e -> e.getKey().equals(companyName))
                .map(e -> e.getValue())
                .map(e -> e.entrySet().stream()
                        .map(e2 -> e2.getValue())
                        .collect(Collectors.toList())
                ).flatMap(List::stream)
                .flatMap(l -> l.stream())
                .filter(p -> p.getBirthday().isBefore(boundary))
                .map(p -> p.getFirstName() + ' ' + p.getLastName())
                .sorted()
                .collect(Collectors.toList());
    }


    public Map<Boolean, List<Person>> booleanMap(List<Stack<Person>> stackList, LocalDate lowerBound,
                                                 LocalDate upperBound) {
        return stackList.stream()
                .flatMap(Stack::stream)
                .filter(p -> {
                    boolean result = p.getToDoMap().size() == 0;
                    result &= p.getFirstName().length() + p.getLastName().length() > 20;
                    result &= !p.getBirthday().toString().contains("3");
                    return result;
                })
                .collect(Collectors.partitioningBy(p -> {
                            LocalDate birthday = p.getBirthday();
                            return lowerBound.isBefore(birthday) && upperBound.isAfter(birthday);
                        }
                ));
    }

}//class
