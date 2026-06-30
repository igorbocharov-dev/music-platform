package org.musicplatform.music.unit.service.music;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musicplatform.music.exception.music.GenreDoesNotExistException;
import org.musicplatform.music.repository.music.GenreRepository;
import org.musicplatform.music.service.music.GenreService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @Test
    void genreResponseById_ShouldThrowGenreDoesNotExistException_WhenIdIsInvalid(){
        Long id = 289L;
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(GenreDoesNotExistException.class, () -> genreService.genreResponseById(id));
    }
}
