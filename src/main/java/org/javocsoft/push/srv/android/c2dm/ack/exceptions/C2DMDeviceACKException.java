package org.javocsoft.push.srv.android.c2dm.ack.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 04/11/2011
 */
@SuppressWarnings("serial")
public class C2DMDeviceACKException extends Exception{
	
	public C2DMDeviceACKException(){
		super();
	}
	
	public C2DMDeviceACKException(Throwable cause){
		super(cause);
	}

	public C2DMDeviceACKException(String msg){
		super(msg);
	}

	public C2DMDeviceACKException(String msg,Throwable cause){
		super(msg,cause);
	}
	
}