package org.javocsoft.push.srv.android.c2dm.credentials;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMDecryptCredentialsException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMEncryptCredentialsException;
import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMGetSrvTokenException;


/**
 * This class allows to save the C2DM credentials ({@link C2DMSenderCredentials})
 * in a safe way by using password based encryption algorithm.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class SecureC2DMCredentialsStorage {
	
	private final String ENCODING="UTF-8";
	private final String PBE_CIPHER_ALGORYTHM = "PBEWithMD5AndDES";
	private char[] PBE_PASSWORD = "mipasswordparaencriptar".toCharArray();
	private final byte[] PBE_SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,};
	
	/**
	 *  This class allows to save the C2DM credentials ({@link C2DMSenderCredentials})
	 * in a safe way by using password based encryption algorithm.
	 * 
	 * @param storePwd	Password for encryption.
	 */
	public SecureC2DMCredentialsStorage(String storePwd){
		this.PBE_PASSWORD=storePwd.toCharArray();
	}
	
	
	
    /**
	 * Saves the Secure Store.
	 *  
	 * @param out
	 * @param credentials
	 * @throws C2DMGetSrvTokenException
	 */
	public void saveSecureStore(OutputStream out, C2DMSenderCredentials credentials) throws C2DMEncryptCredentialsException{
		try{
			String cred=credentials.toString();
			//Encryption
			byte[] encData=encryptData(cred.getBytes(ENCODING));
			//Save
			out.write(encData);
			out.flush();
			
		}catch(Exception e){
			throw new C2DMEncryptCredentialsException("The secure store could not be saved: "+e.getMessage(),e);
		}
	}
    
	/**
	 * Opens the secure store giving the credentials.
	 * 
	 * @param secureStore
	 * @return C2DMSenderCredentials
	 * @throws C2DMDecryptCredentialsException
	 */
	public C2DMSenderCredentials openSecureStore(InputStream secureStore) throws C2DMDecryptCredentialsException{
		C2DMSenderCredentials credentials=null;
		
		try{
			//Read input stream data
			byte[] secStoreData=new byte[secureStore.available()];
			secureStore.read(secStoreData);
			//decrypt the data
			byte[] decData=decryptData(secStoreData);
			StringBuffer storeData=new StringBuffer();
			storeData.append(new String(decData));
			
			//Extract C2DM account info from decrypted data
			String[] c2dm_account=storeData.toString().split(C2DMSenderCredentials.CREDENTIALS_SEPARATOR);			
			String c2dm_sender_account=c2dm_account[0];
			String c2dm_sender_account_pwd=c2dm_account[1];
			//Create the credentials object.
			credentials=new C2DMSenderCredentials(c2dm_sender_account,c2dm_sender_account_pwd);
			
		}catch(Exception e){
			throw new C2DMDecryptCredentialsException("The secure store could not be decrypted: "+e.getMessage(),e);
		}
		
		return credentials;
	}
	
	
	
	/*
     * Encrypts the specified data.
     * 
     * @param data
     * @return
     * @throws GeneralSecurityException
     */
    private byte[] encryptData(byte[] data) throws GeneralSecurityException {
    	byte[] encryptedContent=null;
    	
        SecretKey key = getSecretKey();
        Cipher pbeCipher = getCipher(key,Cipher.ENCRYPT_MODE);
        
        //Encryption process
        encryptedContent=pbeCipher.doFinal(data);
        
        return encryptedContent;
    }
	
	/*
     * Decrypts the specified data.
     * 
     * @param data
     * @return
     * @throws GeneralSecurityException
     */
    private byte[] decryptData(byte[] data) throws GeneralSecurityException {
    	byte[] decryptedContent=null;
    	
    	SecretKey key = getSecretKey();
        Cipher pbeCipher = getCipher(key,Cipher.DECRYPT_MODE);
        
        //Decryption process
        decryptedContent=pbeCipher.doFinal(data);
        
        return decryptedContent;
    }
	
	/*
     * Password based encryption key.
     * 
     * @return
     * @throws GeneralSecurityException
     */
    private SecretKey getSecretKey() throws GeneralSecurityException{
    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_CIPHER_ALGORYTHM);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PBE_PASSWORD));
        
        return key;
    }
    
    /*
     * Gets the ciphering object.
     * 
     * @param key
     * @param cipherMode
     * @return
     * @throws GeneralSecurityException
     */
    private Cipher getCipher(SecretKey key, int cipherMode) throws GeneralSecurityException{
    	Cipher pbeCipher = Cipher.getInstance(PBE_CIPHER_ALGORYTHM);
        pbeCipher.init(cipherMode, key, new PBEParameterSpec(PBE_SALT, 20));
    	
        return pbeCipher;
    }
	
}
