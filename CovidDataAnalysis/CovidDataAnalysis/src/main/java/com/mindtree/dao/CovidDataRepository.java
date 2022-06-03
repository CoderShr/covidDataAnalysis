package com.mindtree.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindtree.entity.CovidData;

public interface CovidDataRepository extends JpaRepository<CovidData, Integer>{

	
	public List<CovidData> findAll();
}
