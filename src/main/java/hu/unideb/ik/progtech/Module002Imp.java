package hu.unideb.ik.progtech;

import hu.unideb.ik.progtech.helpclasses.MP3;

import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
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

    public String largestAlbum(MP3[] songs) {
        if (songs.length == 0)
            return null;

        return Arrays.stream(songs)
                .filter(MP3::hasAlbumTitle)
                .map(MP3::getAlbumTitle)
                .collect(Collectors.groupingBy(e -> e))
                .entrySet().stream()
                .map(e -> e.getKey() + ':' + e.getValue().size())
                .sorted((s1, s2) -> {
                    int s1Size = Integer.parseInt(s1.split(":")[1]);
                    int s2Size = Integer.parseInt(s2.split(":")[1]);
                    return s2Size - s1Size;
                })
                .findFirst()
                .get()
                .split(":")[0];
    }

    public Duration lengthOfLongestAlbum(MP3[] songs) {
        // Akinek van kevésbé maszek megoldása, az legyen szíves elküldeni...
        return Arrays.stream(songs)
                .filter(MP3::hasAlbumTitle)
                .collect(Collectors.groupingBy(MP3::getAlbumTitle))
                .entrySet().stream()
                .map(e -> {
                    String midResult = e.getKey();
                    Integer fullAlbumLength = e.getValue().stream()
                            .map(MP3::getLength)
                            .mapToInt(s -> {
                                String[] parts = s.split(":");
                                int length = Integer.parseInt(parts[2]);
                                length += 60 * Integer.parseInt(parts[1]);
                                length += 60 * 60 * Integer.parseInt(parts[0]);
                                return length;
                            })
                            .sum();
                    midResult += ':' + fullAlbumLength.toString();
                    return midResult;
                })
                .sorted((s1, s2) -> {
                    int s1Length = Integer.parseInt(s1.split(":")[1]);
                    int s2Length = Integer.parseInt(s2.split(":")[1]);
                    return s2Length - s1Length;
                })
                .limit(1)
                .map(s -> {
                    int length = Integer.parseInt(s.split(":")[1]);
                    return Duration.ofSeconds(length);
                })
                .findFirst()
                .get();
    }

    public Map<String, Integer> worksOfArtists(MP3[] songs) {
        Map<String, Integer> identity = new HashMap<>();
        BiFunction<Map<String, Integer>, Set<String>, Map<String, Integer>> accumulator = (map, set) -> {
            set.stream().forEach(artist -> {
                if (map.containsKey(artist))
                    map.put(artist, map.get(artist) + 1);
                else
                    map.put(artist, 1);
            });
            return map;
        };
        BinaryOperator<Map<String, Integer>> combiner = (mapLeft, mapRight) -> {
            Set<String> allKeys = mapLeft.keySet();
            allKeys.addAll(mapRight.keySet());
            allKeys.stream().forEach(key -> {
                if (mapLeft.containsKey(key)) {
                    if (mapRight.containsKey(key))
                        mapLeft.put(key, mapLeft.get(key) + mapRight.get(key));
                }
                else
                    mapLeft.put(key, mapRight.get(key));

            });
            return mapLeft;
        };

        return Arrays.stream(songs)
                .filter(MP3::hasArtists)
                .map(MP3::getArtists)
                .reduce(identity, accumulator, combiner);
    }

}//class
