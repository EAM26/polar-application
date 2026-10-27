package com.em26.repository;

import com.em26.model.DataRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRowRepository extends JpaRepository<DataRow, Long> {
}
