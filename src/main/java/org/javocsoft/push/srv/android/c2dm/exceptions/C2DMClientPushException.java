package org.javocsoft.push.srv.android.c2dm.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMClientPushException extends Exception{
	
	public C2DMClientPushException(){
		super();
	}
	
	public C2DMClientPushException(Throwable cause){
		super(cause);
	}

	public C2DMClientPushException(String msg){
		super(msg);
	}

	public C2DMClientPushException(String msg,Throwable cause){
		super(msg,cause);
	}
	
}