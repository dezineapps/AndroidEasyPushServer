package org.javocsoft.push.srv.android.c2dm.ack;

import java.util.Date;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;


/**
 * This class holds the ACK data of a read push notification.
 * 
 * @author Javier González Serrano
 * @since  04/11/2011
 *
 */
public class AndroidACK {
	private Long id;

	private String aid;
	private String notificationId;
	private String registrationId;
	private Date receivedOn  = new Date();
	private String timeStamp;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Date getReceivedOn() {
		return receivedOn;
	}

	public void setReceivedOn(Date receivedOn) {
		this.receivedOn = receivedOn;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
		try
        {
	        Date clientDate = DateUtil.parseDate(timeStamp);
	        this.setReceivedOn(clientDate);
        } catch (DateParseException e)
        {
        	//Sate registered will be the date of server.
        }
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AndroidACK that = (AndroidACK) o;
		
		return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
	}

	@Override
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}
	
	@Override
	public String toString()
	{
		return "C2DMEfficacy: notification=" + getNotificationId() + ", aid=" + getAid();
	}
}
