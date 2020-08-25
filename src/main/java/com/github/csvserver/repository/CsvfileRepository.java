package com.github.csvserver.repository;

import com.github.csvserver.domain.Csvfile;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Csvfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CsvfileRepository extends JpaRepository<Csvfile, Long> {
}
