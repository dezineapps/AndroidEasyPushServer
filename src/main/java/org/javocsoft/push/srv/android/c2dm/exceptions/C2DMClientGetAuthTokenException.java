package org.javocsoft.push.srv.android.c2dm.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class C2DMClientGetAuthTokenException extends Exception{
	
	public C2DMClientGetAuthTokenException(){
		super();
	}
	
	public C2DMClientGetAuthTokenException(Throwable cause){
		super(cause);
	}

	public C2DMClientGetAuthTokenException(String msg){
		super(msg);
	}

	public C2DMClientGetAuthTokenException(String msg,Throwable cause){
		super(msg,cause);
	}
	
}