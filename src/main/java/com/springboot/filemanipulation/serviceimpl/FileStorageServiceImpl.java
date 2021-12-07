package com.springboot.filemanipulation.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.filemanipulation.exception.ResourceNotFoundException;
import com.springboot.filemanipulation.model.FileInfo;
import com.springboot.filemanipulation.repository.FileStorageRepository;
import com.springboot.filemanipulation.service.FileStorageService;


@Service
public class FileStorageServiceImpl implements FileStorageService{
	@Autowired
	  private FileStorageRepository fileRepository;
	  
	  public FileInfo saveFile(MultipartFile file,String tags) {
		  String docname = file.getOriginalFilename();
		  if(tags.isEmpty()) tags="Default";
		  List<String> list = 
			  Stream.of(tags.split(","))
			  .collect(Collectors.toCollection(ArrayList<String>::new));
		  try {
			  FileInfo doc = new FileInfo(docname,file.getContentType(),list, file.getBytes());
			  return fileRepository.save(doc);
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
		  return null;
	  }
	  public Optional<FileInfo> getFile(Integer fileId) {
		  return Optional.ofNullable(fileRepository.findById(fileId)
			        .orElseThrow(() -> new ResourceNotFoundException("File not found for the given id : "+fileId)));
		  
	  }
	  public List<FileInfo> getFiles(){
		  return fileRepository.findAll();
	  }
	  
	  public List<FileInfo> getFilesbyTag(String tag){
		  return fileRepository.findAllByTag(tag);
		  //return docRepository.findByTagsContaining(tag);
	  }
}
