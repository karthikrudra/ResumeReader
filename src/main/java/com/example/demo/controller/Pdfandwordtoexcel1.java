//package com.example.demo.controller;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@RestController
//public class Pdfandwordtoexcel1 {
//
//	@GetMapping("/getExcel")
//    public ResponseEntity<byte[]>	handleFileUpload(
//            @RequestParam("pdfFile") MultipartFile pdfFile ) {
//		
//    	byte[] bytes;
//    	
//	   try {
//		   
//		   File file = File.createTempFile("temp", null);
//		   ((MultipartFile) pdfFile).transferTo(file);
//		   
//		    
//           // Load PDF file
//           PDDocument document = PDDocument.load(file);
//
//           // Extract text from PDF
//           PDFTextStripper pdfStripper = new PDFTextStripper();
//           String pdfText = pdfStripper.getText(document);
//
////           System.err.println(pdfText.toLowerCase());
//           
//        // Regular expressions for extracting name, email id, and phone number
//           Pattern namePattern = Pattern.compile("name\\s*:\\s*(\\w+\\s+\\w+)");
//           Pattern fullNamePattern = Pattern.compile("full\\s*name\\s*:\\s*(\\w+\\s+\\w+)");
//          
//
//           Pattern mobileNoPattern = Pattern.compile("mobile\\s*no\\s*:\\s*(\\+?\\d+-?\\d+)");
//           Pattern mobileNoPatternWithoutHyphen = Pattern.compile("mobile\\s*no\\s*:\\s*(\\+?\\d+ ?\\d+)");
//           
//           Pattern phoneNoPattern = Pattern.compile("phone\\s*no\\s*:\\s*(\\+?\\d+-?\\d+)");
//           Pattern phoneNoPatternWithoutHyphen = Pattern.compile("phone\\s*no\\s*:\\s*(\\+?\\d+ ?\\d+)");
//
//           Pattern contactNoPattern = Pattern.compile("contact\\s*no\\s*:\\s*(\\+?\\d+-?\\d+)");
//           Pattern contactNoPatternWithoutHyphen = Pattern.compile("contact\\s*no\\s*:\\s*(\\+?\\d+ ?\\d+)");
//
//           Pattern mobilePattern = Pattern.compile("mobile\\s*:\\s*(\\+?\\d+-?\\d+)");
//           Pattern mobilePatternWithoutHyphen = Pattern.compile("mobile\\s*:\\s*(\\+?\\d+ ?\\d+)");
//
//           Pattern phonePattern = Pattern.compile("phone\\s*:\\s*(\\+?\\d+-?\\d+)");
//           Pattern phonePatternWithoutHyphen = Pattern.compile("phone\\s*:\\s*(\\+?\\d+ ?\\d+)");
//
//           Pattern contactPattern = Pattern.compile("contact\\s*:\\s*(\\+?\\d+-?\\d+)");
//           Pattern contactPatternWithoutHyphen = Pattern.compile("contact\\s*:\\s*(\\+?\\d+ ?\\d+)");
//
//           Pattern emailPattern = Pattern.compile("e[-]?mail\\s*:\\s*([\\w.-]+@[\\w.-]+\\.\\w+)");
//           Pattern emailIdPattern = Pattern.compile("email\\s*id\\s*:\\s*([\\w.-]+@[\\w.-]+\\.\\w+)");
//
//           
//           Matcher nameMatcher = namePattern.matcher(pdfText.toLowerCase());
//           Matcher fullNameMatcher = fullNamePattern.matcher(pdfText.toLowerCase());
//           
//           Matcher mobileNoMatcher = mobileNoPattern.matcher(pdfText.toLowerCase());
//           Matcher mobileNoPatternWithoutHyphenMatcher = mobileNoPatternWithoutHyphen.matcher(pdfText.toLowerCase());
//           Matcher mobileMatcher = mobilePattern.matcher(pdfText.toLowerCase());
//           Matcher mobilePatternWithoutHyphenMatcher = mobilePatternWithoutHyphen.matcher(pdfText.toLowerCase());
//           
//           Matcher phoneNoMatcher = phoneNoPattern.matcher(pdfText.toLowerCase());
//           Matcher phoneNoPatternWithoutIphenMatcher = phoneNoPatternWithoutHyphen.matcher(pdfText.toLowerCase());
//           Matcher phoneMatcher = phonePattern.matcher(pdfText.toLowerCase());
//           Matcher phonePatternWithoutHyphenMatcher = phonePatternWithoutHyphen.matcher(pdfText.toLowerCase());
//           
//           Matcher contactNoMatcher = contactNoPattern.matcher(pdfText.toLowerCase());
//           Matcher contactNoPatternWithoutIphenMatcher = contactNoPatternWithoutHyphen.matcher(pdfText.toLowerCase());
//           Matcher contactMatcher = contactPattern.matcher(pdfText.toLowerCase());
//           Matcher contactPatternWithoutHyphenMatcher = contactPatternWithoutHyphen.matcher(pdfText.toLowerCase());
//           
//           Matcher emailMatcher = emailPattern.matcher(pdfText.toLowerCase());
//           Matcher emailIdMatcher = emailIdPattern.matcher(pdfText.toLowerCase());
//
//           // Create Excel workbook and sheet
//           XSSFWorkbook workbook = new XSSFWorkbook();
//           XSSFSheet sheet = workbook.createSheet("Resume Data");
//           int rowNum = 0;
//
//           // Write headers
//           Row headerRow = sheet.createRow(rowNum++);
//           headerRow.createCell(0).setCellValue("Candidate_Name");
//           headerRow.createCell(1).setCellValue("Mobile");
//           headerRow.createCell(2).setCellValue("Email");
// 
//
//               Row row = sheet.createRow(rowNum++);
//               //for name
//               if(nameMatcher.find()) {
//            	   String name = nameMatcher.group(1);
//            	   row.createCell(0).setCellValue(name);  
//               }else if(fullNameMatcher.find()){
//            	   String fullName = fullNameMatcher.group(1);
//            	   row.createCell(0).setCellValue(fullName);
//               }
//              
//               //for contact number
//               if (mobileNoMatcher.find() && mobileNoMatcher.group(1).contains("-")) {
//            	    String mobileNo = mobileNoMatcher.group(1);
//            	    row.createCell(1).setCellValue(mobileNo);
//            	} else if (mobileNoPatternWithoutHyphenMatcher.find()) {
//            	    String mobileNo = mobileNoPatternWithoutHyphenMatcher.group(1);
//            	    row.createCell(1).setCellValue(mobileNo);
//            	} 
//            	else if(mobileMatcher.find() && mobileMatcher.group(1).contains("-") ){
//            		String mobileNo = mobileMatcher.group(1);
//            	    row.createCell(1).setCellValue(mobileNo);
//            	}
//            	else if(mobilePatternWithoutHyphenMatcher.find()) {
//            		String mobileNo = mobilePatternWithoutHyphenMatcher.group(1);
//            	    row.createCell(1).setCellValue(mobileNo);
//            	}
//               else if (phoneNoMatcher.find()&& phoneNoMatcher.group(1).contains("-")) {
//            	   String phoneNo = phoneNoMatcher.group(1);
//            	   row.createCell(1).setCellValue(phoneNo);
//			   }
//               else if (phoneNoPatternWithoutIphenMatcher.find()) {
//            	   String phoneNo = phoneNoPatternWithoutIphenMatcher.group(1);
//            	   row.createCell(1).setCellValue(phoneNo);
//			   }
//               else if (phoneMatcher.find()&& phoneMatcher.group(1).contains("-")) {
//            	   String phoneNo = phoneMatcher.group(1);
//            	   row.createCell(1).setCellValue(phoneNo);
//			   }
//               else if (phonePatternWithoutHyphenMatcher.find()) {
//            	   String phoneNo = phonePatternWithoutHyphenMatcher.group(1);
//            	   row.createCell(1).setCellValue(phoneNo);
//			   }
//             
//               else if (contactNoMatcher.find()&& contactNoMatcher.group(1).contains("-")) {
//            	   String contactNo = contactNoMatcher.group(1);
//            	   row.createCell(1).setCellValue(contactNo);
//			   }
//               else if (contactNoPatternWithoutIphenMatcher.find()) {
//            	   String contactNo = contactNoPatternWithoutIphenMatcher.group(1);
//            	   row.createCell(1).setCellValue(contactNo);
//			   }
//               else if (contactMatcher.find()&& contactMatcher.group(1).contains("-")){
//            	   String contactNo = contactNoMatcher.group(1);
//            	   row.createCell(1).setCellValue(contactNo);
//               }
//               else if (contactPatternWithoutHyphenMatcher.find()) {
//            	   String contactNo = contactPatternWithoutHyphenMatcher.group(1);
//            	   row.createCell(1).setCellValue(contactNo);
//			   }
//               
//               //for emailId
//               if(emailMatcher.find()) {
//            	   String email = emailMatcher.group(1);
//            	   row.createCell(2).setCellValue(email);
//               }
//               else if(emailIdMatcher.find()){
//            	   String emailId = emailIdMatcher.group(1);
//            	   row.createCell(2).setCellValue(emailId);
//               } 
//               
//               
//           // Write Excel data to file
//           FileOutputStream fileOut = new FileOutputStream("resume_data.xlsx");
//           workbook.write(fileOut);
//          
//           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//           workbook.write(outputStream);
//            bytes = outputStream.toByteArray(); 
//            
//            
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//            headers.setContentDispositionFormData("filename", "example.xlsx");
//            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//
//            // Close resources
//            fileOut.close();
//           document.close();
//           workbook.close();
//            
//            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
//
//       } catch (IOException e) {
//           e.printStackTrace();
//           
//       }
//	    
//	return null;
//   
//	}
// 
//}