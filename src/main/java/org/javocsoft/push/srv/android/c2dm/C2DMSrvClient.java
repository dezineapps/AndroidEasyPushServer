package org.javocsoft.push.srv.android.c2dm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.javocsoft.push.srv.android.c2dm.credentials.C2DMSenderCredentials;
import org.javocsoft.push.srv.android.c2dm.credentials.SecureC2DMCredentialsStorage;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMClientGetAuthTokenException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMClientPushException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMDecryptCredentialsException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMEncryptCredentialsException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMSendMessageException;
import org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo;



/**
 * <p>
 * This class is the C2DM client main class to be used by the client.
 * </p>
 * <p>
 * Note:
 * Google C2DM does not provide a Sandbox environment so we do not have 
 * an environment option in Android. Anyway we could try to figure how
 * to achieve something like that in our own way.
 * </p>
 * <p>
 * Limitation of C2DM - IMPORTANT:
 * Google assigns a default quota, for each registered sender account, of
 * 200.000 messages per day. If you need more follow the instructions here:
 * {@code http://code.google.com/intl/es-ES/android/c2dm/quotas.html}
 * </p>
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMSrvClient {
	
	private String c2dm_credentials_storage_password;
	private String c2dm_account_credentials_file;
	private String c2dm_account_sender;
	private String c2dm_account_sender_pwd;
	private String c2dm_token_file;
	private String c2dm_serverName;
	
	
	/**
	 * <p>
	 * Creates a C2DM client. Allows to register a PUSH notification
	 * sender and to send notifications.
	 * </p>
	 * @param c2dm_credentials_storage_password		<p>Password to use in the secure storage that will
	 * 												hold the credentials of C2DM. {@link org.javocsoft.push.srv.android.c2dm.credentials.SecureC2DMCredentialsStorage}
	 * 												and {@link org.javocsoft.push.srv.android.c2dm.credentials.C2DMSenderCredentials}.</p>
	 * @param c2dm_account_credentials_file			Path to the file that will hold the secure storage.
	 * @param c2dm_token_file						Path to the file where authorization token is saved. Could be null.
	 * @param c2dm_account_sender					<p>The C2DM sender registered account.
	 * 												To register as a sender {@code http://code.google.com/intl/es-ES/android/c2dm/signup.html}</p>
	 * @param c2dm_account_sender_pwd				The C2DM sender registered account password.
	 * @param c2dm_serverName						The name of the server to be authenticated with C2DM.
	 */
	public C2DMSrvClient(String c2dm_credentials_storage_password,
					  String c2dm_account_credentials_file,
					  String c2dm_token_file,
					  String c2dm_account_sender,
					  String c2dm_account_sender_pwd,
					  String c2dm_serverName){
		this.c2dm_credentials_storage_password=c2dm_credentials_storage_password;
		this.c2dm_account_credentials_file=c2dm_account_credentials_file;
		this.c2dm_token_file=c2dm_token_file;
		this.c2dm_account_sender=c2dm_account_sender;
		this.c2dm_account_sender_pwd=c2dm_account_sender_pwd;
		this.c2dm_serverName=(!StringUtils.isEmpty(c2dm_serverName)?c2dm_serverName:C2DMConstants.C2DM_ACCOUNT_APPLICATION_INFO);
	}
	
	
	/**
	 * Allows to obtain an authorization token from C2DM
	 * 	
	 * @param update	If is set to TRUE a new authorization token will be get from Google,
	 * 					otherwise the existing one will be used.
	 * @param save		If TRUE the token is saved.
	 * @return The C2DM authorization token.
	 * @throws {@link C2DMClientGetAuthTokenException}
	 */
	public String getServerAuthenticationToken(boolean update, boolean save) throws C2DMClientGetAuthTokenException{
		String c2dm_auth_token=null;
		
		try {
			File f=new File(c2dm_account_credentials_file);
			File fSrvToken=new File(c2dm_token_file);
			
			//--CHECK/UPDATE CREDENTIALS--
			//We create the secure store for credentials
			SecureC2DMCredentialsStorage secStore=new SecureC2DMCredentialsStorage(c2dm_credentials_storage_password);
			boolean credentialsUpdated=false;
			
			//We create the credentials secure storage only if not exists.
			boolean isSecureStorageNew=false;
			FileOutputStream outSecStore=null;
			if(!f.exists()){
				outSecStore=new FileOutputStream(f);
				secStore.saveSecureStore(outSecStore, new C2DMSenderCredentials(c2dm_account_sender,c2dm_account_sender_pwd));
				outSecStore.close();
				isSecureStorageNew=true;
			}
			
			//We read the secure store for credentials
			FileInputStream in=new FileInputStream(f);
			C2DMSenderCredentials credentials=secStore.openSecureStore(in);
			in.close();
			
			if(!isSecureStorageNew && 
			   (!credentials.getC2dm_sender_account().equals(this.c2dm_account_sender) ||
			    !credentials.getC2dm_sender_account().equals(this.c2dm_account_sender_pwd))){
				//Update the credentials secure storage content.
				outSecStore=new FileOutputStream(f);
				secStore.saveSecureStore(outSecStore, new C2DMSenderCredentials(c2dm_account_sender,c2dm_account_sender_pwd));
				outSecStore.close();
				isSecureStorageNew=false;
				//We update the credentials objetc
				in=new FileInputStream(f);
				credentials=secStore.openSecureStore(in);
				in.close();
				credentialsUpdated=true;
			}
			
			//--GET C2DM Authorization TOKEN --
			if((!fSrvToken.exists() && save) || update){
				c2dm_auth_token=generateAuthToken(credentials,fSrvToken,save);
			}else{
				if(credentialsUpdated){
					c2dm_auth_token=generateAuthToken(credentials,fSrvToken,save);
				}else{
					if(!save){
						//The token is not going to be saved afterwards
						if(AndroidC2DM.C2DM_authentication_token==null){
							//If is not in memory we generate it
							c2dm_auth_token=generateAuthToken(credentials,fSrvToken,save);
						}else{
							//If yes we read it
							c2dm_auth_token=AndroidC2DM.C2DM_authentication_token; //From memory
						}
					}else{
						//The token is going to be saved afterwards.
						if(fSrvToken.exists()){
							//Already exists, we open and read it.
							c2dm_auth_token=readSrvAuthToken(fSrvToken);
						}else{
							//Is not created yet. We get it and save.
							c2dm_auth_token=generateAuthToken(credentials,fSrvToken,save);
						}
						
					}
				}
			}
		} catch (C2DMEncryptCredentialsException e) {
			throw new C2DMClientGetAuthTokenException(e.getMessage(),e);
		} catch (C2DMDecryptCredentialsException e) {
			throw new C2DMClientGetAuthTokenException(e.getMessage(),e);
		} catch (IOException e){
			throw new C2DMClientGetAuthTokenException(e.getMessage(),e);
		}
		
		return c2dm_auth_token;
	}
	
	
	/**
	 * Send a PUSH to the specified devices.
	 * <p>
	 * Limitation of C2DM - IMPORTANT:
	 * Google assigns a default quota, for each registered sender account, of
	 * 200.000 messages per day. If you need more follow the instructions here:
	 * {@code http://code.google.com/intl/es-ES/android/c2dm/quotas.html}
	 * </p>
	 * 
	 * @param devices		A list of device registrations ids.
	 * @param message		Message of the PUSH notification.
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param notificationId  Server generated notificationId. This is for efficacy purposes.
	 * @param alertOnIdle	Set to TRUE to make the message be notified in the 
	 * 						android device even if the device is idle.
	 * @return {@link C2DMOperationInfo}
	 * @throws {@link C2DMClientPushException}
	 */
	public C2DMOperationInfo sendPush(String[] devices, String message, String richMediaPushUrl, String notificationId, boolean alertOnIdle) throws C2DMClientPushException{
		
		C2DMOperationInfo result = sendPush(devices, message, richMediaPushUrl, notificationId, alertOnIdle, true);
		
		return result;
		
	}
	
	
	/**
	 * Send a PUSH to the specified devices.
	 * 
	 * <p>
	 * Limitation of C2DM - IMPORTANT:
	 * Google assigns a default quota, for each registered sender account, of
	 * 200.000 messages per day. If you need more follow the instructions here:
	 * {@code http://code.google.com/intl/es-ES/android/c2dm/quotas.html}
	 * </p>
	 * 
	 * @param devices		A list of device registrations ids.
	 * @param message		Message of the PUSH notification
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param notificationId  Server generated notificationId. This is for efficacy purposes.
	 * @param alertOnIdle	Set to TRUE to make the message be notified in the 
	 * 						android device even if the device is idle.
	 * @param groupMessages	By setting this to FALSE, will make all push sent 
	 * 						to the device be notified and not only the last received 
	 * 						one. By default messages are grouped to avoid filling the 
	 * 						notification bar.
	 * @return {@link C2DMOperationInfo}
	 * @throws {@link C2DMClientPushException}
	 */
	public C2DMOperationInfo sendPush(String[] devices, String message, String richMediaPushUrl, String notificationId, boolean alertOnIdle, boolean groupMessages) throws C2DMClientPushException{
		C2DMOperationInfo result = null;
			
		try{
			//Get the SRV token from the file.
			File fSrvToken=new File(c2dm_token_file);
			FileInputStream inSrvToken=new FileInputStream(fSrvToken);
			C2DMOperationInfo opResult=AndroidC2DM.readSavedServerToken(inSrvToken);
			String srv_authentication_token=opResult.getC2DMSrvToken();
			if(srv_authentication_token==null){
				String[] errors=opResult.getExceptions();
				StringBuffer sbE=new StringBuffer();
				for(String e:errors){
					sbE.append(e.toString()+"\n");
				}
				throw new C2DMClientPushException(sbE.toString());
			}else{
				System.out.println(srv_authentication_token);		
				result = AndroidC2DM.C2DM_sendMessageToDevice(srv_authentication_token,devices, message,richMediaPushUrl,notificationId, alertOnIdle, groupMessages);
				
				if(result.getExceptionsList().size()>0){
					String[] errors=result.getExceptions();
					StringBuffer sbE=new StringBuffer();
					for(String e:errors){
						sbE.append(e.toString()+"\n");
					}
					throw new C2DMClientPushException(sbE.toString());
				}else{
					String[] info=result.getInfo();
					for(String i:info){
						System.out.println(i.toString());
					}
				}
			}
		}catch(IOException e){
			throw new C2DMClientPushException(e.getMessage(),e);
		}
		
		return result;
	}
	
	
	/**
	 * <p>
	 * Send a PUSH to the specified devices.
	 * 
	 * (this is usually used when the c2dm auth token is not saved in disk)
	 * </p>
	 * <p>
	 * Limitation of C2DM - IMPORTANT:
	 * Google assigns a default quota, for each registered sender account, of
	 * 200.000 messages per day. If you need more follow the instructions here:
	 * {@code http://code.google.com/intl/es-ES/android/c2dm/quotas.html}
	 * </p>
	 * 
	 * @param c2dmAuthToken		C2DM authorization token.
	 * @param devices			A list of device registrations ids.
	 * @param message			Message of the PUSH notification
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param notificationId  Server generated notificationId. This is for efficacy purposes.
	 * @param alertOnIdle		Set to TRUE to make the message be notified in the 
	 * 							android device even if the device is idle.
	 * @return See {@link C2DMOperationInfo}
	 * @throws See {@link C2DMClientPushException}
	 */
	public C2DMOperationInfo sendPush(String c2dmAuthToken, String[] devices, String message, String richMediaPushUrl, String notificationId, boolean alertOnIdle) throws C2DMClientPushException{
		C2DMOperationInfo result = sendPush(c2dmAuthToken, devices, message, richMediaPushUrl, notificationId, alertOnIdle, true);
		
		return result;
	}
	
	
	/**
	 * <p>
	 * Send a PUSH to the specified devices.
	 * 
	 * (this is usually used when the c2dm auth token is not saved in disk)
	 * </p>
	 * <p>
	 * Limitation of C2DM - IMPORTANT:
	 * Google assigns a default quota, for each registered sender account, of
	 * 200.000 messages per day. If you need more follow the instructions here:
	 * {@code http://code.google.com/intl/es-ES/android/c2dm/quotas.html}
	 * </p>
	 * 
	 * @param c2dmAuthToken		C2DM authorization token.
	 * @param devices			A list of device registrations ids.
	 * @param message			Message of the PUSH notification
	 * @param richMediaPushUrl	An additional information like an url.
	 * @param notificationId  Server generated notificationId. This is for efficacy purposes.
	 * @param alertOnIdle		Set to TRUE to make the message be notified in the 
	 * 							android device even if the device is idle.
	 * @param groupMessages		By setting this to FALSE, will make all push sent 
	 * 							to the device be notified and not only the last received 
	 * 							one. By default messages are grouped to avoid filling the 
	 * 							notification bar.
	 * @return See {@link C2DMOperationInfo}
	 * @throws See {@link C2DMClientPushException}
	 */
	public C2DMOperationInfo sendPush(String c2dmAuthToken, String[] devices, String message, String richMediaPushUrl, String notificationId, boolean alertOnIdle, boolean groupMessages) throws C2DMClientPushException{
		C2DMOperationInfo result = null;
		
		if(c2dmAuthToken==null || (c2dmAuthToken!=null && c2dmAuthToken.length()==0)){
			C2DMSendMessageException sendException=new C2DMSendMessageException(C2DMSendMessageException.C2DM_MSG_SEND_ERROR_SRV_CODE_AUTH_TOKEN_NOT_VALID);
			throw new C2DMClientPushException(sendException);
		}
		
		System.out.println(c2dmAuthToken);		
		result = AndroidC2DM.C2DM_sendMessageToDevice(c2dmAuthToken,devices,message,richMediaPushUrl,notificationId,alertOnIdle,groupMessages);
		
		if(result.getExceptionsList().size()>0){
			String[] errors=result.getExceptions();
			StringBuffer sbE=new StringBuffer();
			for(String e:errors){
				sbE.append(e.toString()+"\n");
			}
			throw new C2DMClientPushException(sbE.toString());
		}else{
			String[] info=result.getInfo();
			for(String i:info){
				System.out.println(i.toString());
			}
		}
		
		return result;
	}
	
	
	//AUXILIAR FUNCTIONS
	
	/*
	 * 
	 * @param fSrvToken
	 * @return
	 * @throws C2DMClientGetAuthTokenException
	 * @throws IOException
	 */
	private String readSrvAuthToken(File fSrvToken) throws C2DMClientGetAuthTokenException,IOException{
		String c2dm_auth_token=null;
		
		//We read the server authorization token
		FileInputStream inSrvToken=new FileInputStream(fSrvToken);
		C2DMOperationInfo result=AndroidC2DM.readSavedServerToken(inSrvToken);
		inSrvToken.close();
					
		//Check for any errors
		if(result.getExceptionsList().size()>0){
			String[] errors=result.getExceptions();
			StringBuffer sbE=new StringBuffer();
			for(String e:errors){
				sbE.append(e.toString()+"\n");
			}
			throw new C2DMClientGetAuthTokenException(sbE.toString());
		}else{
			c2dm_auth_token=result.getC2DMSrvToken();
		}
		
		return c2dm_auth_token.trim();
	}
	
	/*
	 * 
	 * @param credentials
	 * @param fSrvToken
	 * @param save
	 * @return
	 * @throws C2DMClientGetAuthTokenException
	 * @throws IOException
	 */
	private String generateAuthToken(C2DMSenderCredentials credentials, File fSrvToken, boolean save) throws C2DMClientGetAuthTokenException,IOException{
		String c2dm_auth_token=null;
		
		//We obtain the C2DM authorization token and save it.
		C2DMOperationInfo result = null;
		if(save){
			FileOutputStream outSrvToken=new FileOutputStream(fSrvToken);
			result = AndroidC2DM.obtainC2DMAuthenticationToken(c2dm_serverName,credentials,save?outSrvToken:null);
			outSrvToken.close();
		}else{
			result = AndroidC2DM.obtainC2DMAuthenticationToken(c2dm_serverName,credentials,null);
		}
		
		//Check for any errors
		if(result.getExceptionsList().size()>0){
			String[] errors=result.getExceptions();
			StringBuffer sbE=new StringBuffer();
			for(String e:errors){
				sbE.append(e.toString()+"\n");
			}
			throw new C2DMClientGetAuthTokenException(sbE.toString());
		}else{
			c2dm_auth_token=result.getC2DMSrvToken();
		}
		
		return c2dm_auth_token.trim();
	}
}
