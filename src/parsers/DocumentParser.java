package parsers;

import java.io.File;
import java.io.FileInputStream;

import enumarations.MicrosoftDocTypes;
import parsers.ReadDocType;

//used for Microsoft types of documents
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hslf.extractor.PowerPointExtractor; 
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor; 
import org.apache.poi.openxml4j.opc.OPCPackage;

//used for PDF documents
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class DocumentParser {
	
    
	protected String MicrosoftDocumentParser(File file)
	{
		String str = "";
		
		try {
		    
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			MicrosoftDocTypes mDocType = new ReadDocType().GetMicrosoftDocType();
		
			//only microsoft types of documents
			switch(mDocType)
			{
			    case eDoc:

			        // loading the .doc document
			        HWPFDocument doc = new HWPFDocument(fis);
			        
			        // instantiation  of WordExtractor class
    	            WordExtractor WordDoc = new WordExtractor(doc);
    	            
    	            // retrieving the text from the .doc document
    	            str = WordDoc.getText();
    	            
    	            // close the .doc document
    	            WordDoc.close(); 
    	            
    	            break;
    	            
			    case eDocx:

	                // loading the .docx document
			        XWPFDocument  docx = new XWPFDocument(OPCPackage.open(file));
			        
			        // instantiation  of XWPFWordExtractor class
			        XWPFWordExtractor WordDocx = new XWPFWordExtractor(docx);
			                    
                    // retrieving the text from the .docx document
			        str = WordDocx.getText();
			        
	                // close the .docx document
			        WordDocx.close();
			        
		            break;
		            
			    case eXls:

	                // loading the .xls document
			        HSSFWorkbook  xls = new HSSFWorkbook(fis);
			        
	                // instantiation  of ExcelExtractor class
		            ExcelExtractor ExcelDoc = new ExcelExtractor(xls);
		            
                    // retrieving the text from the .xls document
		            str = ExcelDoc.getText();
		            
		            // close the .xls document
		            ExcelDoc.close();
		            
		            break;
		            
			    case eXlsx:
			        
                    // loading the .xlsx document
    	            XSSFWorkbook  xlsx = new XSSFWorkbook(fis);
    	            
                    // instantiation  of XSSFExcelExtractor class
    	            XSSFExcelExtractor ExcelDocx = new XSSFExcelExtractor(xlsx);
    	            
                    // retrieving the text from the .xlsx document
    	            str = ExcelDocx.getText();
    	            
                    // close the .xlsx document
    	            ExcelDocx.close();
    	            
    	            break;
    	            
			    case ePpt:
			        
                    // loading the .ppt document
			        HSLFSlideShowImpl ppt = new HSLFSlideShowImpl(fis);
			        
                    // instantiation  of PowerPointExtractor class
			        PowerPointExtractor PowerPointPpt = new PowerPointExtractor(ppt);
			        
                    // retrieving the text from the .ppt document
			        str = PowerPointPpt.getText();
			        
                    // close the .ppt document
			        PowerPointPpt.close();
			        
			        break;
			        
			    case ePptx:
			        
                    // loading the .pptx document
			        XMLSlideShow pptx = new XMLSlideShow(fis);
			        
                    // instantiation  of XSLFPowerPointExtractor class
			        XSLFPowerPointExtractor PowerPointPptx = new XSLFPowerPointExtractor(pptx);
                    
                    // retrieving the text from the .pptx document
                    str = PowerPointPptx.getText();
                    
                    // close the .pptx document
                    PowerPointPptx.close();
                    
                    break;
                    
    	         default:
    	             break;
			}
			
		}catch ( Exception exception) {
			exception.printStackTrace();
		}
		
		//in case of exception
		return str;
	}
	
	public String PdfDocumentParser(File file)
	{
	    String str = "";
	    
	    try {

	        //loading of the document
	        PDDocument pdfDoc = PDDocument.load(file);
	        //instantiation  of PDFTextStripper class
	        PDFTextStripper pdfS = new PDFTextStripper();
	        
	        //Retrieving the text from the PDF Document
	        str = pdfS.getText(pdfDoc);
	        	                      
	        //closing of the pdf document
	        pdfDoc.close();
	        
	    }catch ( Exception exception) {
            exception.printStackTrace();
        }    
	    
	    return str;
	}
}