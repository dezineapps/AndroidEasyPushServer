package org.javocsoft.push.srv.android.c2dm.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class C2DMEncryptCredentialsException extends C2DMException {
	
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_UNEXPECTED=300;
	
	
	
	public C2DMEncryptCredentialsException(String message){
		super(message);
	}
	
	public C2DMEncryptCredentialsException(String message, Throwable cause){
		super(message,cause);
	}
	
	public C2DMEncryptCredentialsException(int error_code){
		this.error_code=error_code;
		error=getErrorMessage();
	}
	
	public C2DMEncryptCredentialsException(int error_code, Throwable cause){
		super(cause);
		this.error_code=error_code;	
		error=getErrorMessage();
	}
	
}
