package org.javocsoft.push.srv.android;

import static org.testng.AssertJUnit.assertNotNull;

import org.javocsoft.push.srv.android.c2dm.C2DMSrvClient;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMClientGetAuthTokenException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMClientPushException;
import org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo;
import org.testng.annotations.Test;


/**
 * This test the usage of the Google C2DM client.
 * 
 * @author Javier González Serrano
 * @since  04/11/2011
 */
public class C2DMClientTest {

	//The password to secure the credentials storage.
	final String c2dm_secure_store_pwd="1234";
	//Path to the file that will hold the credentials secure storage. 
	//It contents are ciphered with the given password above "c2dm_secure_store_pwd".
	final String c2dm_account_credentials_file="c2dm_account_credentials.c2dm";
	//Path to the file that will hold the C2DM auth token
	final String c2dm_token_file="c2dm_srv_token.c2dm";
	final String c2dm_serverName="Test Server";
	
	//C2DM Registration account information.
	//Go to http://code.google.com/intl/es-ES/android/c2dm/signup.html to register.
	private String c2dm_account_sender=null;
	private String c2dm_account_sender_pwd=null;
	
	
	
	//For testing purposes i we put here a valid android device registration_id.
	String dev_reg_id=null;
	String[] devices=null;
	
	
	@Test
    public void testApp()
    {
		setUp();
		
		assertNotNull(testClient(true));
		assertNotNull(testClient(false));
    }
	
	
	private void setUp(){
		c2dm_account_sender="";
		c2dm_account_sender_pwd="";
		
		//A registered device authentication token
		dev_reg_id="put_here_som_device_register_token_id";
		devices=new String[]{dev_reg_id};
	}
	
	/* 
	 * Gets the C2DM authentication token and sends a push.
	 * 
	 * @param tokenNotPersist	If FALSE the authentication token will 
	 * 							not be persisted in disk.
	 * @return
	 */
	private String testClient(boolean tokenNotPersist) {
		String res=null;
		StringBuffer sb=new StringBuffer();
		
		//We create the C2DM client objetc.
		C2DMSrvClient c2dmClient=new C2DMSrvClient(c2dm_secure_store_pwd, c2dm_account_credentials_file, 
												   c2dm_token_file, c2dm_account_sender, 
												   c2dm_account_sender_pwd,c2dm_serverName);
		
		try {
			
			//...and send a push to the specified devices.
			C2DMOperationInfo result=null;
			String[] pushSent=null;
			String richMediaPushUrl="http://www.google.es";
			
			String notificationId="69";
			if(!tokenNotPersist){
				//Way 1: With token being saved in disk.
				//...get the C2DM authorization token for this app.
				c2dmClient.getServerAuthenticationToken(false, true, null);
				//...and send a push to the specified devices.				
				result=c2dmClient.sendPush(devices, "This is a push test from JavocSoft - AndroidEasyPushServer library.",richMediaPushUrl,notificationId,true,true);
				//We check de result (push sent information)
				pushSent=result.getC2DMSentPushNotifications();
				for(String pushData:pushSent){
					System.out.println("PUSH Info: "+pushData);
					sb.append(pushData).append("\n");
				}
			}else{
				//Way 2: With token not being saved in disk.
				//...get the C2DM authorization token for this app.
				String c2dmToken=c2dmClient.getServerAuthenticationToken(false, false, null);
				//...and send a push to the specified devices.				
				result=c2dmClient.sendPush(c2dmToken,devices, "This is a push test from JavocSoft - AndroidEasyPushServer library.",richMediaPushUrl,notificationId,true,true);
				//We check de result (push sent information)
				pushSent=result.getC2DMSentPushNotifications();
				for(String pushData:pushSent){
					System.out.println("PUSH Info: "+pushData);
					sb.append(pushData).append("\n");
				}
			}
			res=sb.toString();
			
		} catch (C2DMClientGetAuthTokenException e) {
			e.printStackTrace();
		} catch (C2DMClientPushException e) {
			e.printStackTrace();
		}
		
		
		return res;
	}

}
