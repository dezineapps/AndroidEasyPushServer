package org.javocsoft.push.srv.android.c2dm.registration.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMDeviceRegistrationException extends Exception{
	
	public C2DMDeviceRegistrationException(){
		super();
	}
	
	public C2DMDeviceRegistrationException(Throwable cause){
		super(cause);
	}

	public C2DMDeviceRegistrationException(String msg){
		super(msg);
	}

	public C2DMDeviceRegistrationException(String msg,Throwable cause){
		super(msg,cause);
	}
	
}