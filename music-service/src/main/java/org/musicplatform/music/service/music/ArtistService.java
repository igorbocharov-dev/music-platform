package org.musicplatform.music.service.music;

import org.musicplatform.music.dto.music.artist.ArtistResponse;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.exception.music.MusicNotFoundException;
import org.musicplatform.music.repository.music.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public ArtistResponse viewArtistById(Long artistId){
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new MusicNotFoundException("Artist with id: " + artistId + " not found"));
        return new ArtistResponse(artist.getId(), artist.getName());
    }
}
