package com.tourflex.repository;

import com.tourflex.model.RefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Integer> {
}