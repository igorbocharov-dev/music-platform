package org.musicplatform.auth.dto;

import java.util.Collection;

public record TokenSubject(Long userId, Collection<String> roles) {
}
