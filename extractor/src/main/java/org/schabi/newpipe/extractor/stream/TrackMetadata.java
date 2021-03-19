package org.schabi.newpipe.extractor.stream;

import org.schabi.newpipe.extractor.localization.DateWrapper;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.schabi.newpipe.extractor.utils.Utils.isNullOrEmpty;

public class TrackMetadata implements Serializable {

    private String title;
    private String artist;
    private String album;
    private DateWrapper releaseDate;

    public TrackMetadata(final String title, final String artist, final String album, final DateWrapper releaseDate) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
    }

    public TrackMetadata() {}

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public DateWrapper getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setReleaseDate(DateWrapper dateWrapper) {
        this.releaseDate = dateWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackMetadata that = (TrackMetadata) o;
        return     Objects.equals(title, that.title)
                && Objects.equals(artist, that.artist)
                && Objects.equals(album, that.album)
                && (releaseDate == that.releaseDate) ||
                    ((releaseDate != null && that.releaseDate != null)
                            && Objects.equals(releaseDate.offsetDateTime(), that.releaseDate.offsetDateTime()));
    }

    public boolean isEmpty() {
        return  isNullOrEmpty(title) &&
                isNullOrEmpty(artist) &&
                isNullOrEmpty(album) &&
                releaseDate == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, album, releaseDate.offsetDateTime());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!isNullOrEmpty(title)) builder.append("title:\t").append(title).append("\n");
        if (!isNullOrEmpty(artist)) builder.append("artist:\t").append(artist).append("\n");
        if (!isNullOrEmpty(album)) builder.append("album:\t").append(album).append("\n");
        if (!(releaseDate == null)) builder.append("releaseDate:\t").append(
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDate.offsetDateTime())
        ).append("\n");
        return builder.toString();
    }
}
