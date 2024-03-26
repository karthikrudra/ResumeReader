package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Service.PdfandwordtoexcelService;

@RestController
public class PdfandwordtoexcelController {

	@Autowired
	private PdfandwordtoexcelService pdfandwordtoexcelService; 
	
	@PostMapping("/getExcelByPdf")
	public ResponseEntity<byte[]> getExcelByPdf(
			@RequestParam("pdfWordFile") List<MultipartFile> pdfFiles) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("filename", "CandidateDetails.xlsx");
		   return new ResponseEntity<>(pdfandwordtoexcelService.generateExcelByPdf(pdfFiles), headers, HttpStatus.OK);
		
			}
	
	@PostMapping("/meargeExcelFiles")
	private ResponseEntity<byte[]> meargeExcelFiles(
			@RequestParam("excelFiles") List<MultipartFile> excelFiles) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("filename", "CandidateDetails.xlsx");
		   try {
			return new ResponseEntity<>(pdfandwordtoexcelService.mergeExcelFiles(excelFiles), headers, HttpStatus.OK);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
			}

}