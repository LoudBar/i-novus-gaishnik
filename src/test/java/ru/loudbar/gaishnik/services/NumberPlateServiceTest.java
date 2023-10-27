package ru.loudbar.gaishnik.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.loudbar.gaishnik.models.NumberPlate;
import ru.loudbar.gaishnik.repositories.NumberPlateRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class NumberPlateServiceTest {
    private NumberPlateService numberPlateService;
    private NumberPlateRepository numberPlateRepository;

    @BeforeEach
    void setUp() {
        numberPlateRepository = mock(NumberPlateRepository.class);
        numberPlateService = new NumberPlateService(numberPlateRepository);
    }

    @Test
    @Transactional
    public void testGetRandomNumber() {
        when(numberPlateRepository.existsByNumber(anyString())).thenReturn(false);

        String randomNumber = numberPlateService.getRandomNumber();

        assertNotNull(randomNumber);
        verify(numberPlateRepository, times(1)).save(any(NumberPlate.class));
    }

    @Test
    @Transactional
    public void testGetNextNumber() {
        when(numberPlateRepository.existsByNumber(anyString())).thenReturn(false);
        when(numberPlateRepository.findTopByOrderByIdDesc()).thenReturn(NumberPlate.builder().build());

        String nextNumber = numberPlateService.getNextNumber();

        assertNotNull(nextNumber);
        verify(numberPlateRepository, times(1)).save(any(NumberPlate.class));
    }
}
