package com.fo4ik.momcalculator.repo;


import com.fo4ik.momcalculator.models.Logo;
import com.fo4ik.momcalculator.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface LogoRepo extends JpaRepository<Logo, Long> {
    Logo findById(long id);
    Logo findByUser(User userFromDb);
}
