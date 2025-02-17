package dev.fastball.platform.web.data.jpa.repo;

import dev.fastball.platform.core.model.context.Menu;
import dev.fastball.platform.web.data.jpa.entity.JpaMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepo extends JpaRepository<JpaMenuEntity, Long> {

    JpaMenuEntity findByCode(String code);

    List<JpaMenuEntity> findByApplicationIdAndIdIn(Long applicationId, List<Long> menuIdList);
}
