package org.musicplatform.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.musicplatform.auth.dto.ImageResponse;
import org.musicplatform.auth.entity.UserAvatar;

@Mapper(componentModel = "spring", uses = {ImageUrlMapper.class})
public interface ImageMapper {

    @Mapping(target = "url", source = "key", qualifiedByName = "mapImgUrl")
    ImageResponse toImageResponse(UserAvatar userAvatar);
}
