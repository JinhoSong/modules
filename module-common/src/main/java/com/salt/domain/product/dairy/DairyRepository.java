package com.salt.domain.product.dairy;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DairyRepository extends JpaRepository<Dairy, Integer> {
    List<Dairy> findAll();
    List<Dairy> findByStatusEquals(DairyStatus dailyStatus);

}
