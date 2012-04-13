package org.javocsoft.push.srv.android.c2dm.exceptions;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class C2DMException extends Exception{
	
	private static Properties excCatalog=null;
	
	public int error_code;
	public String error=null;
	
	public static final int ERROR_UNEXPECTED=666;	
	
	
	
	public C2DMException(){
		super();
	}
	
	public C2DMException(Throwable cause){
		super(cause);
	}

	public C2DMException(String msg){
		super(msg);
	}

	public C2DMException(String msg,Throwable cause){
		super(msg,cause);
	}
	
	
	protected String getErrorMessage(){
		String res=null;
		
		//Loads the message catalog
		if(excCatalog==null){
			excCatalog=getExceptionsMsgcatalog();
		}
		
		if(excCatalog==null){
			res="__ERROR_CATALOG__";
		}else{
			res=excCatalog.getProperty("exception."+error_code);
		}
		
		return res;
	}
	
	
	private final String exceptionsCatalogFile="exceptions.properties";
	private Properties getExceptionsMsgcatalog(){
		Properties exceptionsCatalog=null;
		
		try{
			InputStream in=this.getClass().getResourceAsStream(exceptionsCatalogFile);
			exceptionsCatalog=new Properties();
			exceptionsCatalog.load(in);
		}catch(Exception e){
			System.out.println("Error accesing the exception messages catalog file: "+e.getMessage());
			exceptionsCatalog=null;
		}
		
		return exceptionsCatalog;
	}
}