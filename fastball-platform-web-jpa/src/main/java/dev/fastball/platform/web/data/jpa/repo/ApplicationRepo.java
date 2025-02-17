package dev.fastball.platform.web.data.jpa.repo;

import dev.fastball.platform.web.data.jpa.entity.JpaApplicationEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<JpaApplicationEntity, Long> {

    JpaApplicationEntity findByCode(String code);
}
