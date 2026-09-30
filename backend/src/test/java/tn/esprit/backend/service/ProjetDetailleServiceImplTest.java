
package tn.esprit.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.entity.ProjetDetaille;
import tn.esprit.backend.repository.ProjetDetailleRepository;
import tn.esprit.backend.repository.ProjetRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetDetailleServiceImplTest {

    @Mock
    private ProjetDetailleRepository projetDetailleRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetDetailleServiceImpl projetDetailleService;

    @Test
    void shouldAddProjetDetaille() {
        ProjetDetaille detail = ProjetDetaille.builder()
                .description("Application DevOps")
                .technologie("Spring Boot")
                .coutProvisoire(5000.0)
                .dateDebut(LocalDate.of(2026, 1, 1))
                .build();

        when(projetDetailleRepository.save(detail))
                .thenReturn(detail);

        ProjetDetaille result =
                projetDetailleService.addProjetDetaille(detail);

        assertNotNull(result);
        assertEquals("Spring Boot", result.getTechnologie());
        assertEquals(5000.0, result.getCoutProvisoire());

        verify(projetDetailleRepository).save(detail);
    }

    @Test
    void shouldUpdateProjetDetaille() {
        ProjetDetaille detail = ProjetDetaille.builder()
                .id(1L)
                .description("Updated project")
                .technologie("Angular")
                .build();

        when(projetDetailleRepository.save(detail))
                .thenReturn(detail);

        ProjetDetaille result =
                projetDetailleService.updateProjetDetaille(detail);

        assertEquals(1L, result.getId());
        assertEquals("Angular", result.getTechnologie());

        verify(projetDetailleRepository).save(detail);
    }

    @Test
    void shouldDeleteProjetDetaille() {
        Long id = 1L;

        doNothing().when(projetDetailleRepository)
                .deleteById(id);

        projetDetailleService.deleteProjetDetaille(id);

        verify(projetDetailleRepository).deleteById(id);
    }

    @Test
    void shouldGetProjetDetailleById() {
        ProjetDetaille detail = ProjetDetaille.builder()
                .id(1L)
                .description("Application")
                .technologie("Spring Boot")
                .build();

        when(projetDetailleRepository.findById(1L))
                .thenReturn(Optional.of(detail));

        ProjetDetaille result =
                projetDetailleService.getProjetDetailleById(1L);

        assertNotNull(result);
        assertEquals("Application", result.getDescription());

        verify(projetDetailleRepository).findById(1L);
    }

    @Test
    void shouldReturnNullWhenProjetDetailleDoesNotExist() {
        when(projetDetailleRepository.findById(99L))
                .thenReturn(Optional.empty());

        ProjetDetaille result =
                projetDetailleService.getProjetDetailleById(99L);

        assertNull(result);
    }

    @Test
    void shouldGetAllProjetsDetailles() {
        ProjetDetaille d1 = ProjetDetaille.builder()
                .id(1L)
                .description("Projet détaillé 1")
                .build();

        ProjetDetaille d2 = ProjetDetaille.builder()
                .id(2L)
                .description("Projet détaillé 2")
                .build();

        when(projetDetailleRepository.findAll())
                .thenReturn(Arrays.asList(d1, d2));

        List<ProjetDetaille> result =
                projetDetailleService.getAllProjetsDetailles();

        assertEquals(2, result.size());
        verify(projetDetailleRepository).findAll();
    }

    @Test
    void shouldGetProjetDetaillesByProjet() {
        Long projetId = 1L;

        ProjetDetaille detail = ProjetDetaille.builder()
                .id(1L)
                .description("Projet DevOps")
                .build();

        when(projetDetailleRepository.findByProjetId(projetId))
                .thenReturn(List.of(detail));

        List<ProjetDetaille> result =
                projetDetailleService.getProjetDetaillesByProjet(projetId);

        assertEquals(1, result.size());
        assertEquals("Projet DevOps",
                result.get(0).getDescription());

        verify(projetDetailleRepository)
                .findByProjetId(projetId);
    }

    @Test
    void shouldAssignProjetDetailleToProjet() {
        Long detailId = 1L;
        Long projetId = 10L;

        ProjetDetaille detail = ProjetDetaille.builder()
                .id(detailId)
                .description("Application")
                .build();

        Projet projet = Projet.builder()
                .id(projetId)
                .sujet("Projet DevOps")
                .build();

        when(projetDetailleRepository.findById(detailId))
                .thenReturn(Optional.of(detail));

        when(projetRepository.findById(projetId))
                .thenReturn(Optional.of(projet));

        when(projetDetailleRepository.save(detail))
                .thenReturn(detail);

        ProjetDetaille result =
                projetDetailleService.assignProjetDetailleToProjet(
                        detailId,
                        projetId
                );

        assertNotNull(result);
        assertEquals(projet, result.getProjet());

        verify(projetDetailleRepository).findById(detailId);
        verify(projetRepository).findById(projetId);
        verify(projetDetailleRepository).save(detail);
    }
}

