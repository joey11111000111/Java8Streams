package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.Person;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by joey on 4/21/16.
 */
public class Module001Impl {


    public Map<String, Integer> countListContent(List<String> list) {
        return list.stream()
                .collect(Collectors.groupingBy(s -> s,
                        Collectors.summingInt(le -> 1)));
    }


    public Integer mostAppearance(List<String> list) {
        return list.stream()
                .collect(Collectors.groupingBy(s -> s,
                        Collectors.summingInt(le -> 1))
                )
                .entrySet().stream()
                .mapToInt(Map.Entry::getValue)
                .max()
                .getAsInt();
    }


    public String mostCommonElement(List<String> list) {
        return list.stream()
                .collect(Collectors.groupingBy(s -> s,
                        Collectors.summingInt(le -> 1))
                )
                .entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get().getKey();
    }


    private boolean isOverwhelmed(Person person) {
        LocalDate bound = LocalDate.now().plusDays(2);
        return person.getToDoMap().values().stream()
                .filter(date -> date.isBefore(bound))
                .count() >= 5;
    }

    public Set<Person> overwhelmedPersons(Set<Person> persons) {
        return persons.stream()
                .filter(this::isOverwhelmed)
                .collect(Collectors.toSet());
    }


    public Integer mostPopulatedStatus(Map<String, Map<String, Set<Person>>> employees) {
        return employees.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .mapToInt(Collection::size)
                .max().getAsInt();
    }


    public Set<String> oldEmployeesOfCompany(Map<String, Map<String, Set<Person>>> employees, String companyName,
                                              LocalDate boundary) {
        return employees.entrySet().stream()
                .filter(entry -> entry.getKey().equals(companyName))
                .map(Map.Entry::getValue)
                .map(Map::values)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .filter(person -> person.getBirthday().isBefore(boundary))
                .map(person -> person.getFirstName() + " " + person.getLastName())
                .collect(Collectors.toSet());
    }


    public Map<Boolean, List<Person>> booleanMap(List<Stack<Person>> stackList, LocalDate lowerBound,
                                                 LocalDate upperBound) {
        return stackList.stream()
                .flatMap(Stack::stream)
                .filter(p -> p.getToDoMap().isEmpty())
                .filter(p -> p.getFirstName().length() + p.getLastName().length() > 20)
                .filter(p -> ! p.getBirthday().toString().contains("3"))
                .collect(Collectors.partitioningBy(p -> p.getBirthday().isBefore(upperBound) &&
                        p.getBirthday().isAfter(lowerBound))
                );
    }


}//class
