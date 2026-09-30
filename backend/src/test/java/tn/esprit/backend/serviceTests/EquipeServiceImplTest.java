package tn.esprit.backend.serviceTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Equipe;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.EquipeRepository;
import tn.esprit.backend.repository.ProjetRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @Test
    void shouldAddEquipe() {
        Equipe equipe = Equipe.builder()
                .nom("Equipe Dev")
                .specialite("Java")
                .build();

        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);

        assertNotNull(result);
        assertEquals("Equipe Dev", result.getNom());
        verify(equipeRepository).save(equipe);
    }

    @Test
    void shouldUpdateEquipe() {
        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("Equipe Backend")
                .specialite("Spring Boot")
                .build();

        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.updateEquipe(equipe);

        assertEquals(1L, result.getId());
        assertEquals("Equipe Backend", result.getNom());
        verify(equipeRepository).save(equipe);
    }

    @Test
    void shouldDeleteEquipe() {
        Long id = 1L;

        doNothing().when(equipeRepository).deleteById(id);

        equipeService.deleteEquipe(id);

        verify(equipeRepository).deleteById(id);
    }

    @Test
    void shouldGetEquipeById() {
        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("Equipe Dev")
                .specialite("Java")
                .build();

        when(equipeRepository.findById(1L))
                .thenReturn(Optional.of(equipe));

        Equipe result = equipeService.getEquipeById(1L);

        assertNotNull(result);
        assertEquals("Equipe Dev", result.getNom());

        verify(equipeRepository).findById(1L);
    }

    @Test
    void shouldReturnNullWhenEquipeDoesNotExist() {
        when(equipeRepository.findById(99L))
                .thenReturn(Optional.empty());

        Equipe result = equipeService.getEquipeById(99L);

        assertNull(result);
    }

    @Test
    void shouldGetAllEquipes() {
        Equipe e1 = Equipe.builder()
                .id(1L)
                .nom("Equipe 1")
                .build();

        Equipe e2 = Equipe.builder()
                .id(2L)
                .nom("Equipe 2")
                .build();

        when(equipeRepository.findAll())
                .thenReturn(Arrays.asList(e1, e2));

        List<Equipe> result = equipeService.getAllEquipes();

        assertEquals(2, result.size());
        verify(equipeRepository).findAll();
    }

    @Test
    void shouldGetEquipesByEntreprise() {
        Long entrepriseId = 1L;

        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("Equipe Dev")
                .build();

        when(equipeRepository.findByEntrepriseId(entrepriseId))
                .thenReturn(List.of(equipe));

        List<Equipe> result =
                equipeService.getEquipesByEntreprise(entrepriseId);

        assertEquals(1, result.size());
        assertEquals("Equipe Dev", result.get(0).getNom());

        verify(equipeRepository)
                .findByEntrepriseId(entrepriseId);
    }

    @Test
    void shouldAssignEquipeToEntreprise() {
        Long equipeId = 1L;
        Long entrepriseId = 10L;

        Equipe equipe = Equipe.builder()
                .id(equipeId)
                .nom("Equipe Dev")
                .build();

        Entreprise entreprise = Entreprise.builder()
                .id(entrepriseId)
                .nom("ESPRIT")
                .build();

        when(equipeRepository.findById(equipeId))
                .thenReturn(Optional.of(equipe));

        when(entrepriseRepository.findById(entrepriseId))
                .thenReturn(Optional.of(entreprise));

        when(equipeRepository.save(equipe))
                .thenReturn(equipe);

        Equipe result =
                equipeService.assignEquipeToEntreprise(
                        equipeId,
                        entrepriseId
                );

        assertNotNull(result);
        assertEquals(entreprise, result.getEntreprise());

        verify(equipeRepository).findById(equipeId);
        verify(entrepriseRepository).findById(entrepriseId);
        verify(equipeRepository).save(equipe);
    }

    @Test
    void shouldAssignEquipeToProjet() {
        Long equipeId = 1L;
        Long projetId = 10L;

        Equipe equipe = Equipe.builder()
                .id(equipeId)
                .nom("Equipe Dev")
                .projets(new ArrayList<>())
                .build();

        Projet projet = Projet.builder()
                .id(projetId)
                .sujet("Projet DevOps")
                .build();

        when(equipeRepository.findById(equipeId))
                .thenReturn(Optional.of(equipe));

        when(projetRepository.findById(projetId))
                .thenReturn(Optional.of(projet));

        when(equipeRepository.save(equipe))
                .thenReturn(equipe);

        Equipe result =
                equipeService.assignEquipeToProjet(
                        equipeId,
                        projetId
                );

        assertNotNull(result);
        assertTrue(result.getProjets().contains(projet));

        verify(equipeRepository).findById(equipeId);
        verify(projetRepository).findById(projetId);
        verify(equipeRepository).save(equipe);
    }
}

