package parsers;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

import parsers.DocumentParser;
import enumarations.DocumentTypes;
import enumarations.MicrosoftDocTypes;

public class ReadDocType {
    protected static MicrosoftDocTypes microsoftDocType;
    protected static DocumentTypes docType;

    private static void  ChooseDocumentType(File file)
    {
        //used to get extension of the file
        String fileType = FilenameUtils.getExtension(file.getPath());
        
        //only microsoft types of documents
        switch(fileType)
        {
            case "doc":
                microsoftDocType = MicrosoftDocTypes.eDoc;
                docType = DocumentTypes.eMicrosoftDocumentsType;
                break;
                
            case "docx":
                microsoftDocType = MicrosoftDocTypes.eDocx;
                docType = DocumentTypes.eMicrosoftDocumentsType;
                break;
                
            case "xls":
                microsoftDocType = MicrosoftDocTypes.eXls;
                docType = DocumentTypes.eMicrosoftDocumentsType;
                break;
                
            case "xlsx":
                microsoftDocType = MicrosoftDocTypes.eXlsx;         
                docType = DocumentTypes.eMicrosoftDocumentsType;
                break;
                
            case "ppt":
                microsoftDocType = MicrosoftDocTypes.ePpt;
                docType = DocumentTypes.eMicrosoftDocumentsType;

                break;
                
            case "pptx":
                microsoftDocType = MicrosoftDocTypes.ePptx;
                docType = DocumentTypes.eMicrosoftDocumentsType;
                break;
                
            case "pdf":
                microsoftDocType = MicrosoftDocTypes.eNotSupportedMicrosoftFormat;
                docType = DocumentTypes.ePdfType;
                break;
                
             default:
                 microsoftDocType = MicrosoftDocTypes.eNotSupportedMicrosoftFormat;
                 docType = DocumentTypes.eNotSupportedType;

                 break;
        }
 
        if( docType == DocumentTypes.eNotSupportedType )
        {
            System.out.println("");
        }
    }
    
    protected MicrosoftDocTypes GetMicrosoftDocType()
    {
        return microsoftDocType;
    }   
    
    public String ParseDocuments(File file)
    {
        String str = "";
        
        DocumentParser docParser = new DocumentParser();
        
        ChooseDocumentType(file);
       
        switch(docType)
        {
            case eMicrosoftDocumentsType:
                
                //retrieve microsoft documents text i
                str = docParser.MicrosoftDocumentParser(file);
                
                break;
            
            case ePdfType:
                
                //retrieve pdf document text information
                str = docParser.PdfDocumentParser(file);

                break;
            
            case eTxtType:
                break;
            
            default :
                str = "Document type not supported!";
                break;

        }
        
        return str;
    }
}