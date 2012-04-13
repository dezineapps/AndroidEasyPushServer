package org.javocsoft.push.srv.android.c2dm.credentials;

/**
 * C2DM registered account credentials for sending PUSH notifications.
 * 
 * To register in C2DM go to {@code http://code.google.com/intl/es-ES/android/c2dm/signup.html}
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMSenderCredentials {
	
	public static final String CREDENTIALS_SEPARATOR=":";
	
	private String c2dm_sender_account;
	private String c2dm_sender_account_pwd;
	
	/**
	 * C2DM registered account credentials for sending PUSH notifications.
	 * 
 	 * To register in C2DM go to {@code http://code.google.com/intl/es-ES/android/c2dm/signup.html}
 	 * 
	 * @param c2dm_sender_account
	 * @param c2dm_sender_account_pwd
	 */
	public C2DMSenderCredentials(String c2dm_sender_account,
			String c2dm_sender_account_pwd) {
		this.c2dm_sender_account = c2dm_sender_account;
		this.c2dm_sender_account_pwd = c2dm_sender_account_pwd;
	}

	/** This is the C2DM google account sender (an email) */
	public String getC2dm_sender_account() {
		return c2dm_sender_account;
	}


	public void setC2dm_sender_account(String c2dm_sender_account) {
		this.c2dm_sender_account = c2dm_sender_account;
	}

	/** Password of the C2DM google account */
	public String getC2dm_sender_account_pwd() {
		return c2dm_sender_account_pwd;
	}


	public void setC2dm_sender_account_pwd(String c2dm_sender_account_pwd) {
		this.c2dm_sender_account_pwd = c2dm_sender_account_pwd;
	}

	@Override
	public String toString() {
		return this.c2dm_sender_account+CREDENTIALS_SEPARATOR+this.c2dm_sender_account_pwd;
	}
	
	
	
}
