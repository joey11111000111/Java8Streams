package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.MP3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by joey on 4/14/16.
 */
public class Module002Imp {

    public int durationOfLongestSong(MP3[] songs) {
        return Arrays.stream(songs)
                .map(MP3::getLength)
                .mapToInt(s -> {
                    String[] parts = s.split(":");
                    int[] intParts = new int[3];
                    intParts[0] = Integer.parseInt(parts[0]);
                    intParts[1] = Integer.parseInt(parts[1]);
                    intParts[2] = Integer.parseInt(parts[2]);
                    return intParts[2] + 60 * intParts[1] + 60 * 60 * intParts[0];
                })
                .max()
                .getAsInt();
    }

    public Map<Boolean, List<MP3>> bestAndWorstSongs(MP3[] songs) {
        return Arrays.stream(songs)
                .filter(MP3::hasRating)
                .filter(mp3 -> mp3.getRating() == 5 || mp3.getRating() == 1)
                .collect(Collectors.partitioningBy(mp3 -> mp3.getRating() == 5));
    }

    public Map<String, List<MP3>> mapByAlbum(MP3[] songs) {
        Comparator<MP3> ratingSort = (mp1, mp2) -> {
            if (mp1.hasRating()) {
                if (mp2.hasRating())
                    return mp2.getRating() - mp1.getRating();
                else
                    return -1;
            } else {
                if (mp2.hasRating())
                    return 1;
                else
                    return mp2.getLength().compareTo(mp1.getLength());
            }
        };

        Map<String, List<MP3>> result = Arrays.stream(songs)
                .filter(MP3::hasAlbumTitle)
                .collect(Collectors.groupingBy(MP3::getAlbumTitle));
        result.entrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(list -> list.sort(ratingSort));
        return result;
    }

}
