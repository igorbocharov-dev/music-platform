package org.musicplatform.music.mapper.sound;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.musicplatform.music.dto.music.sound.SoundPageResponse;
import org.musicplatform.music.dto.music.sound.SoundResponse;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.mapper.image.ImageUrlMapper;

@Mapper(componentModel = "spring", uses = {SoundUrlMapper.class, ImageUrlMapper.class})
public interface SoundMapper {

    @Mapping(target = "url", source = "key", qualifiedByName = "mapMp3Url")
    @Mapping(target = "albumId", source = "album.id")
    SoundResponse toResponse(Sound sound);

    @Mapping(target = "url", source = "key", qualifiedByName = "mapMp3Url")
    @Mapping(target = "album.image.url", source = "album.image.key", qualifiedByName = "mapImgUrl")
    SoundPageResponse toPageResponse(Sound sound);
}
