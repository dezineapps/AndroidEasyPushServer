package org.javocsoft.push.srv.android.c2dm.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class C2DMSendMessageException extends C2DMException {
	
	public static final int C2DM_MSG_SEND_ERROR_CODE_UNEXPECTED=100;
	/**  Too many messages sent by the sender. Retry after a while. */
	public static final int C2DM_MSG_SEND_ERROR_CODE_QuotaExceeded=101;
	public static final String C2DM_MSG_SEND_ERROR_QuotaExceeded="QuotaExceeded";
	/** Too many messages sent by the sender to a specific device. Retry after a while. */
	public static final int C2DM_MSG_SEND_ERROR_CODE_DeviceQuotaExceeded=102;
	public static final String C2DM_MSG_SEND_ERROR_DeviceQuotaExceeded="DeviceQuotaExceeded";
	/** Missing or bad registration_id. Sender should stop sending messages to this device. */
	public static final int C2DM_MSG_SEND_ERROR_CODE_InvalidRegistration=103;
	public static final String C2DM_MSG_SEND_ERROR_InvalidRegistration="InvalidRegistration";
	/** 
	 * The registration_id is no longer valid, for example user has uninstalled the 
	 * application or turned off notifications. Sender should stop sending messages to 
	 * this device. */
	public static final int C2DM_MSG_SEND_ERROR_CODE_NotRegistered=104;
	public static final String C2DM_MSG_SEND_ERROR_NotRegistered="NotRegistered";
	/** The payload of the message is too big, see the limitations. Reduce the size of the message. */
	public static final int C2DM_MSG_SEND_ERROR_CODE_MessageTooBig=105;
	public static final String C2DM_MSG_SEND_ERROR_MessageTooBig="MessageTooBig";
	/** Collapse key is required. Include collapse key in the request. */
	public static final int C2DM_MSG_SEND_ERROR_CODE_MissingCollapseKey=106;
	public static final String C2DM_MSG_SEND_ERROR_MissingCollapseKey="MissingCollapseKey";
	
	/** Server not available. */
	public static final int C2DM_MSG_SEND_ERROR_SRV_CODE_NOT_AVAILABLE=503;
	/** The server auth token is not valid */
	public static final int C2DM_MSG_SEND_ERROR_SRV_CODE_AUTH_TOKEN_NOT_VALID =401;
	/** No devices to send */
	public static final int C2DM_MSG_SEND_ERROR_CODE_NO_DEVICES =107;
	
	
	
	public C2DMSendMessageException(String message, String response){
		super(message);
		if(response!=null){
			checkErrorInSendMsgRequest(response);
		}
	}
	
	public C2DMSendMessageException(String message, String response, Throwable cause){
		super(message,cause);
		if(response!=null){
			checkErrorInSendMsgRequest(response);
		}		
	}
	
	public C2DMSendMessageException(int error_code){
		this.error_code=error_code;
		error=getErrorMessage();
	}
	
	public C2DMSendMessageException(int error_code, String message){
		super(message);
		this.error_code=error_code;
		error=getErrorMessage();
	}
	
	public C2DMSendMessageException(int error_code, Throwable cause){
		super(cause);
		this.error_code=error_code;	
		error=getErrorMessage();
	}
	
	private void checkErrorInSendMsgRequest(String response){
		if(response.indexOf("Error=")!=-1){
			int error_start=(response.indexOf("Error=")+6);
			String error=response.substring(error_start);
			
			if(error.equalsIgnoreCase(C2DM_MSG_SEND_ERROR_QuotaExceeded)){
				error_code=C2DM_MSG_SEND_ERROR_CODE_QuotaExceeded;
			}else if(error.equalsIgnoreCase(C2DM_MSG_SEND_ERROR_DeviceQuotaExceeded)){
				error_code=C2DM_MSG_SEND_ERROR_CODE_DeviceQuotaExceeded;
			}else if(error.equalsIgnoreCase(C2DM_MSG_SEND_ERROR_InvalidRegistration)){
				error_code=C2DM_MSG_SEND_ERROR_CODE_InvalidRegistration;
			}else if(error.equalsIgnoreCase(C2DM_MSG_SEND_ERROR_NotRegistered)){
				error_code=C2DM_MSG_SEND_ERROR_CODE_NotRegistered;
			}else if(error.equalsIgnoreCase(C2DM_MSG_SEND_ERROR_MessageTooBig)){
				error_code=C2DM_MSG_SEND_ERROR_CODE_MessageTooBig;
			}else if(error.equalsIgnoreCase(C2DM_MSG_SEND_ERROR_MissingCollapseKey)){
				error_code=C2DM_MSG_SEND_ERROR_CODE_MissingCollapseKey;
			}else{
				error_code=C2DM_MSG_SEND_ERROR_CODE_UNEXPECTED;
				error="n/a"; 
			}
			this.error=getErrorMessage();
		}
	}
	
	
}
