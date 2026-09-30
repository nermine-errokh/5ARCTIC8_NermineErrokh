package tn.esprit.backend.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceImplTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServiceImpl entrepriseService;

    @Test
    void shouldAddEntreprise() {
        Entreprise entreprise = Entreprise.builder()
                .nom("ESPRIT")
                .adresse("Ariana")
                .build();

        when(entrepriseRepository.save(entreprise)).thenReturn(entreprise);

        Entreprise result = entrepriseService.addEntreprise(entreprise);

        assertNotNull(result);
        assertEquals("ESPRIT", result.getNom());
        verify(entrepriseRepository).save(entreprise);
    }

    @Test
    void shouldUpdateEntreprise() {
        Entreprise entreprise = Entreprise.builder()
                .id(1L)
                .nom("ESPRIT Updated")
                .adresse("Ariana")
                .build();

        when(entrepriseRepository.save(entreprise)).thenReturn(entreprise);

        Entreprise result = entrepriseService.updateEntreprise(entreprise);

        assertEquals(1L, result.getId());
        assertEquals("ESPRIT Updated", result.getNom());
        verify(entrepriseRepository).save(entreprise);
    }

    @Test
    void shouldDeleteEntreprise() {
        Long id = 1L;

        doNothing().when(entrepriseRepository).deleteById(id);

        entrepriseService.deleteEntreprise(id);

        verify(entrepriseRepository).deleteById(id);
    }

    @Test
    void shouldGetEntrepriseById() {
        Entreprise entreprise = Entreprise.builder()
                .id(1L)
                .nom("ESPRIT")
                .adresse("Ariana")
                .build();

        when(entrepriseRepository.findById(1L))
                .thenReturn(Optional.of(entreprise));

        Entreprise result = entrepriseService.getEntrepriseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ESPRIT", result.getNom());

        verify(entrepriseRepository).findById(1L);
    }

    @Test
    void shouldReturnNullWhenEntrepriseDoesNotExist() {
        when(entrepriseRepository.findById(99L))
                .thenReturn(Optional.empty());

        Entreprise result = entrepriseService.getEntrepriseById(99L);

        assertNull(result);
        verify(entrepriseRepository).findById(99L);
    }

    @Test
    void shouldGetAllEntreprises() {
        Entreprise e1 = Entreprise.builder()
                .id(1L)
                .nom("ESPRIT")
                .build();

        Entreprise e2 = Entreprise.builder()
                .id(2L)
                .nom("Microsoft")
                .build();

        when(entrepriseRepository.findAll())
                .thenReturn(Arrays.asList(e1, e2));

        List<Entreprise> result = entrepriseService.getAllEntreprises();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ESPRIT", result.get(0).getNom());

        verify(entrepriseRepository).findAll();
    }
}


