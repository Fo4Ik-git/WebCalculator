package com.fo4ik.momcalculator.repo;


import com.fo4ik.momcalculator.models.Elements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository

public interface ElementsRepo extends JpaRepository<Elements, Long> {

    Elements findByName(String surface);

    List<Elements> findAll();

    Elements findByValue(Float value);
}
