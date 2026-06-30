package org.musicplatform.music.mapper.music;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.musicplatform.music.dto.music.album.AlbumResponse;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.mapper.image.ImageUrlMapper;

@Mapper(componentModel = "spring", uses = {ImageUrlMapper.class})
public interface AlbumMapper {

    @Mapping(target = "image.url", source = "image.key", qualifiedByName = "mapImgUrl")
    AlbumResponse toAlbumResponse(Album album);
}
