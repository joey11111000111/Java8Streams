package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.MP3;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Created by joey on 4/14/16.
 */
public class TestModule002 {

    static Module002Imp moduleImp;
    MP3[] allTestMP3s;

    @BeforeClass
    public static void setUp() {
        moduleImp = new Module002Imp();
    }

    @Before
    public void createTestData() {
        allTestMP3s = new MP3[14];
        allTestMP3s[0] = new MP3("3:4:5");
        allTestMP3s[1] = new MP3("0:3:45")
                .setAlbumTitle("One Way").setTitle("Direction").setRating(3).setSerialNumber(2)
                .setArtists(new HashSet<>(Arrays.asList("Drake", "Paul", "John", "Carl")));
        allTestMP3s[2] = new MP3("0:4:6")
                .setRating(4).setTitle("Lost").setAlbumTitle("Direction")
                .setArtists(new HashSet<>(Arrays.asList("John", "Carl"))).setSerialNumber(1);
        allTestMP3s[3] = new MP3("1:12:0")
                .setSerialNumber(5).setRating(5).setTitle("Beautiful Voices")
                .setAlbumTitle("Chill you out").setRating(5).setSerialNumber(1)
                .setArtists(new HashSet<>(Arrays.asList("Roger Shash")));
        allTestMP3s[4] = new MP3("0:6:32")
                .setTitle("Stringer").setAlbumTitle("Direction").setRating(1).setSerialNumber(12);
        allTestMP3s[5] = new MP3("0:9:25")
                .setAlbumTitle("Zerena").setRating(2).setSerialNumber(4)
                .setArtists(new HashSet<>(Arrays.asList("Zelda", "Uktar")));
        allTestMP3s[6] = new MP3("2:0:6")
                .setAlbumTitle("Zerena").setRating(4).setSerialNumber(1).setTitle("Piano chill");
        allTestMP3s[7] = new MP3("0:2:20").setRating(5);
        allTestMP3s[8] = new MP3("0:7:18");
        allTestMP3s[9] = new MP3("12:0:0").setAlbumTitle("Annoy your neighbours")
                .setTitle("Massive deathmetal techno mix rapping").setRating(1)
                .setArtists(new HashSet<>(Arrays.asList("Kampou", "Monster", "Fighter")));
        allTestMP3s[10] = new MP3("0:34:33")
                .setAlbumTitle("Zerena").setTitle("Psychedelic downtempo").setRating(4);
        allTestMP3s[11] = new MP3("0:3:29")
                .setAlbumTitle("Lonely album").setTitle("Running").setSerialNumber(2)
                .setArtists(new HashSet<>(Arrays.asList("Jake")));
        allTestMP3s[13] = new MP3("0:9:54").setAlbumTitle("Zerena");
        allTestMP3s[12] = new MP3("2:10:36").setAlbumTitle("Zerena");
    }

    private int getLengthInSeconds(String length) {
        String[] parts = length.split(":");
        if (parts.length != 3)
            throw new IllegalArgumentException("given length is invalid");

        int[] intParts = new int[3];
        intParts[0] = Integer.parseInt(parts[0]);
        intParts[1] = Integer.parseInt(parts[1]);
        intParts[2] = Integer.parseInt(parts[2]);
        return intParts[2] + 60 * intParts[1] + 60 * 60 * intParts[0];
    }

    /*
    Adott egy mp3 objektumokat tartalmazó tömb. A függvényhívás eredménye adjon vissza egy int-et,
    ami a leghosszabb zeneszám hosszát reprezentálja másodpercekben.
     */
    @Test
    public void durationOfLongestSongTest() {
        // Generate expected result
        int max = -1;
        for (MP3 mp3 : allTestMP3s) {
            int length = getLengthInSeconds(mp3.getLength());
            if (length > max)
                max = length;
        }
        int expected = max;     // rename

        int result = moduleImp.durationOfLongestSong(allTestMP3s);
        assertEquals(expected, result);
    }

    /*
    Adott egy mp3 objektumokat tartalmazó tömb. A függvényhívás eredménye adjon vissza egy
    Map<Boolean, List<MP3>> objektumot. Az igaz kulcs melletti lista tartalmazza az 5-re értékelt
    zenéket, a hamis kulcs melletti pedig az 1-re értékelt zenéket
     */
    @Test
    public void bestAndWorstSongsTest() {
        // Generate expected result
        Map<Boolean, List<MP3>> expected = new HashMap<>();
        expected.put(true, new ArrayList<>());
        expected.put(false, new ArrayList<>());
        for (MP3 mp3 : allTestMP3s) {
            if (!mp3.hasRating())
                continue;
            switch (mp3.getRating()) {
                case 5: expected.get(true).add(mp3); break;
                case 1: expected.get(false).add(mp3); break;
            }
        }

        Map<Boolean, List<MP3>> result = moduleImp.bestAndWorstSongs(allTestMP3s);
        assertEquals(expected, result);
    }

    /*
    Rendezd albumokba a zeneszámokat (Csak azokat, amelyeknél az album meg van adva)!
    (Az eredmény Map<string[album címe], List<MP3>> legyen)!
    A zeneszámok sorszám szerint növekvőbe legyenek rendezve, a lista végén legyenek azok, amiknek nincs sorszámuk!
    A sorszámmal nem rendelkező zeneszámok a lista végén egymás között hossz szerint csökkenő sorrendben
    legyenek rendezve.
     */
    @Test
    public void mapByAlbumTest() {
        // Generate expected result
        Map<String, List<MP3>> expected = new HashMap<>();
        for (MP3 mp3 : allTestMP3s) {
            if (!mp3.hasAlbumTitle())
                continue;
            String albumTitle = mp3.getAlbumTitle();
            if (!expected.containsKey(albumTitle))
                expected.put(albumTitle, new ArrayList<>());
            expected.get(albumTitle).add(mp3);
        }
        // sort the lists
        Comparator<MP3> ratingSort = (mp1, mp2) -> {
            if (mp1.hasRating()) {
                if (!mp2.hasRating())
                    return -1;
                else
                    return mp2.getRating() - mp1.getRating();
            }
            else {
                if (mp2.hasRating())
                    return 1;
                else {
                    int mp1Length = getLengthInSeconds(mp1.getLength());
                    int mp2Length = getLengthInSeconds(mp2.getLength());
                    return mp2Length - mp1Length;
                }
            }
        };

        for (Map.Entry<String, List<MP3>> entry : expected.entrySet())
            entry.getValue().sort(ratingSort);

        // test result
        Map<String, List<MP3>> result = moduleImp.mapByAlbum(allTestMP3s);
        assertEquals(expected, result);
    }



}
