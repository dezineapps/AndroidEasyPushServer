package org.javocsoft.push.srv.android;

import static org.testng.AssertJUnit.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.javocsoft.push.srv.android.c2dm.AndroidC2DM;
import org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo;
import org.testng.annotations.Test;


/**
 * This test the process done by the C2DMSrvClient class to 
 * send a Google C2DM Push notification.
 * 
 * @author Javier González Serrano
 * @since  04/11/2011
 *
 */
public class SendPushTest {

	final static String c2dm_token_file="c2dm_srv_token.c2dm";
	
	String dev_reg_id=null;
	String[] devices=null;
	
	
	@Test
	public void testSendPush(){
		setUp();
		
		assertNotNull(sendPush());
	}
	
	
	private void setUp(){
		//Devices to send.
		dev_reg_id="put_here_som_device_register_token_id";
		devices=new String[]{dev_reg_id};
	}
	
	
	private String sendPush(){
		String res=null;
		StringBuffer sb=new StringBuffer();
		
		//Get the SRV token from the file.
		File fSrvToken=new File(c2dm_token_file);
		FileInputStream inSrvToken=null;
		try{
			inSrvToken=new FileInputStream(fSrvToken);
			C2DMOperationInfo opResult=AndroidC2DM.readSavedServerToken(inSrvToken);
			String srv_authentication_token=opResult.getC2DMSrvToken();
			if(srv_authentication_token==null){
				String[] errors=opResult.getExceptions();
				for(String e:errors){
					System.out.println(e.toString());
				}
			}else{
				System.out.println(srv_authentication_token);
				String richMediaPushUrl="http://www.google.es";
				String notificationId="69";
				C2DMOperationInfo result = AndroidC2DM.C2DM_sendMessageToDevice(srv_authentication_token,devices, "This is a push test from JavocSoft - AndroidEasyPushServer library.",richMediaPushUrl,notificationId,true,true);
				
				if(result.getExceptionsList().size()>0){
					String[] errors=result.getExceptions();
					for(String e:errors){
						System.out.println(e.toString());
					}
				}else{
					String[] info=result.getInfo();
					for(String i:info){
						System.out.println(i.toString());
						sb.append(i.toString()).append("\n");
						res=sb.toString();
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return res;
	}

}
