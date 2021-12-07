package com.springboot.filemanipulation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.ArrayList;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.springboot.filemanipulation.service.FileStorageService;

@SpringBootTest
@AutoConfigureMockMvc
public class FileManipulationControllerTest {
	@MockBean 
    private FileStorageService fileStorageService;
	
	@Autowired
	private MockMvc mockMvc;
	private InputStream is;
    
	@MockBean
    private FileManipulationController controller;
    
    @Autowired
    WebApplicationContext wContext;


    @Before(value = "")
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
                   .alwaysDo(MockMvcResultHandlers.print())
                   .build();
    }
    
	@Test
	public void testUploadFiles() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("files", "sql-changelog-alter-table.sql",
				"application/sql", this.getClass().getResourceAsStream("/sql-changelog-alter-table.sql"));

		MockMultipartFile file2 = new MockMultipartFile("files", "WishNet.txt", "text/plain",
				this.getClass().getResourceAsStream("/WishNet.txt"));

		MockMultipartFile file3 = new MockMultipartFile("files", "Static Data", "text/plain",
				"test data".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/1.0/files").file(file1).file(file2).file(file3).param("tags","test"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
    public void testUploadFile() throws Exception {
		String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "files",
                fileName,
                "text/plain",
                "This is the file content".getBytes());

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/api/1.0/files");

        mockMvc.perform(multipartRequest.file(sampleFile).param("tags","test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
	@Test
    public void test_handleFileUpload_NoFileProvided() throws Exception{
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/api/1.0/files");
        
        mockMvc.perform(multipartRequest.param("tags","aa"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
    @Test
    public void testDownloadFile() throws Exception {
        Mockito.when(fileStorageService.getFiles()).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/1.0/files").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
    }
}
