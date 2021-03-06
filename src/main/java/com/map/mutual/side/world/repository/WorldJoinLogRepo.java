package com.map.mutual.side.world.repository;

import com.map.mutual.side.world.model.entity.WorldEntity;
import com.map.mutual.side.world.model.entity.WorldJoinLogEntity;
import com.map.mutual.side.world.repository.dsl.WorldRepoDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldJoinLogRepo extends JpaRepository<WorldJoinLogEntity, Long> {
}
