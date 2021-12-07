package com.springboot.filemanipulation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.filemanipulation.model.FileInfo;

public interface FileStorageService {
	  public FileInfo saveFile(MultipartFile file,String tags) ;
	  public Optional<FileInfo> getFile(Integer fileId)  ;
	  public List<FileInfo> getFiles() ;
	  public List<FileInfo> getFilesbyTag(String tag) ;
}
