package com.springboot.filemanipulation.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.filemanipulation.exception.BadRequestException;
import com.springboot.filemanipulation.model.FileInfo;
import com.springboot.filemanipulation.response.ResponseModel;
import com.springboot.filemanipulation.service.FileStorageService;

@RestController
@RequestMapping("/api/1.0/files")
public class FileManipulationController {
	@Autowired 
	private FileStorageService fileStorageService;
	
	@GetMapping
	public ResponseEntity<Object> getAll(Model model) {
		List<FileInfo> docs = fileStorageService.getFiles();
		return ResponseModel.generateResponse("List of files", HttpStatus.OK, docs);
	}
	
	@PostMapping
	public ResponseEntity<Object> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("tags") String tags) {
		if(Arrays.asList(files)
		        .stream()
		        .anyMatch(e -> (e.getOriginalFilename() == null || e.getOriginalFilename().isEmpty()))){
			throw new BadRequestException("One or more file attachments are missing, Please attach the same.");
		}else {
			List<FileInfo> uploadedFiles = new ArrayList<FileInfo>();
			Arrays.asList(files).stream().forEach(file -> {
				uploadedFiles.add(fileStorageService.saveFile(file,tags));
		      });
			
			return ResponseModel.generateResponse("Uploaded Files", HttpStatus.OK, uploadedFiles);
			//return ResponseEntity.status(HttpStatus.OK).body(uploadedFiles);
		}
			
	}
	@GetMapping("/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
		
		FileInfo doc = fileStorageService.getFile(fileId).get();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
				.body(new ByteArrayResource(doc.getData()));
	}
	
	@GetMapping("tags/{tag}")
	public ResponseEntity<Object> get(Model model ,@PathVariable String tag) {
		
			List<FileInfo> tagdocs = fileStorageService.getFilesbyTag(tag);
			return ResponseModel.generateResponse("Files with respective tags", HttpStatus.OK, tagdocs);
			//return ResponseEntity.status(HttpStatus.OK).body(tagdocs);
		
	}

}
