package org.javocsoft.push.srv.android.c2dm.ack;

import org.javocsoft.push.srv.android.c2dm.ack.exceptions.C2DMDeviceACKException;


/**
 * This interface covers the basic server efficacy (ACK) operations.
 * 
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public interface ACKInterface {
	
	public static final String ACK_PARAM_DEVICEID="deviceid";
	public static final String ACK_PARAM_REGISTRATIONID="registrationid";
	public static final String ACK_PARAM_NOTIFICATIONID="notId";
	
	
	/**
	 * This method registers an ACK on the server.
	 * 
	 * @param andACK	ACK data to register.
	 * @return			TRUE if the ACK was correctly inserted, otherwise FALSE.
	 * @throws C2DMDeviceACKException
	 */
	public boolean receiveACK(AndroidACK andACK) throws C2DMDeviceACKException;
	
	
}
