package edu.icet.ecom.repository;

import edu.icet.ecom.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    // Add custom query methods if needed
}