package com.pms.cde.processPensionMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.cde.processPensionMicroservice.model.PensionDetail;


public interface ProcessPensionRepository extends JpaRepository<PensionDetail, Long>{

}
