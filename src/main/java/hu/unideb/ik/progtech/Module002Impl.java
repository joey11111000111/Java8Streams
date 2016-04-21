package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.MP3;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by joey on 4/21/16.
 */
public class Module002Impl {

    private int lengthInSeconds(String length) {
        String[] parts = length.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return seconds + 60 * minutes + 60 * 60 * hours;
    }

    public int durationOfLongestSong(MP3[] songs) {
        return Arrays.stream(songs)
                .mapToInt(mp3 -> lengthInSeconds(mp3.getLength()))
                .max().getAsInt();
    }


    public Map<Boolean, List<MP3>> bestAndWorstSongs(MP3[] songs) {
        return Arrays.stream(songs)
                .filter(MP3::hasRating)
                .filter(mp3 -> mp3.getRating() == 5 || mp3.getRating() == 1)
                .collect(Collectors.partitioningBy(mp3 -> mp3.getRating() == 5));
    }


    public Map<String, List<MP3>> mapByAlbum(MP3[] songs) {
        Comparator<MP3> comparator = (mp1, mp2) -> {
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
                    int mp1Length = lengthInSeconds(mp1.getLength());
                    int mp2Length = lengthInSeconds(mp2.getLength());
                    return mp2Length - mp1Length;
                }
            }
        };

        Map<String, List<MP3>> result = Arrays.stream(songs)
                .filter(MP3::hasAlbumTitle)
                .collect(Collectors.groupingBy(MP3::getAlbumTitle));
        result.values().stream().forEach(list -> Collections.sort(list, comparator));
        return result;
    }


    public String largestAlbum(MP3[] songs) {
        return Arrays.stream(songs)
                .filter(MP3::hasAlbumTitle)
                .collect(Collectors.groupingBy(MP3::getAlbumTitle, Collectors.summingInt(le -> 1)))
                .entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get().getKey();
    }


    public Duration lengthOfLongestAlbum(MP3[] songs) {
        return Duration.ofSeconds(
                Arrays.stream(songs)
                        .filter(MP3::hasAlbumTitle)
                        .collect(Collectors.groupingBy(MP3::getAlbumTitle,
                                Collectors.summingInt(mp3 -> lengthInSeconds(mp3.getLength()))))
                        .entrySet().stream()
                        .mapToInt(Map.Entry::getValue)
                        .max().getAsInt()
        );


    }


    public Map<String, Integer> worksOfArtists(MP3[] songs) {
        return Arrays.stream(songs)
                .filter(MP3::hasArtists)
                .map(MP3::getArtists)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(artist -> artist, Collectors.summingInt(le -> 1)));
    }


}//class
