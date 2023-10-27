package ru.loudbar.gaishnik.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.loudbar.gaishnik.models.NumberPlate;

public interface NumberPlateRepository extends JpaRepository<NumberPlate, Long> {
    boolean existsByNumber(String number);

    NumberPlate findTopByOrderByIdDesc();
}
