package hu.unideb.ik.progtech.helpclasses;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joey on 4/11/16.
 */
public class Person {

    // The name -fields are never null, never empty, always trimmed
    private String firstName;
    private String lastName;
    // Can be any date
    private LocalDate birthday;
    // never null, can be empty, task date is always today or later, every task name is unique
    private final Map<String, LocalDate> toDoMap = new HashMap<>();

    // Constructors ------------------
    public Person(String firstName, String lastName, LocalDate birthday) {
        firstName = firstName.trim();
        lastName = lastName.trim();
        if (firstName.isEmpty() || lastName.isEmpty() || firstName.contains(" ") || lastName.contains(" "))
            throw new IllegalArgumentException("Invalid name");
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }
    public Person(String name, LocalDate birthday) {
        String[] nameParts = name.split("\\s");
        if (nameParts.length != 2)
            throw new IllegalArgumentException("Invalid name");
        if (nameParts[0].length() < 2 || nameParts[1].length() < 2)
            throw new IllegalArgumentException("Invalid name");

        firstName = nameParts[0];
        lastName = nameParts[1];
        this.birthday = birthday;
    }

    // Getters -------------------------
    public Person(String name) {
        this(name, LocalDate.now());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Map<String, LocalDate> getToDoMap() {
        return toDoMap;
    }

    // Throws exception if a task rule is violated
    public void addTask(String taskName, LocalDate dueDate) {
        if (taskName == null)
            throw new NullPointerException();
        if (taskName.isEmpty())
            throw new IllegalArgumentException("Empty task name");

        LocalDate now = LocalDate.now();
        if (dueDate.isBefore(now))
            throw new IllegalArgumentException("Trying to rewrite history");

        if (toDoMap.containsKey(taskName))
            throw new IllegalArgumentException("Every task name must be unique");

        toDoMap.put(taskName, dueDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(firstName).append(' ').append(lastName)
                .append("\t\t").append("Date of birth: ").append(birthday);
        return sb.toString();
    }
}
