package org.musicplatform.music.support.factory.it;

import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.image.AlbumImage;
import org.musicplatform.music.entity.likes.AlbumLike;
import org.musicplatform.music.entity.likes.SoundLike;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.integration.jamendo.response.MusicResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MusicFactoryIT {

    public static List<MusicResponse> musicResponseList(int size){
        List<MusicResponse> responseList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            responseList.add(new MusicResponse(
                    "track_" + i,
                    250,
                    "Mr.Kitty",
                    "Time",
                    LocalDate.of(2008, 10, 29),
                    "https://album_" + i,
                    "https://track_" + i + "_download",
                    true,
                    null, null));
        }
        return responseList;
    }

    public static Artist artist(Genre genre) {
        return new Artist("Muse", genre);
    }

    public static Album album(Artist artist, Genre genre) {
        return new Album("Black Holes and Revelations", LocalDate.of(2003, 3,5), artist, genre);
    }

    public static AlbumImage albumImage(Album album) {
        return new AlbumImage(UUID.randomUUID() + ".jpg", album);
    }

    public static Sound sound(Artist artist, Album album, Genre genre){
        return new Sound("Supermassive Black Hole", 280,
                artist, album, album.getTitle() + "/supermassive_black_hole.mp3",
                LocalDate.of(2003, 3,5), genre);
    }

    public static AlbumLike albumLike(Long userId, Album album){
        return new AlbumLike(userId, album);
    }

    public static SoundLike soundLike(Long userId, Sound sound){
        return new SoundLike(userId, sound);
    }
}

