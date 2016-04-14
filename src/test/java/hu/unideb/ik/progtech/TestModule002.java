package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.MP3;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by joey on 4/14/16.
 */
public class TestModule002 {

    static Module002Imp moduleImp;
    static MP3[] allTestMP3s;

    @BeforeClass
    public void setUp() {
        moduleImp = new Module002Imp();
        allTestMP3s = new MP3[20];
        allTestMP3s[0] = new MP3("3:4:5");

        allTestMP3s[1] = new MP3("0:3:45");
        allTestMP3s[1].setAlbumTitle("One Way");
        allTestMP3s[1].setTitle("Direction");
        allTestMP3s[1].setArtists(new HashSet<>(Arrays.asList("Drake", "Paul", "John", "Carl")));
        allTestMP3s[1].setRating(3);
        allTestMP3s[1].setSerialNumber(2);

        allTestMP3s[2] = new MP3("0:4:6");
        allTestMP3s[2].setRating(4);
        allTestMP3s[2].setTitle("Lost");
        allTestMP3s[2].setAlbumTitle("Direction");
        allTestMP3s[2].setArtists(new HashSet<>(Arrays.asList("John", "Carl"));
        allTestMP3s[2].setSerialNumber(1);

        allTestMP3s[3] = new MP3("1:12:0");
        allTestMP3s[3].setSerialNumber(5);
        allTestMP3s[3].setRating(5);
        allTestMP3s[3].setTitle();
    }

    @Test
    public void durationOfLongestSong() {




    }



}
