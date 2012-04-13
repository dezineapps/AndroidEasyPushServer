package org.javocsoft.push.srv.android.c2dm.registration.data;

import java.util.Date;

import org.javocsoft.push.srv.android.c2dm.C2DMConstants.TargetServer;


/**
 * Contains the registration info of a Device.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class PushDevice {
	
	//It is the udid in Apple.
	private String deviceId;
	//It is the token in APNS.
	private String registrationId;	
	private String registrationMail;	
	private Date registrationDate;
	private boolean deviceActive;
	
	private TargetServer targetEnvironment=TargetServer.PRODUCTION;
	
	private String deviceName;
	private String deviceModel;
	private String deviceOS;	
	

	/**
	 * Creates a basic push prepared android device. By default
	 * the devices are put in the PRODUCTION environment. 
	 * {@link TargetServer}
	 * 
	 * @param registrationId
	 * @param registrationMail
	 * @param registrationDate
	 */
	public PushDevice(String registrationId, String registrationMail,
			Date registrationDate) {
		this.registrationId = registrationId;
		this.registrationMail = registrationMail;
		this.registrationDate = registrationDate;
		this.deviceActive=true;
	}

	/**
	 * <p>
	 * Creates a basic push prepared android device. By default
	 * the devices are put in the PRODUCTION environment. 
	 * {@link TargetServer}
	 * 
	 * In this constructor today is assigned as registration date.
	 * </p>
	 * 
	 * @param registrationId
	 * @param registrationMail
	 */
	public PushDevice(String registrationId, String registrationMail) {
		this.registrationId = registrationId;
		this.registrationMail = registrationMail;
		this.registrationDate = new Date();
		this.deviceActive=true;
	}
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public TargetServer getTargetEnvironment() {
		return targetEnvironment;
	}
	public void setTargetEnvironment(TargetServer targetEnvironment) {
		this.targetEnvironment = targetEnvironment;
	}
	
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getRegistrationMail() {
		return registrationMail;
	}
	public void setRegistrationMail(String registrationMail) {
		this.registrationMail = registrationMail;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public boolean isDeviceActive() {
		return deviceActive;
	}
	public void setDeviceActive(boolean deviceActive) {
		this.deviceActive = deviceActive;
	}
	
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceOS() {
		return deviceOS;
	}
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
