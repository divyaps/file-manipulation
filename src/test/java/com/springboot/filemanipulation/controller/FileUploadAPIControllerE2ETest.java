package com.springboot.filemanipulation.controller;

	import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

	@SpringBootTest
	@AutoConfigureMockMvc
	@ActiveProfiles("unit-test")
	public class FileUploadAPIControllerE2ETest {
	    @Autowired
	    MockMvc mockMvc;

	    @Test
	    public void test_handleFileUpload() throws Exception {
	        String fileName = "sampleFile.txt";
	        MockMultipartFile sampleFile = new MockMultipartFile("files",
	                fileName, "text/plain",
	                "This is the file content".getBytes());

	        MockMultipartHttpServletRequestBuilder multipartRequest =
	                MockMvcRequestBuilders.multipart("/api/1.0/files");

	        mockMvc.perform(multipartRequest.file(sampleFile).param("tags", "test,test1"))
	                .andExpect(status().isOk());

	        

	    }
	
}
