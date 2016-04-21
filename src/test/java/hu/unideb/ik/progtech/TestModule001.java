package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.Person;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class TestModule001 {

    static Module001Impl moduleImpl;

    @BeforeClass
    public static void setUp() {
        moduleImpl = new Module001Impl();
    }

    /*
    Adott egy String lista, amiben minden elem egyszer vagy többször fordul elő.
    A függvényhívás eredménye legyen egy Map<String, Integer>, ami kulcsai a különböző listaelemeket
    tartalmazza, értékei pedig a kulcs előfordulásának számát a listában.
     */
    @Test
    public void countListContentTest() {
        List<String> list = Arrays.asList("a", "b", "a", "c", "k", "c", "a", "k", "d", "c", "k", "b", "k");
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("a", 3);
        expected.put("b", 2);
        expected.put("c", 3);
        expected.put("k", 4);
        expected.put("d", 1);
        Map<String, Integer> result = moduleImpl.countListContent(list);
        assertEquals(expected, result);
    }

    /*
    Adott egy String lista, amiben minden elem egyszer vagy többször fordul elő.
    A függvényhívás eredménye legyen egy Integer, aminek értéke azt reprezentálja,
    hogy a leggyakoribb elem mennyiszer fordul elő a listában.
     */
    @Test
    public void mostAppearanceTest() {
        List<String> list = Arrays.asList("a", "a", "b", "c", "a", "b", "a", "c", "c", "a", "c", "c", "c");
        Integer expected = 6;
        Integer result = moduleImpl.mostAppearance(list);
        assertEquals(expected, result);
    }

    /*
    Adott az előző feladatban megadott String lista. A függvényhívás eredménye adja vissza azt
    a String -et, amelyik a legtöbbször fordul elő a listában.
     */
    @Test
    public void mostCommonElementTest() {
        List<String> list = Arrays.asList("a", "a", "b", "c", "a", "b", "a", "c", "c", "a", "c", "c", "c");
        String expected = "c";
        String result = moduleImpl.mostCommonElement(list);
        assertEquals(expected, result);

    }

    /*
    Adott egy halmaz, ami személyeket tartalmaz. A függvényhívás adjon vissza egy olyan
    halmazt, ami az eredeti halmaz azon személyeit tartalmazza, akiknek a mai és holnapi
    nap során összesen legalább 5 feladata van.
     */
    @Test
    public void overwhelmedPersonsTest() {
        Supplier<Person> freePersonSupplier = createFreePersonSupplier();
        Set<Person> allPersons = new HashSet<>(10);
        for (int i = 0; i < 10; i++) {
            Person person = freePersonSupplier.get();
            if (i % 3 == 0) {
                person.addTask("task1", LocalDate.now());
                person.addTask("task2", LocalDate.now().plusDays(1));
                person.addTask("task3", LocalDate.now());
                person.addTask("task4", LocalDate.now());
                person.addTask("task5", LocalDate.now().plusDays(1));
            }
            allPersons.add(person);
        }

        Set<Person> result = moduleImpl.overwhelmedPersons(allPersons);
        result.stream()
            .forEach(p -> assertEquals(5, p.getToDoMap().size()));
    }//overwhelmedPersonTest

    /*
    Adott egy olyan "összetetten generikus Map", amilyennel ZH-n nem szeretnék találkozni.
    Az első String egy vállalat nevét adja meg.
    A belső Map String-je egy beosztás neve, a hozzá tartozó Set pedig tartalmazza az adott
    beosztáshoz tartozó személyeket.
    Tehát egy entry egy cég összes beosztását és a beosztásokhoz tartozó személyeket adja meg.
    A függvényhívás eredménye legyen egy Integer, amely megadja, hogy a legnépesebb beosztáshoz
    (cégtől függetlenül) mennyi személy tartozik.
     */
    @Test
    public void mostPopulatedStatusTest() {
        Map<String, Map<String, Set<Person>>> employeesAtCompanies = createEmployeeMap();
        Integer result = moduleImpl.mostPopulatedStatus(employeesAtCompanies);
        assertEquals(new Integer(19), result);
    }

    /*
    Adott az előző feladatban is használt Map. A függvényhívás eredménye adjon vissza egy
    String listát, ami azoknak a személyeknek a nevét (firstName+egySzóköz+lastName formában) tartalmazza,
    akik a megadott nevű vállalatnál dolgoznak és a megadott idő előtt születtek.
     */
    @Test
    public void oldEmployeesOfCompanyTest() {
        Map<String, Map<String, Set<Person>>> employeesAtCompanies = createEmployeeMap();
        // Calculate expected result
        Map<String, Set<Person>> innerMap = employeesAtCompanies.get("Firente");
        Set<Person> firenteEmployees = new HashSet<>();
        for (Map.Entry<String, Set<Person>> entry : innerMap.entrySet())
            firenteEmployees.addAll(entry.getValue());
        Set<String> expected = new HashSet<>();
        LocalDate boundary = LocalDate.of(1955, 1, 1);
        for (Person p : firenteEmployees)
            if (p.getBirthday().isBefore(boundary))
                expected.add(p.getFirstName() + ' ' + p.getLastName());

        Set<String> result = moduleImpl.oldEmployeesOfCompany(employeesAtCompanies, "Firente", boundary);
        assertEquals(expected, result);
    }

    /*
    Adott egy lista, ami személyeket tartalmazó stack-eket tárol. A függvényhívás eredménye legyen egy
    olyan Map, amely kulcsai Boolean típusúak. Az értékek az alábbi feltételek alapján oszlanak ketté.
    Az igaz kulcs értéke egy olyan lista, amely azon személyeket tartalmazza, akik a két megadott dátum
    között születtek (a két szélsőérték már nem elfogadott), a hamis kulcs mellett pedig azoknak a személyeknek
    a listája álljon, akik az előző feltételt nem teljesítik.
    A visszaadott Map -ben csak és kizárólag olyan személyek szerepelhetnek, akiknek nincs egyetlen elvégzendő
    feladatuk sem, a kereszt- és vezetéknevük együttes hossza nagyobb 20-nál
    és a születési dátumukban nem szerepel a 3-as szám (pl az 1933.03.13. elég erősen elvérzik ez alatt).
     */
    @Test
    public void booleanMapTest() {
        // Generate test data
        List<Stack<Person>> stackList = new ArrayList<>();
        Supplier<Person> personSupplier = createFreePersonSupplier();
        for (int i = 0; i < 10; i++) {
            Stack<Person> stack = new Stack<>();
            for (int j = 0; j < 6 + (Math.random() * 6); j++)
                stack.push(personSupplier.get());
            stackList.add(stack);
        }
        LocalDate lowerBound = LocalDate.of(1940, 4, 6);
        LocalDate upperBound = LocalDate.of(1985, 7, 19);

        // Generate expected result
        Map<Boolean, List<Person>> expected = new HashMap<>();
        expected.put(true, new ArrayList<>());
        expected.put(false, new ArrayList<>());

        for (Stack<Person> stack : stackList) {
            for (Person p : stack) {
                if (p.getToDoMap().size() > 0)
                    continue;
                if (p.getFirstName().length() + p.getLastName().length() <= 20)
                    continue;

                LocalDate birthday = p.getBirthday();
                if(birthday.toString().contains("3"))
                    continue;

                if (lowerBound.isBefore(birthday) && upperBound.isAfter(birthday))
                    expected.get(true).add(p);
                else
                    expected.get(false).add(p);
            }//for
        }//for

        Map<Boolean, List<Person>> result = moduleImpl.booleanMap(stackList, lowerBound, upperBound);
        assertEquals(expected, result);
    }

    // Supplier methods -----------------------------------------------------
    private Supplier<Person> createFreePersonSupplier() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Supplier<Person> freePersonSupplier = () -> {
            // Generate random name
            String[] name = new String[2];
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int n = 0; n < 2; n++) {
                for (int i = 0; i < Math.random() * 18 + 2; i++) {
                    int index = random.nextInt(alphabet.length());
                    sb.append(alphabet.charAt(index));
                }
                name[n] = sb.toString();
            }

            // Generate random birthday from [1920.01.01-2010.01.01]
            LocalDate oldestDate = LocalDate.of(1920, 1, 1);
            LocalDate latestDate = LocalDate.of(2010, 1, 1);
            long daysBetween = ChronoUnit.DAYS.between(oldestDate, latestDate);
            long randomDays = random.nextInt((int) daysBetween);
            LocalDate birthday = oldestDate.plusDays(randomDays);

            return new Person(name[0], name[1], birthday);
        };

        return freePersonSupplier;
    }//createFreePersonSupplier

    private Map<String, Map<String, Set<Person>>> createEmployeeMap() {
        Map<String, Map<String, Set<Person>>> map = new HashMap<>(3);
        // Creating names of the companies. If they really exist, or simply the words really mean something,
        // it is only a coincidence.
        String[] companyNames = new String[3];
        companyNames[0] = "Firente";
        companyNames[1] = "Beron Goron";
        companyNames[2] = "Pederolux";

        Supplier<Person> personSupplier = createFreePersonSupplier();
        Map<String, Set<Person>> firenteEmployees = new HashMap<>(4);
        String[] firenteStatusNames = {"Junior", "Senior", "Manager", "Director"};
        for (int i = 0; i < firenteStatusNames.length; i++) {   // 10 Junior
            Set<Person> employees = new HashSet<>();
            for (int j = 0; j < 10 - i * i; j++)
                employees.add(personSupplier.get());
            firenteEmployees.put(firenteStatusNames[i], employees);
        }

        Map<String, Set<Person>> bgEmployees = new HashMap<>(3);
        String[] bgStatusNames = {"Worker", "Cleaner", "Human resource"};
        for (int i = 0; i < bgStatusNames.length; i++) {    // 19
            Set<Person> employees = new HashSet<>();
            for (int j = 0; j < 20 - (i+1)*(i+1)-i; j++)
                employees.add(personSupplier.get());
            bgEmployees.put(bgStatusNames[i], employees);
        }

        Map<String, Set<Person>> pedEmployees = new HashMap<>(4);
        String[] pedStatusNames = {"Producer", "Camera man", "Reporter", "Technician"};
        for (int i = 0; i < pedStatusNames.length; i++) {   // 6 producer
            Set<Person> employees = new HashSet<>(6);
            for (int j = 0; j < 6; j++)
                employees.add(personSupplier.get());
        }

        map.put(companyNames[0], firenteEmployees);
        map.put(companyNames[1], bgEmployees);
        map.put(companyNames[2], pedEmployees);
        return map;
    }

}//class
