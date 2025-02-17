package dev.fastball.platform.web.data.jpa.repo;

import dev.fastball.platform.web.data.jpa.entity.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<JpaUserEntity, Long> {

    JpaUserEntity findByUsername(String username);

    JpaUserEntity findByMobile(String mobile);
}
