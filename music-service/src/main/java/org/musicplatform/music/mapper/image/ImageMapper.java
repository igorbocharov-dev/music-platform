package org.musicplatform.music.mapper.image;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.musicplatform.music.dto.image.ImageResponse;
import org.musicplatform.music.entity.image.UserAvatar;

@Mapper(componentModel = "spring", uses = {ImageUrlMapper.class})
public interface ImageMapper {

    @Mapping(target = "url", source = "key", qualifiedByName = "mapImgUrl")
    ImageResponse toImageResponse(UserAvatar userAvatar);
}
