package org.musicplatform.music.service.uploadData;

import org.musicplatform.music.dto.metadata.TrackMetadata;

public interface TrackMetadataWriter {
    void write(TrackMetadata metadata);
}
