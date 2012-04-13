/**
 * C2DM Constant values.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
package org.javocsoft.push.srv.android.c2dm;

/**
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public class C2DMConstants {
	
	public final static String C2DM_ACCOUNT_APPLICATION_INFO = "JavocSoft Android Push Server";
	
	/** The address to register with C2DM */
	public final static String C2DM_SERVICE_AUTH_URL = "https://www.google.com/accounts/ClientLogin";
	/** The address to send PUSH notifications. */
	public final static String C2DM_SERVICE_SEND_URL = "https://android.apis.google.com/c2dm/send";
	
	/** This parameter is used to set the Android registration ID.*/
	public static final String C2DM_SEND_PARAM_REGISTRATION_ID = "registration_id";
	/** If is set to 1 the C2DM message isn't received by the android device if the device is idle.
	 *  By that, it means the screen is dark. If the device is revived (press the power button), 
	 *  the message is notified.  */
	public static final String C2DM_SEND_PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
	/** This parameter is used for overriding old messages with the same key on the Google C2DM servers.
	 * When a group of notifications use the same collapse_key, only the last one is received by the android device. */
	public static final String C2DM_SEND_PARAM_COLLAPSE_KEY = "collapse_key";
	
	/** Type of Google account. HOSTED or GOOGLE account */
	public static final String C2DM_ACCOUNT_TYPE__HOSTED_OR_GOOGLE="HOSTED_OR_GOOGLE";
	/** Type of Google account. GOOGLE account */
	public static final String C2DM_ACCOUNT_TYPE__GOOGLE="GOOGLE";
	/** C2DM Service name */
	public static final String C2DM_SERVICE_NAME="ac2dm";
	
	/** This is the key (plus {@link C2DMConstants#C2DM_SEND_PARAM_KEYPAIR}) where the message is going
	 * to be set */
	public static final String C2DM_SEND_MESSAGE_PARAM="payload";
	
	/** This is the key (plus {@link C2DMConstants#C2DM_SEND_PARAM_KEYPAIR}) where the message rich media url is going
	 * to be set */
	public static final String C2DM_SEND_MESSAGE_RICHMEDIAURL_PARAM="richmediaUrl";
	
	/** This is the key (plus {@link C2DMConstants#C2DM_SEND_PARAM_KEYPAIR}) where the message server 
	 * generated notification id is going to be set for afterwards efficacy purposes */
	public static final String C2DM_SEND_MESSAGE_NOTIFICATIONID_PARAM="notId";
	
	
	
	/** 
	 * <p>
	 * When sending a PUSH message it is possible to add extra values to it by using
	 * this PARAMETER plus the name of the desired key, equals and finally the content.
	 * </p>
	 * <p>
	 * Each value set this way will be put as an extra String in the sent Push Intent
	 * from Google C2DM. The Max number of key-pairs is 4 and each value has a max size
	 * of 1024 bytes.
	 * 
	 * </p>
	 * <p>
	 * Example:
	 *	Server side: (payload is where the PUSH message is set) 
	 *	...&"+append(C2DMConstants.C2DM_SEND_PARAM_KEYPAIR+"payload").append("=").append(URLEncoder.encode(message, UTF8)
	 *	Mobile side:
	 *	final String payload = intent.getStringExtra("payload"); 	
	 * </p> 
	 *  */
	public static final String C2DM_SEND_PARAM_KEYPAIR = "data.";

	/** The available environments. */
	public enum TargetServer
	{
		SANDBOX, PRODUCTION
	}
}
