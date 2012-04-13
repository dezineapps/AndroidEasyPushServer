package org.javocsoft.push.srv.android.c2dm.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class C2DMGetSrvTokenException extends C2DMException {
	
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_UNEXPECTED=200;
	/** The login request used a username or password that is not recognized. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_BadAuthentication=201;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_BadAuthentication="BadAuthentication";
	/** 
	 * The account email address has not been verified. The user will need to 
	 * access their Google account directly to resolve the issue before logging 
	 * in using a non-Google application. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_NotVerified=202;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_NotVerified="NotVerified";
	/** 
	 * The user has not agreed to terms. The user will need to access their 
	 * Google account directly to resolve the issue before logging in using a 
	 * non-Google application. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_TermsNotAgreed=203;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_TermsNotAgreed="TermsNotAgreed";
	/** 
	 * A CAPTCHA is required. (A response with this error code will also 
	 * contain an image URL and a CAPTCHA token.) */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_CaptchaRequired=204;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_CaptchaRequired="CaptchaRequired";
	/** 
	 * The error is unknown or unspecified; the request contained invalid 
	 * input or was malformed. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_Unknown=205;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_Unknown="Unknown";
	/** The user account has been deleted. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_AccountDeleted=206;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_AccountDeleted="AccountDeleted";
	/** The user account has been disabled. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_ServiceDisabled=207;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_ServiceDisabled="ServiceDisabled";
	/** The user's access to the specified service has been disabled. */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_ServiceUnavailable=208;
	public static final String C2DM_GET_SRV_TOKEN_ERROR_ServiceUnavailable="ServiceUnavailable";
	
	/** This error occurs when trying to read a saved token */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_READ_SAVED_TOKEN=209;
	/** This error occurs when trying to register with an invalid password or account */
	public static final int C2DM_GET_SRV_TOKEN_ERROR_CODE_WRONG_C2DM_ACCOUNT_DATA=403;
	
	
	
	
	public C2DMGetSrvTokenException(String message, String response){
		super(message);
		if(response!=null){
			checkErrorInAuthTokenRequest(response);
		}
	}
	
	public C2DMGetSrvTokenException(String message, String response, Throwable cause){
		super(message,cause);
		if(response!=null){
			checkErrorInAuthTokenRequest(response);
		}	
	}
	
	public C2DMGetSrvTokenException(int error_code){
		this.error_code=error_code;
		error=getErrorMessage();
	}
	
	public C2DMGetSrvTokenException(int error_code, String message){
		super(message);
		this.error_code=error_code;
		error=getErrorMessage();
	}
	
	public C2DMGetSrvTokenException(int error_code, Throwable cause){
		super(cause);
		this.error_code=error_code;	
		error=getErrorMessage();
	}
	
	
	
	private void checkErrorInAuthTokenRequest(String response){
		if(response.indexOf("Error=")!=-1){
			int error_start=(response.indexOf("Error=")+6);
			String error=response.substring(error_start);
			
			if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_AccountDeleted)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_AccountDeleted;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_BadAuthentication)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_BadAuthentication;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_CaptchaRequired)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_CaptchaRequired;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_NotVerified)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_NotVerified;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_ServiceDisabled)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_ServiceDisabled;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_ServiceUnavailable)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_ServiceUnavailable;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_TermsNotAgreed)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_TermsNotAgreed;
			}else if(error.equalsIgnoreCase(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_Unknown)){
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_Unknown;
			}else{
				error_code=C2DM_GET_SRV_TOKEN_ERROR_CODE_UNEXPECTED;
			}
			this.error=getErrorMessage();
		}
	}
}
