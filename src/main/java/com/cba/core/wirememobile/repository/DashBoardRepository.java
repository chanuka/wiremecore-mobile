package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.UserConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface 

DashBoardRepository extends JpaRepository<UserConfig, Integer>, JpaSpecificationExecutor<UserConfig> {

    List<UserConfig> findByUser_NameAndConfigType(String userName, String configType);

//    List<UserConfig> findByConfigName(String configName);

    Optional<UserConfig> findByUser_NameAndConfigName(String userName, String configName);

    void deleteByUser_NameAndConfigName(String userName, String configName);

}
