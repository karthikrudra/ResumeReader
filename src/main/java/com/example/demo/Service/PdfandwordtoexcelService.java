package com.example.demo.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

@Service
public class PdfandwordtoexcelService {

	public byte[] generateExcelByPdf(List<MultipartFile> pdfFiles) {
		byte[] bytes;

		try {
			// Create Excel workbook and sheet
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			int rowNum = 0;

			// Write headers
			Row headerRow = sheet.createRow(rowNum++);
			headerRow.createCell(0).setCellValue("Candidate_Name");
			headerRow.createCell(1).setCellValue("Mobile");
			headerRow.createCell(2).setCellValue("Email");
			headerRow.createCell(3).setCellValue("Candidate_NameFromEmail");
			headerRow.createCell(4).setCellValue("Candidate_NameFromFileName");

			
			//to store unique mailIds for stopping the duplicate resumes to process 
			List<String> uniqueMailIds = new ArrayList<>();
			for (MultipartFile pdfFile : pdfFiles) {
				PDDocument document = null;
				if (pdfFile.getContentType().toString()
						.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {

					document = convertToPDF(pdfFile);
				} else {
					document = PDDocument.load(pdfFile.getInputStream());
				}
				   

				// Extract text from PDF
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String pdfText = pdfStripper.getText(document);

				// Regular expressions for extracting name, email id, and phone number

				// for name , full name :
				Pattern namePattern = Pattern.compile("name\\s*:?\\s*(\\w+\\s+\\w+)");
				Pattern fullNamePattern = Pattern.compile("full\\s*name\\s*:\\s*(\\w+\\s+\\w+)");
//				Pattern namePattern = Pattern.compile("^(?!.*(father|mother)(?:'s| ))(?:name\\s*:?\\s+)?(\\w+(?:\\s+\\w+))");


				
//				Pattern nameFromEmailpattern = Pattern.compile("^[@\\d]*(\\b[a-zA-Z]+).*");
//				Pattern nameFromEmailpattern = Pattern.compile(".*(?:\\((?:\\d+|\\d+-\\d+)\\)|\\d+)(?:\\s*-\\s*(?:\\((?:\\d+|\\d+-\\d+)\\)|\\d+))*");
				
//				Pattern mobileNoPattern = Pattern.compile("mobile\\s*no\\s*:\\s*(\\+?\\d+-?\\d+)");
//				Pattern mobileNoPatternWithoutHyphen = Pattern.compile("mobile\\s*no\\s*:\\s*(\\+?\\d+ ?\\d+)");
//				Pattern mobilePattern = Pattern.compile("mobile\\s*:\\s*(\\+?\\d+-?\\d+)");
//				Pattern mobilePatternWithoutHyphen = Pattern.compile("mobile\\s*:\\s*(\\+?\\d+ ?\\d+)");
//				
////					Pattern phoneNoPattern = Pattern.compile("phone\\s*no\\s*:\\s*(\\+?\\d+-?\\d+)");
//				Pattern phoneNoPattern = Pattern.compile("phone\\s*no\\.?\\s*:?\\s*(\\+?\\d+-?\\d+)");
//				Pattern phoneNoPatternWithoutHyphen = Pattern.compile("phone\\s*no\\s*:\\s*(\\+?\\d+ ?\\d+)");
//				Pattern phonePattern = Pattern.compile("phone\\s*:\\s*(\\+?\\d+-?\\d+)");
//				Pattern phonePatternWithoutHyphen = Pattern.compile("phone\\s*:\\s*(\\+?\\d+ ?\\d+)");
//				
//				Pattern contactNoPattern = Pattern.compile("contact\\s*no\\s*:\\s*(\\+?\\d+-?\\d+)");
//				Pattern contactNoPatternWithoutHyphen = Pattern.compile("contact\\s*no\\s*:\\s*(\\+?\\d+ ?\\d+)");
//				Pattern contactPattern = Pattern.compile("contact\\s*:\\s*(\\+?\\d+-?\\d+)");
//				Pattern contactPatternWithoutHyphen = Pattern.compile("contact\\s*:\\s*(\\+?\\d+ ?\\d+)");
				
				Pattern continuousNumberPattern = Pattern.compile("\\b\\d{10,15}\\b");
				Pattern continuousNumberPattern1 = Pattern.compile("\\d{10}");
				Pattern continuousNumberPattern2 = Pattern.compile("\\b[\\d()+\\s-]{10,20}\\b");


//				Pattern mobileNumberWithBraces =  Pattern.compile("((?<!\\+1-)(\\(\\d{1,9}\\)|\\d{1,9}))((\\s?\\(\\d{1,9}\\))?[-\\s]?\\d{1,9}){2,3}");


				Pattern emailPattern = Pattern.compile("e[-]?mail\\s*:\\s*([\\w.-]+@[\\w.-]+\\.\\w+)");
				Pattern emailIdPattern = Pattern.compile("email\\s*id\\s*:\\s*([\\w.-]+@[\\w.-]+\\.\\w+)");
				Pattern havingSymbolPattern = Pattern.compile("\\b\\S+@\\S+\\b");
				 
				//Matching the patterns 
				Matcher nameMatcher = namePattern.matcher(pdfText.toLowerCase());
				Matcher fullNameMatcher = fullNamePattern.matcher(pdfText.toLowerCase());

//				Matcher mobileNoMatcher = mobileNoPattern.matcher(pdfText.toLowerCase());
//				Matcher mobileNoPatternWithoutHyphenMatcher = mobileNoPatternWithoutHyphen
//						.matcher(pdfText.toLowerCase());
//				Matcher mobileMatcher = mobilePattern.matcher(pdfText.toLowerCase());
//				Matcher mobilePatternWithoutHyphenMatcher = mobilePatternWithoutHyphen.matcher(pdfText.toLowerCase());
//
//				Matcher phoneNoMatcher = phoneNoPattern.matcher(pdfText.toLowerCase());
//				Matcher phoneNoPatternWithoutIphenMatcher = phoneNoPatternWithoutHyphen.matcher(pdfText.toLowerCase());
//				Matcher phoneMatcher = phonePattern.matcher(pdfText.toLowerCase());
//				Matcher phonePatternWithoutHyphenMatcher = phonePatternWithoutHyphen.matcher(pdfText.toLowerCase());
//
//				Matcher contactNoMatcher = contactNoPattern.matcher(pdfText.toLowerCase());
//				Matcher contactNoPatternWithoutIphenMatcher = contactNoPatternWithoutHyphen.matcher(pdfText.toLowerCase());
//				Matcher contactMatcher = contactPattern.matcher(pdfText.toLowerCase());
//				Matcher contactPatternWithoutHyphenMatcher = contactPatternWithoutHyphen.matcher(pdfText.toLowerCase());
				
				Matcher continuousNumberPatternMatcher = continuousNumberPattern.matcher(pdfText.toLowerCase());
				Matcher continuousNumberPatternMatcher1 = continuousNumberPattern1.matcher(pdfText.toLowerCase());
				Matcher continuousNumberPatternMatcher2 = continuousNumberPattern2.matcher(pdfText.toLowerCase());
//				Matcher mobileNumberWithBracesMatcher = mobileNumberWithBraces.matcher(pdfText.toLowerCase());

				Matcher emailMatcher = emailPattern.matcher(pdfText.toLowerCase());
				Matcher emailIdMatcher = emailIdPattern.matcher(pdfText.toLowerCase());
				Matcher havingSymbolMatcher = havingSymbolPattern.matcher(pdfText.toLowerCase());
				
				// to create the new row
				Row row = null;
				
				// for emailId
				if (emailMatcher.find()) { 
					String emailId = emailMatcher.group(1);
				
					//checking whether the resume already processed or not through mailId
					if(uniqueMailIds.contains(emailId)) {
						continue;
					}else {
						// to create the new row
						 row = sheet.createRow(rowNum++);
						uniqueMailIds.add(emailId);
					}
					row.createCell(2).setCellValue(emailId);
					
//					Matcher nameFromEmailmatcher = nameFromEmailpattern.matcher(email);
					String nameEmail = exctractCandidateName(emailId);
					row.createCell(3).setCellValue(nameEmail);
				} else if (emailIdMatcher.find()) {
					String emailId = emailIdMatcher.group(1);
				
					//checking whether the resume already processed or not through mailId
					if(uniqueMailIds.contains(emailId)) {
						continue;
					}else {
						 	// to create the new row
						 row = sheet.createRow(rowNum++);
						uniqueMailIds.add(emailId);
					}
					row.createCell(2).setCellValue(emailId);
					
//					Matcher nameFromEmailmatcher = nameFromEmailpattern.matcher(emailId);
					String nameEmail = exctractCandidateName(emailId);
					row.createCell(3).setCellValue(nameEmail);
				} else if(havingSymbolMatcher.find()){
					String emailId = havingSymbolMatcher.group();

					//checking whether the resume already processed or not through mailId
					if(uniqueMailIds.contains(emailId)) {
						continue;
					}else {
							// to create the new row
						 row = sheet.createRow(rowNum++);
						uniqueMailIds.add(emailId);
					} 
					
					row.createCell(2).setCellValue(emailId);
					
					String nameEmail = exctractCandidateName(emailId);
					row.createCell(3).setCellValue(nameEmail);
				}
				if(row == null) {
					continue;
				}
				
				
				// for name
				if (nameMatcher.find()) {
					String name = nameMatcher.group(1);
					row.createCell(0).setCellValue(name);
				} else if (fullNameMatcher.find()) {
					String fullName = fullNameMatcher.group(1);
					row.createCell(0).setCellValue(fullName);
				} 
				
					if(pdfFile.getOriginalFilename().contains("/")) {
						String fileName = pdfFile.getOriginalFilename()
								.substring(pdfFile.getOriginalFilename().indexOf('/')+1);
						row.createCell(4).setCellValue
						(fileName.substring(0,fileName.indexOf('.')));
					}
					else if(pdfFile.getOriginalFilename().contains("Naukri_")) {
				    	String fileName =	pdfFile.getOriginalFilename().substring(pdfFile.getOriginalFilename().indexOf('_')+1, pdfFile.getOriginalFilename().indexOf('['));
				    	row.createCell(4).setCellValue( fileName);
					}
					else if(pdfFile.getOriginalFilename().contains(".")){
						String fileName = pdfFile.getOriginalFilename().substring(0,pdfFile.getOriginalFilename().indexOf('.'));
						row.createCell(4).setCellValue(fileName);
					}
					
//				}

				String matchedNumber = mobileNumberPatternWithBraces(pdfText); 
				// for contact number
				  if (continuousNumberPatternMatcher.find()) {
					String continuousNumber = continuousNumberPatternMatcher.group();
					row.createCell(1).setCellValue(continuousNumber);
				}else if (continuousNumberPatternMatcher1.find()) {
					String continuousNumber = continuousNumberPatternMatcher1.group();
					row.createCell(1).setCellValue(continuousNumber);
				}else if (continuousNumberPatternMatcher2.find()) {
					String continuousNumber = continuousNumberPatternMatcher2.group();
					row.createCell(1).setCellValue(continuousNumber);
				}else if (matchedNumber != null) {
					String continuousNumber = matchedNumber;
					row.createCell(1).setCellValue(continuousNumber);
				} 
//				 else if (mobileNoMatcher.find() && mobileNoMatcher.group(1).contains("-")) {
//					String mobileNo = mobileNoMatcher.group(1);  
//					row.createCell(1).setCellValue(mobileNo);
//				} else if (mobileNoPatternWithoutHyphenMatcher.find()) {
//					String mobileNo = mobileNoPatternWithoutHyphenMatcher.group(1);
//					row.createCell(1).setCellValue(mobileNo); 
//				} else if (mobileMatcher.find() && mobileMatcher.group(1).contains("-")) {
//					String mobileNo = mobileMatcher.group(1);
//					row.createCell(1).setCellValue(mobileNo);
//				} else if (mobilePatternWithoutHyphenMatcher.find()) {
//					String mobileNo = mobilePatternWithoutHyphenMatcher.group(1);
//					row.createCell(1).setCellValue(mobileNo);
//				} else if (phoneNoMatcher.find() && phoneNoMatcher.group(1).contains("-")) {
//					String phoneNo = phoneNoMatcher.group(1);
//					row.createCell(1).setCellValue(phoneNo);
//				} else if (phoneNoPatternWithoutIphenMatcher.find()) {
//					String phoneNo = phoneNoPatternWithoutIphenMatcher.group(1);
//					row.createCell(1).setCellValue(phoneNo);
//				} else if (phoneMatcher.find() && phoneMatcher.group(1).contains("-")) {
//					String phoneNo = phoneMatcher.group(1);
//					row.createCell(1).setCellValue(phoneNo);
//				} else if (phonePatternWithoutHyphenMatcher.find()) {
//					String phoneNo = phonePatternWithoutHyphenMatcher.group(1);
//					row.createCell(1).setCellValue(phoneNo);
//				} else if (contactNoMatcher.find() && contactNoMatcher.group(1).contains("-")) {
//					String contactNo = contactNoMatcher.group(1);
//					row.createCell(1).setCellValue(contactNo);
//				} else if (contactNoPatternWithoutIphenMatcher.find()) {
//					String contactNo = contactNoPatternWithoutIphenMatcher.group(1);
//					row.createCell(1).setCellValue(contactNo);
//				} else if (contactMatcher.find() && contactMatcher.group(1).contains("-")) {
//					String contactNo = contactMatcher.group(1);
//					row.createCell(1).setCellValue(contactNo);
//				} else if (contactPatternWithoutHyphenMatcher.find()) {
//					String contactNo = contactPatternWithoutHyphenMatcher.group(1);
//					row.createCell(1).setCellValue(contactNo);
//				}  
				document.close();
				
				 
			}
		        
//		        // Try-with-resources to automatically close the FileOutputStream
//			  String filePath = "C:\\Users\\KARTHIK RUDRA\\Downloads\\CandidatesInfo.xlsx";
//		        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
//		            // Write the workbook content to the output stream
//		            workbook.write(outputStream); 
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
			
			
			// Write Excel data to file
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			bytes = outputStream.toByteArray();

			// Close resources
			workbook.close();

			return bytes;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public PDDocument convertToPDF(MultipartFile wordFile) {
		try {
			// Convert multipart file to XWPFDocument
			InputStream inputStream = wordFile.getInputStream();
			XWPFDocument docxDocument = new XWPFDocument(inputStream);

			// Create PDF document
			PDDocument pdfDocument = new PDDocument();

			// Iterate over paragraphs and add to PDF document
			for (XWPFParagraph paragraph : docxDocument.getParagraphs()) {
				String text = paragraph.getText().replaceAll("\\p{C}", ""); // Remove control characters
				if (!text.isEmpty()) {
					pdfDocument.addPage(new PDPage());
					try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument,
							pdfDocument.getPage(pdfDocument.getNumberOfPages() - 1))) {
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA, 12); // Using default font
						contentStream.newLineAtOffset(100, 700);
						contentStream.showText(text);
						contentStream.endText();
					}
				}
			}

			return pdfDocument;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 public String exctractCandidateName(String email) {
	        int atIndex = email.indexOf('@'); // Find the index of '@' symbol
	        if (atIndex != -1) {
	            String username = email.substring(0, atIndex); // Extract the username part before '@'
	            int firstDigitIndex = -1;
	            // Find the index of the first digit in the username
	            for (int i = 0; i < username.length(); i++) {
	                if (Character.isDigit(username.charAt(i))) {
	                    firstDigitIndex = i;
	                    break;
	                }
	            }
	            if (firstDigitIndex != -1) {
	                // If a digit is found in the username, remove the numeric part
	                return username.substring(0, firstDigitIndex);
	            } else {
	                // If no digit is found, return the username as it is
	                return username;
	            }
	        }
	        return null; // Return null if no '@' symbol is found
	    }
	 
	 /////////////////////////////////

	     public byte[] mergeExcelFiles(List<MultipartFile> excelFiles) throws IOException, InvalidFormatException {
	     
	    	 // Create a Workbook object for each Excel sheet.
	         List<Workbook> workbooks = new ArrayList<>();
	         for (MultipartFile excelFile : excelFiles) {
	             workbooks.add(new XSSFWorkbook(excelFile.getInputStream()));
	         }

	         // Create a new Workbook object to store the merged data.
	         Workbook mergedWorkbook = new XSSFWorkbook();
	         Sheet mergedWorkbookSheet2 = mergedWorkbook.createSheet();
//	     	Row headerRow = mergedWorkbookSheet2.createRow(0);
//				headerRow.createCell(0).setCellValue("Candidate_Name");
//				headerRow.createCell(1).setCellValue("Mobile");
//				headerRow.createCell(2).setCellValue("Email");
//				headerRow.createCell(3).setCellValue("NameFromEmail");
	         
				//to create rows in mergedWorkbookSheet2
				int rowNum = 0;

				// for removing the header if already exists.
				int removeHeaders = 0;
				
	         // Iterate over the sheets in each workbook and copy the data to the merged workbook.
	         for (Workbook workbook : workbooks) {
	             for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	                 Sheet sheet1 = workbook.getSheetAt(i);
	                 
	                 // Check for existing sheet in the merged workbook
	                 boolean sheetExists = false;
	                 for (int j = 0; j < mergedWorkbook.getNumberOfSheets(); j++) {
	                     if (mergedWorkbook.getSheetAt(i).equals("mearged_excel")) {
	                         sheetExists = true;
	                         break;
	                     }
	                 }
	                 
	                 if (sheetExists) {
	                     // Handle duplicate sheet name (generate unique name)
	                    mergedWorkbookSheet2 =  mergedWorkbook.createSheet("mearged_excel");
	                 }
	 				
	                 // Copy data from the source sheet to the new sheet in the merged workbook
	                 for (int j = 0; j <= sheet1.getLastRowNum(); j++) {
	                	 
	                	 // for removing the header if already exists.
	                	 if(removeHeaders > 0) {
	                		 if(j == 0) {
	                		 continue;
	                		 }
	    	        	 }
	                	 removeHeaders = 1;
	                	 
	                     Row row1 = sheet1.getRow(j);
	                     Row row2 = mergedWorkbookSheet2.createRow(++rowNum);
	                     
	                     if (row1 != null) {
	                         for (int k = 0; k <= row1.getLastCellNum(); k++) {
	                             Cell cell1 = row1.getCell(k);
	                             Cell cell2 = row2.createCell(k);

	                             // Retrieve cell value based on type
	                             if (cell1 != null) {
	                                 CellType cellType = cell1.getCellType();
	                                 switch (cellType) {
	                                     case NUMERIC:
	                                         if (DateUtil.isCellDateFormatted(cell1)) {
	                                             cell2.setCellValue(cell1.getDateCellValue());
	                                         } else {
	                                             cell2.setCellValue(cell1.getNumericCellValue());
	                                         }
	                                         break;
	                                     case STRING:
	                                         cell2.setCellValue(cell1.getStringCellValue());
	                                         break;
	                                     case BOOLEAN:
	                                         cell2.setCellValue(cell1.getBooleanCellValue());
	                                         break;
	                                     case FORMULA:
	                                         cell2.setCellValue(cell1.getCellFormula());
	                                         break;
	                                     default:
	                                         // Handle other cell types or empty cells as needed
	                                         break;
	                                 }
	                             }
	                         }
	                     }
	                 }
	             }
	         }

	         // Save the merged workbook to a byte array.
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         mergedWorkbook.write(baos);
	         byte[] mergedExcelBytes = baos.toByteArray();

	         // Close the workbooks.
	         for (Workbook workbook : workbooks) {
	             workbook.close();
	         }
	         mergedWorkbook.close();

	         return mergedExcelBytes;
	     }
	     
	     
	     public String mobileNumberPatternWithBraces(String pdfText) {
			
	    	 
	    	         List<Pattern> patterns = new ArrayList<>();
	    	         // Pattern for +1-(123) (123) 4567
	    	         patterns.add(Pattern.compile("\\+1-\\(\\d{3}\\) \\(\\d{3}\\) \\d{4}"));

	    	         // Pattern for +1-(123) (123) (4567)
	    	         patterns.add(Pattern.compile("\\+1-\\(\\d{3}\\) \\(\\d{3}\\) \\(\\d{4}\\)"));
	    	         
	    	         // Pattern for +1 (123)-123-4567
	    	         patterns.add(Pattern.compile("\\+?1?\\s?\\(\\d{3}\\)-\\d{3}-\\d{4}"));

	    	         // Pattern for +1 (123)-(123)-4567
	    	         patterns.add(Pattern.compile("\\+?1?\\s?\\(\\d{3}\\)-\\(\\d{3}\\)-\\d{4}"));

	    	         // Pattern for +1 (123)-(123)-(4567)
	    	         patterns.add(Pattern.compile("\\+?1?\\s?\\(\\d{3}\\)-\\(\\d{3}\\)-\\(\\d{4}\\)"));

	    	         // Pattern for +1-(555)-123-4567
	    	         patterns.add(Pattern.compile("\\+?1?-\\(\\d{3}\\)-\\d{3}-\\d{4}"));

	    	         // Pattern for +1-(123)-(123)-4567
	    	         patterns.add(Pattern.compile("\\+?1?-\\(\\d{3}\\)-\\(\\d{3}\\)-\\d{4}"));

	    	         // Pattern for +1-(123)-(123)-(4567)
	    	         patterns.add(Pattern.compile("\\+?1?-\\(\\d{3}\\)-\\(\\d{3}\\)-\\(\\d{4}\\)"));

	    	         // Pattern for +1 (123) 123 4567
	    	         patterns.add(Pattern.compile("\\+?1?\\s?\\(\\d{3}\\)\\s?\\d{3}\\s?\\d{4}"));

	    	         // Pattern for +1-(123) (123) 4567
	    	         patterns.add(Pattern.compile("\\+1-\\(\\d{3}\\) \\(\\d{3}\\) \\d{4}"));

	    	         // Pattern for +1-(123) (123) (4567)
	    	         patterns.add(Pattern.compile("\\+1-\\(\\d{3}\\) \\(\\d{3}\\) \\(\\d{4}\\)"));
	    	         
	    	         // Pattern for (555) 123 4567
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)\\s\\d{3}\\s\\d{4}"));

	    	         // Pattern for (555) (123) 4567
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)\\s\\(\\d{3}\\)\\s\\d{4}"));

	    	         // Pattern for (555) (123) (4567)
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)\\s\\(\\d{3}\\)\\s\\(\\d{4}\\)"));

	    	         // Pattern for (555)-123-4567
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)-\\d{3}-\\d{4}"));

	    	         // Pattern for (555)-(123)-4567
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)-\\(\\d{3}\\)-\\d{4}"));

	    	         // Pattern for (555)-(123)-(4567)
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)-\\(\\d{3}\\)-\\(\\d{4}\\)"));

	    	        


	    	         // Pattern for 123-456-7890
	    	         patterns.add(Pattern.compile("\\d{3}-\\d{3}-\\d{4}"));

	    	         // Pattern for +1 123-456-7890
	    	         patterns.add(Pattern.compile("\\+1\\s\\d{3}-\\d{3}-\\d{4}"));

	    	         // Pattern for 123 456 7890
	    	         patterns.add(Pattern.compile("\\d{3}\\s\\d{3}\\s\\d{4}"));

	    	         // Pattern for +1 123 4567 890
	    	         patterns.add(Pattern.compile("\\+1\\s\\d{3}\\s\\d{4}\\s\\d{3}"));

	    	         // Pattern for (555) 1234567
	    	         patterns.add(Pattern.compile("\\(\\d{3}\\)\\s\\d{7}"));

	    	         // Pattern for 1234567 (967)
	    	         patterns.add(Pattern.compile("\\d{7}\\s\\(\\d{3}\\)"));



	    	          for(Pattern pattern : patterns) {
	    	        	  Matcher mobileNumberWithBracesMatcher = pattern.matcher(pdfText.toLowerCase());
	    	        	  if(mobileNumberWithBracesMatcher.find()) {
	    	        		  return  mobileNumberWithBracesMatcher.group();
	    	        	  }
				 
	    	          }
					return null;
		}
	         
}
