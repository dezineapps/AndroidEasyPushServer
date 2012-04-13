package org.javocsoft.push.srv.android.c2dm.registration;

import java.util.List;

import org.javocsoft.push.srv.android.c2dm.C2DMConstants.TargetServer;
import org.javocsoft.push.srv.android.c2dm.registration.data.PushDevice;
import org.javocsoft.push.srv.android.c2dm.registration.exceptions.C2DMDeviceRegistrationException;



/**
 * This interface covers the basic server registration operations.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 *
 */
public interface RegistrationInterface {
	
	public static final String REGISTRATION_PARAM_DEVICEID="deviceid";
	public static final String REGISTRATION_PARAM_REGISTRATIONID="registrationid";
	public static final String REGISTRATION_PARAM_REGISTRATIONMAIL="registrationEMail";
	public static final String REGISTRATION_PARAM_DEVICENAME="deviceName";
	public static final String REGISTRATION_PARAM_DEVICEMODEL="deviceModel";
	public static final String REGISTRATION_PARAM_DEVICEOS="deviceOS";
	public static final String REGISTRATION_PARAM_DEVICEACTIVE="deviceActive";
	
	
	/**
	 * This method registers a device.
	 * 
	 * @param deviceToRegister		Device data to register.
	 * @return						The registration device object once is registered, otherwise
	 * 								null.
	 * @throws C2DMDeviceRegistrationException
	 */
	public PushDevice registerDevice(PushDevice deviceToRegister) throws C2DMDeviceRegistrationException;
	
	/**
	 * This method removes the registration of the specified device.
	 * 
	 * @param pushDevice	Device to unregister.
	 * @return				TRUE if all if is successfully unregistered, otherwise FALSE.
	 * @throws C2DMDeviceRegistrationException
	 */
	public boolean unregisterDevice(PushDevice pushDevice) throws C2DMDeviceRegistrationException;
	
	/**
	 * 
	 * @param registrationId
	 * @return {@link org.javocsoft.push.srv.android.c2dm.registration.data.PushDevice}
	 * @throws C2DMDeviceRegistrationException
	 */
	public PushDevice getRegisterInformation(String registrationId) throws C2DMDeviceRegistrationException;
	
	/**
	 * This method desactivates a device. A desactivated device is still registered but
	 * is marked to not receive notifications.
	 * 
	 * @param pushDevice	The device to activate/desactivate
	 * @param activation	If is set to TRUE the device is activate, otherwise is desactivated.
	 * @return				The actual activation status. TRUE if activate, FALSE otherwise.
	 * @throws C2DMDeviceRegistrationException
	 */
	public boolean activationRegisteredDevice(PushDevice pushDevice, boolean activation) throws C2DMDeviceRegistrationException;
	
	/**
	 * This method lists the registered devices.
	 * 
	 * @param onlyActive	A device can be desactivated instead of being unregistered. If
	 * 						is set to TRUE only the active devices will be listed, otherwise
	 * 						all.
	 * @return				A list containing the devices.
	 * @throws C2DMDeviceRegistrationException
	 */
	public List<PushDevice> listRegisteredDevices(boolean onlyActive) throws C2DMDeviceRegistrationException;
	
	/**
	 * This method lists the registered devices.
	 * 
	 * @param target		Gets on ly the devices in the specified environment target. {@link org.javocsoft.push.srv.android.c2dm.C2DMConstants}.
	 * @param onlyActive	A device can be desactivated instead of being unregistered. If
	 * 						is set to TRUE only the active devices will be listed, otherwise
	 * 						all.
	 * @return				A list containing the devices.
	 * @throws C2DMDeviceRegistrationException
	 */
	public List<PushDevice> listRegisteredDevices(TargetServer target, boolean onlyActive) throws C2DMDeviceRegistrationException;
	
	/**
	 * This method updates a registration of a device.
	 * 
	 * @param deviceToRegister		Device data to update.
	 * @return						The registration device object once is update, otherwise
	 * 								null.
	 * @throws C2DMDeviceRegistrationException
	 */
	public PushDevice updateDeviceRegistration(PushDevice deviceToRegister) throws C2DMDeviceRegistrationException;
	
}
