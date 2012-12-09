package org.xikilze.utils.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader extends BufferedReader {

    public static final String DEFAULT_FIELDSEPARATOR = ",";
    public static final String DEFAULT_TEXTSEPARATOR  = "\"";
    public static final int    DEFAULT_SKIPKINES      = 0;
    
    private String fieldSeparator  = DEFAULT_FIELDSEPARATOR;
	private String textSeparator   = DEFAULT_TEXTSEPARATOR;
	private int     skipLines      = DEFAULT_SKIPKINES;
    private boolean linesSkipped   = false;
    
    public String[] headers = null;
    
    public void loadProperties(String fieldSeparator, String textSeparator, int skipLines) {
    	this.fieldSeparator = fieldSeparator;
        this.textSeparator = textSeparator;
        this.skipLines     = skipLines;
    }
    
	public CSVReader(Reader arg0, int arg1) {
		super(arg0, arg1);
		loadProperties(fieldSeparator, textSeparator, skipLines);
	}

	public CSVReader(Reader arg0) {
	   this(arg0, DEFAULT_FIELDSEPARATOR);
    }

    public CSVReader(Reader reader, String separator) {
        this(reader, separator, DEFAULT_TEXTSEPARATOR);
    }

    public CSVReader(Reader reader, String separator, String quotechar) {
        this(reader, separator, quotechar, DEFAULT_SKIPKINES);
    }
    
    public CSVReader(Reader reader, String fieldSeparator, String textSeparator, int skipLines) {
        super(reader);
        loadProperties(fieldSeparator, textSeparator, skipLines);
    }
    
   
   /**
    * Retourne la prochaine ligne d'enregistrements.
    * Saute les lignes d'entêtes inutiles et charge l'entête si elle existe
    * 
    * @throws IOException
 * @throws CSVException 
    */
   public String[] readFields() throws IOException, CSVException {
   		if (!linesSkipped) {
           for (int i = 0; i < skipLines; i++) {
        	   if ( i == 0 )
        		   headers=getFields();
        	   else
        		   getFields();
           }
           linesSkipped = true;
       }

       return getFields();
   }
   
   private String[] getFields() throws IOException, CSVException {

      String line=readLine();
      if ( line == null ) return null;
      
      char txtSep = ( textSeparator.isEmpty() ? 0 : textSeparator.toCharArray()[0] );
      char fldSep = fieldSeparator.toCharArray()[0];
      
      StringBuffer sb = new StringBuffer();
      List<String> fields = new ArrayList<String>();
      boolean inText=false;
      boolean endText=false;
      boolean isTextSeparator=true;
      boolean internalSep=false;
      while ( line != null ) {
    	  char lastChar=0;
    	 
    	  for (int i = 0; i < line.length(); i++) {
    		  
              char c = line.charAt(i);
              char nextC = ( i+1<line.length() ? line.charAt(i+1) : 0);
                            
              // Marquage des début de séparation de champ
              if ( !inText ) {
            	  if ( c == fldSep) {
            		  if ( lastChar == fldSep || i==0) {
            			  fields.add("");
    	                  sb = new StringBuffer();
    	                  inText=false;
    	                  endText=false;
            		  }
            		  lastChar=c;
                  	  continue;
            	  }
            	  
                  isTextSeparator = (  txtSep != 0 && ( lastChar == fldSep || i==0) && c == txtSep );
                  internalSep=true;
                  inText=true;  
                  lastChar=c;
                  sb.append(c);
              }
              else { 
             
	              // Marquage des fin de textes avec délimiteurs
		          if ( isTextSeparator ) {
		        	   
		        	  if ( internalSep==false && c == txtSep )
		        	      internalSep=true;
		        	  else
		        	  if ( internalSep==true && c == txtSep )
			        	 internalSep=false;
	        	  
		        	  if ( ! internalSep && c == txtSep && ( nextC == fldSep || i == line.length()-1 ) ) {
		        		  endText=true;
		        		  sb.append(c);
		        	  }
		          }
	              
	              // Textes sans délimiteurs
		          else
		    	  if ( ! isTextSeparator && ( c == fldSep || i == line.length()-1 ) ) {
		    		  if ( i == line.length()-1 && c != fldSep )
		    			  sb.append(c);
		    		  
		    		  endText=true;
		          } 
		    	        
	              // Extraction du champ
	              if ( endText ) {
	            	  
	            	  // Reconstruction du texte
	            	  String field = sb.toString().trim();
	            	  
	            	  if ( ! textSeparator.isEmpty() ) {
		            	  if ( field.startsWith(textSeparator) && field.length()>1) {
		            		  field=field.substring(1,field.length()-1);
		            	  }
		            	  field=field.replaceAll(textSeparator+textSeparator, textSeparator);
	              	  }
	            	  fields.add(field);
	                  sb = new StringBuffer();
	                  inText=false;
	                  endText=false;
	                  
	                  
	    			  if (  i == line.length()-1 && c == fldSep ) {
            			  fields.add("");
    	                  sb = new StringBuffer();
    	                  inText=false;
    	                  endText=false;
            		  }
		          }
	              else  
		            sb.append(c);
	              lastChar=c;
              }
    	  }	  
          
    	  if ( !inText ) {
    		  break;
    	  } else
    		  if ( ! isTextSeparator )  {
    			  String field = sb.toString().trim();
    			  fields.add(field);
                  sb = new StringBuffer();
                  inText=false;
                  endText=false;
            	  break;
    		  }
    	 
    	  // Mutliligne
    	  sb.append("\r"); 
    	  line=readLine();
      }
           
      return (String[]) fields.toArray(new String[0]);
      
   }
}
