package org.musicplatform.music.repository.music;

import org.musicplatform.music.entity.music.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByName(String name);

    Page<Artist> findByNameStartingWithIgnoreCase(String prefix, Pageable pageable);
}