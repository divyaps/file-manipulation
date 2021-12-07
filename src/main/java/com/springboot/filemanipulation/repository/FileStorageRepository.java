package com.springboot.filemanipulation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.filemanipulation.model.FileInfo;

public interface FileStorageRepository extends JpaRepository<FileInfo,Integer>{
	@Query("select d from #{#entityName} d where ?1 MEMBER d.tags")
	  List<FileInfo> findAllByTag(String tag);

}

