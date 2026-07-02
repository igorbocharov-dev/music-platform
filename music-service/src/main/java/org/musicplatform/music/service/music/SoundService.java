package org.musicplatform.music.service.music;

import org.musicplatform.music.dto.music.common.PageResponse;
import org.musicplatform.music.dto.music.sound.SoundPageResponse;
import org.musicplatform.music.dto.music.sound.SoundResponse;
import org.musicplatform.music.dto.music.sound.SoundsResponse;
import org.musicplatform.music.entity.likes.SoundLike;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.exception.music.MusicNotFoundException;
import org.musicplatform.music.mapper.sound.SoundMapper;
import org.musicplatform.music.repository.music.SoundRepository;
import org.musicplatform.music.service.likes.SoundLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SoundService {

    private final SoundRepository soundRepository;
    private final SoundLikeService soundLikeService;
    private final SoundMapper soundMapper;

    @Autowired
    public SoundService(SoundRepository soundRepository, SoundLikeService soundLikeService, SoundMapper soundMapper) {
        this.soundRepository = soundRepository;
        this.soundLikeService = soundLikeService;
        this.soundMapper = soundMapper;
    }

    public SoundsResponse getSoundsByAlbumId(Long albumId){
        List<SoundResponse> sounds = soundRepository.findAllByAlbumId(albumId)
                .stream().map(soundMapper::toResponse).toList();
        if(sounds.isEmpty()) throw new MusicNotFoundException("Треки не найдены");
        return new SoundsResponse(sounds);
    }

    private PageResponse<SoundResponse> toPageResponse(Page<Sound> soundsPage){
        List<Sound> sounds = soundsPage.getContent();
        if(sounds.isEmpty()) throw new MusicNotFoundException("Треки не найдены");
        List<SoundResponse> response = sounds.stream().map(soundMapper::toResponse).toList();
        return new PageResponse<>(response, soundsPage.hasNext());
    }

    public PageResponse<SoundResponse> getSoundsByArtistIdPaged(Long artistId, int page, int size){
        Page<Sound> soundsPage = soundRepository.findByArtistId
                (artistId, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        return toPageResponse(soundsPage);
    }

    public PageResponse<SoundResponse> getSoundsByGenreIdPaged(Long genreId, int page, int size){
        Page<Sound> soundsPage = soundRepository.findByGenreId
                (genreId, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        return toPageResponse(soundsPage);
    }

    public PageResponse<SoundResponse> getTrackCollectionByUserId(Long userId, int page, int size){
        Page<SoundLike> pageResponse = soundLikeService.findSoundLikesByUserid(userId, PageRequest.of(page, size));
        List<SoundResponse> response = pageResponse.getContent()
                .stream().map(SoundLike::getSound).map(soundMapper::toResponse).toList();
        return new PageResponse<>(response, pageResponse.hasNext());
    }

    public SoundPageResponse getSoundPageResponseById(Long id) {
        Sound sound = soundRepository.findByIdForSoundPage(id)
                .orElseThrow(()-> new MusicNotFoundException("Песня не найдена"));
        return soundMapper.toPageResponse(sound);
    }
}
