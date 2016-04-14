package hu.unideb.ik.progtech.helpclasses;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by joey on 4/14/16.
 */
public class MP3 {

    private String title;           // null if not given, never empty string
    private Set<String> artists;    // empty set if no artist is given, doesn't have empty strings nor null strings
    private String albumTitle;      // null if not given, never empty string
    private Integer serialNumber;   // >= 1 or null if not given
    private String length;          // in hour:minute:second format, never null
    private Integer rating;         // from 1 to 5 (both including) or null if not given

    public MP3(String title, Set<String> artists, String albumTitle, Integer serialNumber,
               String length, Integer rating) {
/*        if (!validTitle(title))
            throw new IllegalArgumentException("invalid title");
        if (!validartists(artists))
            throw new IllegalArgumentException("invalid artists");
        if (!validAlbumTitle(albumTitle))
            throw new IllegalArgumentException("invalid album title");
        if (!validSerialNumber(serialNumber))
            throw new IllegalArgumentException("invalid serial number");
        if (!validLength(length))
            throw new IllegalArgumentException("invalid length");
        if (!validRating(rating))
            throw new IllegalArgumentException("invalid rating");*/
        setTitle(title);
        setArtists(artists);
        setAlbumTitle(albumTitle);
        setSerialNumber(serialNumber);
        setLength(length);
        setRating(rating);

/*        this.title = title;
        this.artists = artists;
        this.albumTitle = albumTitle;
        this.serialNumber = serialNumber;
        this.length = length;
        this.rating = rating;*/
    }

    public MP3(String length) {
        this(null, new HashSet<>(), null, null, length, null);
    }

    public boolean validTitle(String title) {
        return !(title != null && title.isEmpty());
    }
    public boolean validartists(Set<String> artists) {
        if (artists == null)
            return true;

        return artists.stream()
                .map(a -> validArtist(a))
                .anyMatch(bool -> !bool);
    }
    public boolean validArtist(String artist) {
        return !(artist != null && artist.isEmpty());
    }
    public boolean validAlbumTitle(String albumTitle) {
        return !(albumTitle != null && albumTitle.isEmpty());
    }
    public boolean validSerialNumber(Integer serialNumber) {
        return !(serialNumber != null && serialNumber < 1);
    }
    public boolean validRating(Integer rating) {
        return !(serialNumber != null && (serialNumber < 1 || serialNumber > 5));
    }
    public boolean validLength(String length) {
        if (length == null)
            return false;
        if (length.isEmpty())
            return false;
        String[] parts = length.trim().split(":");
        if (parts.length != 3)
            return false;
        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            int second = Integer.parseInt(parts[2]);
            LocalTime lt = LocalTime.of(hour % 24, minute, second);
        } catch (Exception e) {
            return false;
        }

        return true;
    }



    public boolean hasTitle() {
        return title != null;
    }
    public String getTitle() {
        return title;
    }

    public boolean hasAtists() {
        return artists.size() > 0;
    }
    public Set<String> getArtists() {
        return artists;
    }

    public boolean hasAlbumTitle() {
        return albumTitle != null;
    }
    public String getAlbumTitle() {
        return albumTitle;
    }

    public boolean hasSerialNumber() {
        return serialNumber != null;
    }
    public Integer getSerialNumber() {
        return serialNumber;
    }

    public boolean hasRating() {
        return rating != null;
    }
    public Integer getRating() {
        return rating;
    }

    public String getLength() {
        return length;
    }

    public void setTitle(String title) {
        if (!validTitle(title))
            throw new IllegalArgumentException("invalid title");
        this.title = title;
    }

    public void setArtists(Set<String> artists) {
        if (!validartists(artists))
            throw new IllegalArgumentException("invalid artists");
        this.artists = artists;
    }

    public void setAlbumTitle(String albumTitle) {
        if (!validAlbumTitle(albumTitle))
            throw new IllegalArgumentException("invalid album title");
        this.albumTitle = albumTitle;
    }

    public void setSerialNumber(Integer serialNumber) {
        if (!validSerialNumber(serialNumber))
            throw new IllegalArgumentException("invalid serial number");
        this.serialNumber = serialNumber;
    }

    public void setLength(String length) {
        if (!validLength(length))
            throw new IllegalArgumentException("invalid length");
        this.length = length;
    }

    public void setRating(Integer rating) {
        if (!validRating(rating))
            throw new IllegalArgumentException("invalid rating");
        this.rating = rating;
    }
}//class
