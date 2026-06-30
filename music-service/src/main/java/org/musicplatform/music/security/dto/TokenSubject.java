package org.musicplatform.music.security.dto;

import java.util.Collection;

public record TokenSubject(Long userId, Collection<String> roles) {
}
