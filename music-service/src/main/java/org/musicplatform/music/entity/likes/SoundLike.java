package org.musicplatform.music.entity.likes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.musicplatform.music.entity.music.Sound;

import java.time.Instant;


@Entity
@Table(name = "sound_like")
@Getter
@Setter
public class SoundLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sound_id")
    private Sound sound;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public SoundLike(){}

    public SoundLike(Long userId, Sound sound) {
        this.userId = userId;
        this.sound = sound;
    }
}
