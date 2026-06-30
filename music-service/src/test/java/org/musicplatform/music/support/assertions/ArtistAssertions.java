package org.musicplatform.music.support.assertions;

import org.musicplatform.music.entity.music.Artist;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArtistAssertions {

    public static void assertArtists(List<Artist> artists, String artistNamePrefix){
        assertThat(artists).extracting(Artist::getId).isNotEmpty();
        assertThat(artists).extracting(Artist::getName).allMatch(name -> name.startsWith(artistNamePrefix));
    }
}
