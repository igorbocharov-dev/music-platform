package org.musicplatform.music.entity.likes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.musicplatform.music.entity.music.Album;

import java.time.Instant;

@Entity
@Table(name = "album_like")
@Getter
@Setter
public class AlbumLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public AlbumLike(){}

    public AlbumLike(Long userId, Album album) {
        this.userId = userId;
        this.album = album;
    }
}
