package com.searchengine.searchAlgorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.nio.file.Path;
import java.util.List;

import com.searchengine.model.SearchPhrase;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.commons.lang3.StringUtils;

import com.searchengine.enumarations.DocumentStatus;
import com.searchengine.parsers.ReadDocType;

public class Indexer {

    public Indexer() {
        // TODO Auto-generated constructor stub
    }
    
    private static final String pathOfSourceFiles = "/home/ggw/eclipse-workspace/BachelorArbeit/files"; //location of source files
    private static final String pathOfIndexedFiles = "/home/ggw/eclipse-workspace/BachelorArbeit/indexed"; //location of indexed files
    private IndexWriter iWriter;
        
    private void Index()
    {
        try
        {
            //open indexed files directory
            Path indexDirectoryPath = Paths.get(pathOfIndexedFiles);         
            FSDirectory dir = FSDirectory.open(indexDirectoryPath);
           
            //create new index write configuration
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iConfig = new IndexWriterConfig(analyzer);

            iWriter = new IndexWriter(dir, iConfig);
            
            //commit indexed files.
            iWriter.commit();
            
        }
        catch( Exception e)
        {
            System.out.println("Cannot get index writer.");   
        }
     
    }   
    
    private void closeIndex()
    {
        try
        {
            iWriter.close();
        }
        catch(Exception e)
        {
            System.out.println("Indexer can not be closed ! ");
        }
    }
    
    private void startIndexing(File file, DocumentStatus docStatus) throws FileNotFoundException, IOException, CorruptIndexException
    {
        String fileContent = null;
        
        try
        {
            //creating lucene document
            Document lucenedoc = new Document();
            
            //creating supported document types reader
            ReadDocType doc = new ReadDocType();
            
            //take file content from supported document types
            fileContent = doc.ParseDocuments(file);
            
            //add new fields in lucene indexing document
            lucenedoc.add(new StringField("filename",file.getName(),Field.Store.YES));
            lucenedoc.add(new StringField("filepath",file.getAbsolutePath(),Field.Store.YES));
            lucenedoc.add(new TextField("content", fileContent, Field.Store.YES));
            
            //check if lucenedocument is different than null and add it to index writer
            if( lucenedoc != null)
            {
                if(docStatus != DocumentStatus.eUpdateDocument)
                {
                    //add document if not existing.
                    iWriter.addDocument(lucenedoc);
                }
                else
                {
                    //update document if existing.
                    iWriter.updateDocument(new Term("filepath", file.getAbsolutePath()), lucenedoc);  
                }
            }
            
        }catch ( Exception e)
        {
            System.out.println("Error in indexing file " + file.getAbsolutePath());
        }
        
    }
    
    private int TotalNumberOfIndexedDocs()
    {
        try {
            
            Path indexDirectoryPath = Paths.get(pathOfIndexedFiles);

            IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDirectoryPath));
            
            return reader.maxDoc();
            
        } catch (Exception ex) {
            System.out.println("Sorry no index found");
        }
        
        return 0;
    }
    
    private void checkFileValidity() throws IOException, IndexNotFoundException
    {
        File dir = new File(pathOfSourceFiles); // suppose there are 100 files at max
        String[] fileExtensions =  new String[] {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt"};
        LinkedList<File> filesToIndex = (LinkedList<File>) FileUtils.listFiles(dir, fileExtensions, true);
        DocumentStatus docStatus;
        
        Path indexDirectoryPath = Paths.get(pathOfIndexedFiles);
        
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(indexDirectoryPath));
        
        for (File file : filesToIndex) 
        {

            try 
            {
                // Creates new Term using the path of the html file 
                Term indexTerm = new Term("filepath", file.getPath());      
                
                if( reader.docFreq(indexTerm) > 0 ) 
                {
                    docStatus = DocumentStatus.eUpdateDocument;
                }
                else {
                    docStatus = DocumentStatus.eCreateDocument;
                }
                
                //to check whenther the file is a readable file or not.
                if (!( file.isDirectory()
                     && !file.isHidden()
                     && file.exists()
                     && file.canRead()
                     && file.length() > 0.0
                     && file.isFile())) 
                {
                    startIndexing(file,docStatus);
                }
            } catch (Exception e) {
                System.out.println("Sorry cannot index " + file.getAbsolutePath());
            }
         
        }
    }
    
    public void mains() throws FileNotFoundException, CorruptIndexException, IOException {
            Index();
            checkFileValidity();
            closeIndex();
            System.out.println("Total Document Indexed : " + TotalNumberOfIndexedDocs());
    }

    public List<SearchPhrase> searchIndex(String instring) throws IOException  {
        int queryCounter = 0;
        
        try
        {
            Path indexDirectoryPath = Paths.get(pathOfIndexedFiles);
            
            IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDirectoryPath));
            
            System.out.println("Searching for ' " + instring + " '");
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer1 = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser("content", analyzer1);
            //QueryParser queryParserfilename = new QueryParser("fullpath", analyzer1);
            Query query = queryParser.parse(instring);
            QueryScorer queryScorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(queryScorer);
            TopDocs hits = searcher.search(query, 100);
            
            System.out.println("Total no of hits for content: " + hits.totalHits);
            List<SearchPhrase> result = new ArrayList<>();
            for (int i = 0; i < hits.scoreDocs.length; i++) {
                ScoreDoc numOfMacthes = hits.scoreDocs[i];
                Document doc = searcher.doc(numOfMacthes.doc);
                String fileName = doc.get("filename");
                String fileContent = doc.get("content");
                String fragment = highlighter.getBestFragment(analyzer1, "content", fileContent);
                queryCounter = StringUtils.countMatches(fragment, "</B>");
                SimpleDateFormat formatter = new SimpleDateFormat();
                String sDateAndTime = formatter.format(new File(doc.get("filepath")).lastModified());

                SearchPhrase searchPhrase = new SearchPhrase(fileName,numOfMacthes.score,fragment,queryCounter,sDateAndTime);
                result.add(searchPhrase);
            }
            return result;
        }catch(Exception e) {
            System.out.println("Problems searching");
        }
        return null;
    }
