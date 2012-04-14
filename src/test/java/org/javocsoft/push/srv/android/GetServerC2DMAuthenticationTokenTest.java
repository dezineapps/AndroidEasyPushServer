package org.javocsoft.push.srv.android;

import static org.testng.AssertJUnit.assertNotNull;

import org.javocsoft.push.srv.android.c2dm.AndroidC2DM;
import org.javocsoft.push.srv.android.c2dm.credentials.C2DMSenderCredentials;
import org.javocsoft.push.srv.android.c2dm.credentials.SecureC2DMCredentialsStorage;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMDecryptCredentialsException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMEncryptCredentialsException;
import org.javocsoft.push.srv.android.c2dm.operation.info.C2DMOperationInfo;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * This test the process done by the C2DMSrvClient class to 
 * get a Google Authentication token.
 * 
 * @author Javier González Serrano
 * @since  04/11/2011
 */
public class GetServerC2DMAuthenticationTokenTest
{
	
	final String c2dm_account_credentials_file="c2dm_account_credentials.c2dm";
	final String c2dm_account_sender="";
	final String c2dm_account_sender_pwd="";
	final String c2dm_token_file="c2dm_srv_token.c2dm";
	final String c2dm_serverName="Test Server";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GetServerC2DMAuthenticationTokenTest( String testName )
    {
    }

    @Test
    public void testApp()
    {
    	String srvToken=getServerC2DMAuthenticationToken();
    	assertNotNull(srvToken);	
    }
    
    
    /* 
     * Ask to Google for a C2DM token.
     * 
     * @return The token string or null in case of error.
     */
    private String getServerC2DMAuthenticationToken(){
    	String token=null;
    	
    	//We create the secure store for credentials
		SecureC2DMCredentialsStorage secStore=new SecureC2DMCredentialsStorage("1234");
				
		try {
			File f=new File(c2dm_account_credentials_file);
			FileOutputStream out=new FileOutputStream(f);
			secStore.saveSecureStore(out, new C2DMSenderCredentials(c2dm_account_sender,c2dm_account_sender_pwd));
			out.close();
			
			//We read the secure store for credentials
			FileInputStream in=new FileInputStream(f);
			C2DMSenderCredentials credentials=secStore.openSecureStore(in);
			in.close();
			
			File fSrvToken=new File(c2dm_token_file);
			FileOutputStream outSrvToken=new FileOutputStream(fSrvToken);
			C2DMOperationInfo result = AndroidC2DM.obtainC2DMAuthenticationToken(c2dm_serverName,credentials,outSrvToken);
			out.close();
			
			if(result.getExceptionsList().size()>0){
				String[] errors=result.getExceptions();
				for(String e:errors){
					System.out.println(e.toString());
				}
			}else{
				token=result.getC2DMSrvToken();
			}
		} catch (C2DMEncryptCredentialsException e) {
			e.printStackTrace();
		} catch (C2DMDecryptCredentialsException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
    	
    	return token;
    }
}
