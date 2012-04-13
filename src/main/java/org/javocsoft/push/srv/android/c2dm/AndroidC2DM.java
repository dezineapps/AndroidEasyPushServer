package org.javocsoft.push.srv.android.c2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.javocsoft.push.srv.android.c2dm.credentials.C2DMSenderCredentials;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMGetSrvTokenException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMSendMessageException;
import org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo;
import org.javocsoft.push.srv.android.c2dm.operation.info.type.C2DMInfoData;



/**
 * C2DM Core Class.
 * 
 * <p>
 * Before being able to send notifications you need to register 
 * in C2DM(Cloud to Device Messaging) Service using this link 
 * {@code http://code.google.com/intl/es-ES/android/c2dm/signup.html}<br><br>
 *  ...registration needs a day more or less to activate your account 
 *  before being able to start sending PUSH messages.
 * </p>
 * <p>
 * Note - SANDBOX Environment:
 * Google C2DM does not provide a Sandbox environment so we do not have 
 * an environment option in Android. Anyway we could register a device in
 * a SANDBOX environment specifying this as a property during registration. 
 * Afterwards, we could filter devices internally using this property.
 * </p>
 * <p>
 * Limitation of C2DM - IMPORTANT:
 * Google assigns a default quota, for each registered sender account, of
 * 200.000 messages per day. If you need more follow the instructions here:
 * {@code http://code.google.com/intl/es-ES/android/c2dm/quotas.html}
 * </p>
 * <p> 
 *  HowTo: {@code http://code.google.com/intl/es-ES/android/c2dm/}
 * </p>
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class AndroidC2DM {
	private static final String ENCODING = "UTF-8";
	
	public static String C2DM_authentication_token=null;
	private static Random r=new Random();
	
		
	/**
	 * Gets the authentication token for this server.
	 * 
	 * @param c2dm_serverName	The name of the server to be authenticated with C2DM.
	 * @param credentials		Credentials of the server. {@link org.javocsoft.push.srv.android.c2dm.credentials.C2DMSenderCredentials}
	 * @param tokenOutput		Output stream where save the token. Can be null.
	 * @return {@link org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo}
	 */
	public static C2DMOperationInfo obtainC2DMAuthenticationToken(String c2dm_serverName, C2DMSenderCredentials credentials, OutputStream tokenOutput){
		
		C2DMOperationInfo opInfo=new C2DMOperationInfo(C2DMOperationInfo.C2DM_SRV_REGISTER);
		try{
			String token = AndroidC2DM.getAuthenticationServerToken(c2dm_serverName,credentials.getC2dm_sender_account(),credentials.getC2dm_sender_account_pwd());
			if(tokenOutput!=null){
				saveServerToken(tokenOutput,token);
			}
			opInfo.addInfo(C2DMInfoData.INFO_DATA_SRV_TOKEN,token);
		}catch(C2DMGetSrvTokenException e){
			opInfo.addException(e.error_code,e.error,e);
		}	
			
		return opInfo;
	}

	/**
	 * This method sends a PUSH message to the specified devices in the list.
	 * 
	 * @param devices
	 * @param message
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param notificationId  Server generated notificationId. This is for efficacy purposes.
	 * @param alertOnIdle	Set to TRUE to make the message be notified in the 
	 * 						android device even if the device is idle.
	 * @return C2DMOperationInfo
	 */
	public static C2DMOperationInfo C2DM_sendMessageToDevice(String[] devices, String message, String richMediaPushUrl, String notificationId, boolean alertOnIdle){
		C2DMOperationInfo opInfo=new C2DMOperationInfo(C2DMOperationInfo.C2DM_SEND_MSG);
		if(devices.length==0){
			opInfo.addException(C2DMSendMessageException.C2DM_MSG_SEND_ERROR_CODE_NO_DEVICES,"No Devices to send",null);
		}else{
			for(String dev_reg_id:devices){
				try{
					String notId = sendMessage(AndroidC2DM.C2DM_authentication_token,dev_reg_id, message, notificationId, richMediaPushUrl,alertOnIdle,true);
					 //Add the notification of the sent PUSH + deviceRegId + Date of delivery
					opInfo.addInfo(C2DMInfoData.INFO_DATA_PUSH_NOT_ID, notId + 
									C2DMInfoData.INFO_DATA_PUSH_NOT_INFO_SEPARATOR + dev_reg_id + 
									C2DMInfoData.INFO_DATA_PUSH_NOT_INFO_SEPARATOR + new java.util.Date().getTime());
				}catch(C2DMSendMessageException e){
					opInfo.addException(e.error_code,e.error,e);
				}				
			}
		}
		
		return opInfo;
	}
	
	/**
	 * This method sends a PUSH message to the specified devices in the list.
	 * 
	 * @param srv_auth_token
	 * @param devices
	 * @param message
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param notificationId  Server generated notificationId. This is for efficacy purposes.
	 * @param alertOnIdle	Set to TRUE to make the message be notified in the 
	 * 						android device even if the device is idle.
	 * @param groupMessages	By setting this to FALSE, will make all push sent 
	 * 						to the device be notified and not only the last received 
	 * 						one. By default messages are grouped to avoid filling the 
	 * 						notification bar.
	 * @return {@link org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo}
	 */
	public static C2DMOperationInfo C2DM_sendMessageToDevice(String srv_auth_token, String[] devices, String message, String richMediaPushUrl, String notificationId, boolean alertOnIdle, boolean groupMessages){
		C2DMOperationInfo opInfo=new C2DMOperationInfo(C2DMOperationInfo.C2DM_SEND_MSG);
		if(devices.length==0){
			opInfo.addException(C2DMSendMessageException.C2DM_MSG_SEND_ERROR_CODE_NO_DEVICES,"No Devices to send",null);
		}else{
			AndroidC2DM.C2DM_authentication_token=srv_auth_token;
			for(String dev_reg_id:devices){
				try{
					String notId = sendMessage(AndroidC2DM.C2DM_authentication_token,dev_reg_id, notificationId, message,richMediaPushUrl,alertOnIdle,groupMessages).trim();
					 //Add the notification of the sent PUSH + deviceRegId + Date of delivery
					opInfo.addInfo(C2DMInfoData.INFO_DATA_PUSH_NOT_ID, notId + 
									C2DMInfoData.INFO_DATA_PUSH_NOT_INFO_SEPARATOR + dev_reg_id + 
									C2DMInfoData.INFO_DATA_PUSH_NOT_INFO_SEPARATOR + new java.util.Date().getTime());
				}catch(C2DMSendMessageException e){
					opInfo.addException(e.error_code,e.error,e);
				}	
			}
		}
		
		return opInfo;
	}
	
	/**
	 * Reads the token from an input stream.
	 * 
	 * @param tokenInput
	 * @return {@link org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo}
	 */
	public static C2DMOperationInfo readSavedServerToken(InputStream tokenInput){
	
		C2DMOperationInfo opInfo=new C2DMOperationInfo(C2DMOperationInfo.C2DM_SRV_RETRIEVE_TOKEN);
		try{
			//Using Apache Commons IO
			StringWriter writer = new StringWriter();
			IOUtils.copy(tokenInput, writer, ENCODING);
			String token = writer.toString().trim();
			if(token!=null){
				opInfo.addInfo(C2DMInfoData.INFO_DATA_SRV_TOKEN,token.trim());
			}
		}catch(Exception e){
			opInfo.addException(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_CODE_READ_SAVED_TOKEN,e.getMessage(),new C2DMGetSrvTokenException("Error: readSavedServerToken()",null,e));
		}
		
		return opInfo;
	}
	
	
	
	
	
	//AUXILIAR FUNCTIONS
	
	/*
	 * Saves the token.
	 *  
	 * @param out
	 * @param token
	 * @throws C2DMGetSrvTokenException
	 */
	private static void saveServerToken(OutputStream out, String token) throws C2DMGetSrvTokenException{
		try{
			out.write(token.getBytes(ENCODING));
			out.flush();
			
			AndroidC2DM.C2DM_authentication_token=token;
		}catch(Exception e){
			throw new C2DMGetSrvTokenException("Error: saveServerToken()",null,e);
		}
	}
	
	
	/*
	 * 
	 * The server needs to get its C2DM registration token to be able
	 * to send PUSH messages.
	 * 
	 * This method requests the server authentication token via the 
	 * registered email address for C2DM.
	 *  
	 * @param c2dm_serverName				The name of the server to be authenticated with C2DM. 
	 * @param email
	 * @param password
	 * @return Authorization token
	 * @throws C2DMGetSrvTokenException
	 */
	private static String getAuthenticationServerToken(String c2dm_serverName, String email, String password) throws C2DMGetSrvTokenException {
		String authToken=null;
		
		// Create a new HttpClient and Post Header 
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(C2DMConstants.C2DM_SERVICE_AUTH_URL); 
		// this is for proxy settings 

		HttpParams params = httpclient.getParams(); 
		params.setParameter("content-type", "application/x-www-form-urlencoded"); 
		try { 
           // Add your data 
           List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2); 
           nameValuePairs.add(new BasicNameValuePair("accountType",C2DMConstants.C2DM_ACCOUNT_TYPE__HOSTED_OR_GOOGLE)); 
           nameValuePairs.add(new BasicNameValuePair("Email",email)); 
           nameValuePairs.add(new BasicNameValuePair("Passwd",password)); 
           nameValuePairs.add(new BasicNameValuePair("service", C2DMConstants.C2DM_SERVICE_NAME)); 
           nameValuePairs.add(new BasicNameValuePair("source",  (!StringUtils.isEmpty(c2dm_serverName)?c2dm_serverName:C2DMConstants.C2DM_ACCOUNT_APPLICATION_INFO))); 
           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
    
           // Execute HTTP Post Request 
           HttpResponse response = httpclient.execute(httppost);
           
           if(response.getStatusLine().getStatusCode()!=200){
        	   if(response.getStatusLine().getStatusCode()==C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_CODE_WRONG_C2DM_ACCOUNT_DATA){
        		   throw new C2DMGetSrvTokenException(C2DMGetSrvTokenException.C2DM_GET_SRV_TOKEN_ERROR_CODE_WRONG_C2DM_ACCOUNT_DATA,response.getStatusLine().getReasonPhrase());
        	   }else{
        		   throw new C2DMGetSrvTokenException("("+response.getStatusLine().getStatusCode()+")"+response.getStatusLine().getReasonPhrase(),null);
        	   }
           }
           
           if (response.getEntity() != null){
				String opResponse=EntityUtils.toString(response.getEntity());
				System.out.println(opResponse);
				
				//Check for errors
				if(opResponse.indexOf("Error=")!=-1){
					throw new C2DMGetSrvTokenException("Error: getAuthenticationServerToken()",opResponse);
				}
				
				 //authToken=result.substring(result.indexOf("Auth=")+5);   
				 authToken=opResponse.substring(opResponse.indexOf("Auth=")+5);
			}
           
		}catch(C2DMGetSrvTokenException e){
			throw e;
		}catch(Exception e){
			throw new C2DMGetSrvTokenException("Error: sendMessage()",null,e);
		}
		
		return authToken;
	}
	
	
	/*
	 * 
	 * @param auth_token		The server authentication token.
	 * @param registrationId	Server registration Id.
	 * @param srvNotificationId Server generated notificationId. This is for efficacy purposes.
	 * @param message			Message to send.
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param alertOnIdle		Set to TRUE to make the message be notified in the 
	 * 							android device even if the device is idle.
	 * @param groupMessages		By setting this to FALSE, will make all push sent 
	 * 							to the device be notified and not only the last received 
	 * 							one. By default messages are grouped to avoid filling the 
	 * 							notification bar.
	 * @return The notification id if is successfully sent, otherwise null.
	 * @throws C2DMSendMessageException
	 */
	private static String sendMessage(String auth_token, String registrationId, String srvNotificationId, String message, String richMediaPushUrl, boolean alertOnIdle, boolean groupMessages) throws C2DMSendMessageException {
		String notificationId=null;
		
		try{
			// Create a new HttpClient and Post Header 
			HttpClient httpclient = new DefaultHttpClient(); 
			HttpPost httppost = new HttpPost(C2DMConstants.C2DM_SERVICE_SEND_URL); 
	
			//The Authorization Token
			httppost.addHeader("Authorization", "GoogleLogin auth="+auth_token); 
			httppost.addHeader("Content-Type","application/x-www-form-urlencoded"); 
			
			// Add my data 
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2); 
			nameValuePairs.add(new BasicNameValuePair(C2DMConstants.C2DM_SEND_PARAM_REGISTRATION_ID,registrationId)); 
			if(alertOnIdle){
				nameValuePairs.add(new BasicNameValuePair(C2DMConstants.C2DM_SEND_PARAM_DELAY_WHILE_IDLE, "0"));
			}else{
				nameValuePairs.add(new BasicNameValuePair(C2DMConstants.C2DM_SEND_PARAM_DELAY_WHILE_IDLE, "1"));
			}
			if(groupMessages){
				nameValuePairs.add(new BasicNameValuePair(C2DMConstants.C2DM_SEND_PARAM_COLLAPSE_KEY, "0"));
			}else{
				//We generate a data that is diferent each time to avoid grouping the messages.
				String rndValue=String.valueOf(new java.util.Date().getTime())+String.valueOf(r.nextInt());
				nameValuePairs.add(new BasicNameValuePair(C2DMConstants.C2DM_SEND_PARAM_COLLAPSE_KEY, rndValue));
			}
	
			//.<C2DMConstants.C2DM_SEND_PARAM_KEYPAIR> is the key and message is the text to send in the push
			nameValuePairs.add(new BasicNameValuePair((C2DMConstants.C2DM_SEND_PARAM_KEYPAIR+C2DMConstants.C2DM_SEND_MESSAGE_PARAM),message));
			if(richMediaPushUrl!=null){
				nameValuePairs.add(new BasicNameValuePair((C2DMConstants.C2DM_SEND_PARAM_KEYPAIR+C2DMConstants.C2DM_SEND_MESSAGE_RICHMEDIAURL_PARAM),richMediaPushUrl));
			}
			//Internal server generated notification id. 
			nameValuePairs.add(new BasicNameValuePair((C2DMConstants.C2DM_SEND_PARAM_KEYPAIR+C2DMConstants.C2DM_SEND_MESSAGE_NOTIFICATIONID_PARAM),srvNotificationId));
			
			
			UrlEncodedFormEntity entity=new UrlEncodedFormEntity(nameValuePairs,ENCODING);
			httppost.setEntity(entity); 
			//httppost.addHeader("Content-Length", Integer.toString(((int)entity.getContentLength())));
			
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost); 
			
			//System.out.println(response.getStatusLine());
			if(response.getStatusLine().getStatusCode()!=200){
	        	   throw new C2DMSendMessageException("("+response.getStatusLine().hashCode()+")"+response.getStatusLine().getReasonPhrase(),null);
	        }
			 
			if (response.getEntity() != null){
				String opResponse=EntityUtils.toString(response.getEntity());
				System.out.println(opResponse);
				
				//Check for errors
				if(opResponse.indexOf("Error=")!=-1){
					throw new C2DMSendMessageException("Error: sendMessage()",opResponse);
				}
				
				//Extract sent notification id
				notificationId=opResponse.substring(opResponse.indexOf("id=")+3);
			}
		
		}catch(C2DMSendMessageException e){
			throw e;
		}catch(Exception e){
			throw new C2DMSendMessageException("Error: sendMessage()",null,e);
		}
		
		return notificationId;
	}
	
	
	/*
	 * Converts a Http Response in a String.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private static String httpResponseToString(HttpResponse response) throws IOException{
		String res=null;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); 
		StringBuffer sb = new StringBuffer(""); 
		String line = ""; 
		while ((line = in.readLine()) != null){ 
			sb.append(line); 
		} 
		in.close();
		res = sb.toString(); 
		
		return res;
	}
}
