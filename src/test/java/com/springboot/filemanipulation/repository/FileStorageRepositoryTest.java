package com.springboot.filemanipulation.repository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.mock.web.MockMultipartFile;

import com.springboot.filemanipulation.model.FileInfo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class FileStorageRepositoryTest {
	@Autowired
	   private TestEntityManager entityManager;

	   @Autowired
	   private FileStorageRepository fileStorageRepository;

	   @Test
	   public void testUploadFile() throws IOException {
		   String fileName = "sample-file-mock.txt";
	        MockMultipartFile sampleFile = new MockMultipartFile(
	                "uploaded-file",
	                fileName,
	                "text/plain",
	                "This is the file content".getBytes());
	        
	        FileInfo fileInfo = new FileInfo();
	        fileInfo.setDocName(fileName);
	        fileInfo.setData(sampleFile.getBytes());
	        fileInfo.setDocType(sampleFile.getContentType());
	        
	        FileInfo uploadedFile = fileStorageRepository.save(fileInfo);
	        FileInfo existsFile = entityManager.find(FileInfo.class, uploadedFile.getId());
	        
	        assertArrayEquals(existsFile.getData(),fileInfo.getData());
	   }
	   
	   @Test
	   public void testGetAllFiles() throws IOException {
	       //given
		   String fileName = "sample-file-mock.txt";
	        MockMultipartFile sampleFile = new MockMultipartFile(
	                "uploaded-file-1",
	                fileName,
	                "text/plain",
	                "This is the file content".getBytes());
	        
	        FileInfo fileInfo = new FileInfo();
	        fileInfo.setDocName(fileName);
	        fileInfo.setData(sampleFile.getBytes());
	        fileInfo.setDocType(sampleFile.getContentType());
	   
	       entityManager.persist(fileInfo);
	       entityManager.flush();
	       
	       String fileName2 = "sample-file-mock-2.txt";
	        MockMultipartFile sampleFile2 = new MockMultipartFile(
	                "uploaded-file-2",
	                fileName,
	                "text/plain",
	                "This is the second file content".getBytes());
	        
	        FileInfo fileInfo2 = new FileInfo();
	        fileInfo.setDocName(fileName2);
	        fileInfo.setData(sampleFile2.getBytes());
	        fileInfo.setDocType(sampleFile2.getContentType());
	   
	       
	       entityManager.persist(fileInfo2);
	       entityManager.flush();

	       //when
	       List<FileInfo> files = fileStorageRepository.findAll();

	       //then
	       assertEquals(files.size(),2);
	   }
	   
	   @Test
	   public void testGetByTags() throws IOException {
	       //given
		   String fileName = "sample-file-mock.txt";
	        MockMultipartFile sampleFile = new MockMultipartFile(
	                "uploaded-file",
	                fileName,
	                "text/plain",
	                "This is the file content".getBytes());
	        
	        FileInfo fileInfo = new FileInfo();
	        fileInfo.setDocName(fileName);
	        fileInfo.setData(sampleFile.getBytes());
	        fileInfo.setDocType(sampleFile.getContentType());
	        fileInfo.setTags(Stream.of("a,b,c".split(","))
	        		  .collect(Collectors.toCollection(ArrayList<String>::new)));
	       entityManager.persist(fileInfo);
	       entityManager.flush();
	       
	       String fileName1 = "sample-file-mock-1.txt";
	        MockMultipartFile sampleFile1 = new MockMultipartFile(
	                "uploaded-file-1",
	                fileName,
	                "text/plain",
	                "This is the file content".getBytes());
	        
	        FileInfo fileInfo1 = new FileInfo();
	        fileInfo.setDocName(fileName1);
	        fileInfo.setData(sampleFile1.getBytes());
	        fileInfo.setDocType(sampleFile1.getContentType());
	        fileInfo.setTags(Stream.of("test,test1".split(","))
	        		  .collect(Collectors.toCollection(ArrayList<String>::new)));
	       entityManager.persist(fileInfo1);
	       entityManager.flush();

	       //when
	       List<FileInfo> files = fileStorageRepository.findAllByTag("test");

	       //then
	       assertEquals(files.size(),1);
	   }

}
