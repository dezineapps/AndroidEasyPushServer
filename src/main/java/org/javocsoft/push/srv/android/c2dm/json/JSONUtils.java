package org.javocsoft.push.srv.android.c2dm.json;


import java.io.StringWriter;

import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.badgerfish.BadgerFishXMLStreamWriter;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;
import org.javocsoft.push.srv.android.c2dm.registration.RegistrationInterface;
import org.javocsoft.push.srv.android.c2dm.registration.data.PushDevice;



/**
 * 
 * @author Javier González Serrano
 * @since 19-Oct-2011
 *
 */
public class JSONUtils {
	
	public static final int JSON_MODE_MAPPED=1;
	public static final int JSON_MODE_BADGERFISH=2;
	
	public static final int MESSAGE_TYPE_INFO=1;
	public static final int MESSAGE_TYPE_ERROR=2;
	
	/**
	 * Gets a JSON formatted text from the given message and type.
	 * 
	 * @param jsonMode			JSON mode. {@link JSONUtils#JSON_MODE_MAPPED}, {@link JSONUtils#JSON_MODE_BADGERFISH}
	 * @param messageType		Message type. {@link JSONUtils#MESSAGE_TYPE_INFO}, {@link JSONUtils#MESSAGE_TYPE_ERROR}
	 * @param registrationId	The registration id of this device.
	 * @param message			Message
	 * @return Message
	 */
	public static String getJSONStringMessage(int jsonMode, int messageType, String registrationId, String message){
		
		String res=null;
		
		StringWriter strWriter = new StringWriter();		
		AbstractXMLStreamWriter w = null;
		switch(jsonMode){
			case JSON_MODE_MAPPED:
				 // Mapped convention
		        MappedNamespaceConvention con = new MappedNamespaceConvention();
		        w = new MappedXMLStreamWriter(con, strWriter);
				break;
			case JSON_MODE_BADGERFISH:
				 // BadgerFish convention
		        w = new BadgerFishXMLStreamWriter(strWriter);
				break;
		}   
		
		switch(messageType){
			case MESSAGE_TYPE_INFO:
				res=getJSONStringInfo(strWriter,w,jsonMode,registrationId,message);
				break;
			case MESSAGE_TYPE_ERROR:
				res=getJSONStringError(strWriter,w,jsonMode,registrationId,message);
				break;
		}   
		    
	    return res;
	}
	
	/**
	 * Returns the information of a device in JSON format.
	 * 
	 * @param jsonMode		JSON mode. {@code JSON_MODE_MAPPED}, {@code JSON_MODE_BADGERFISH} 
	 * @param pushDevice	The device.
	 * @return	The device info in JSON format. Example: 
	 */
	public static String getJSONStringFromDeviceInfo(int jsonMode, PushDevice pushDevice){
		String res=null;
		
		StringWriter strWriter = new StringWriter();		
		AbstractXMLStreamWriter w = null;
		switch(jsonMode){
			case JSON_MODE_MAPPED:
				 // Mapped convention
		        MappedNamespaceConvention con = new MappedNamespaceConvention();
		        w = new MappedXMLStreamWriter(con, strWriter);
				break;
			case JSON_MODE_BADGERFISH:
				 // BadgerFish convention
		        w = new BadgerFishXMLStreamWriter(strWriter);
				break;
		}       
		
        try{
	        w.writeStartDocument();
	        
	        w.writeStartElement("OK");	        
	        w.writeStartElement("registeredDevice");	        
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_DEVICEID);
	        w.writeCharacters(pushDevice.getDeviceId());
	        w.writeEndElement();
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_REGISTRATIONID);
	        w.writeCharacters(pushDevice.getRegistrationId());
	        w.writeEndElement();
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_REGISTRATIONMAIL);
	        w.writeCharacters(pushDevice.getRegistrationMail());
	        w.writeEndElement();
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_DEVICEMODEL);
	        w.writeCharacters(pushDevice.getDeviceModel());
	        w.writeEndElement();
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_DEVICENAME);
	        w.writeCharacters(pushDevice.getDeviceName());
	        w.writeEndElement();
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_DEVICEOS);
	        w.writeCharacters(pushDevice.getDeviceOS());
	        w.writeEndElement();
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_DEVICEACTIVE);
	        w.writeCharacters(Boolean.toString(pushDevice.isDeviceActive()));
	        w.writeEndElement();	        
	        w.writeEndElement();	        
	        w.writeEndElement();	        
	        w.writeEndDocument();
	
	        w.close();
	        strWriter.close();
	        res=strWriter.toString();
        }catch(Exception e){
        	res="{\"OK\":{\"registeredDevice\":"+"{"+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_DEVICEID+"\":\""+pushDevice.getDeviceId()+"\","+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_REGISTRATIONID+"\":\""+pushDevice.getRegistrationId()+"\","+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_REGISTRATIONMAIL+"\":\""+pushDevice.getRegistrationMail()+"\","+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_DEVICEMODEL+"\":\""+pushDevice.getDeviceModel()+"\","+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_DEVICENAME+"\":\""+pushDevice.getDeviceName()+"\","+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_DEVICEOS+"\":\""+pushDevice.getDeviceOS()+"\","+
        	"\""+RegistrationInterface.REGISTRATION_PARAM_DEVICEACTIVE+"\":\""+pushDevice.isDeviceActive()+"\""+
        	"}}}";
        }
        
        return res;
	}
	
	
	
	
	
	/*
	 * @param strWriter
	 * @param w
	 * @param jsonMode			JSON mode. {@code JSON_MODE_MAPPED}, {@code JSON_MODE_BADGERFISH} 
	 * @param registrationId	The registration id of this device.
	 * @param message			Message.
	 * @return Error
	 */
	private static String getJSONStringError(StringWriter strWriter, AbstractXMLStreamWriter w, int jsonMode, String registrationId, String message){
		String res=null;
		try{
	        w.writeStartDocument();	        
	        w.writeStartElement("ERROR");	        	        
	        w.writeStartElement("message");
	        w.writeCharacters(message);
	        w.writeEndElement(); 
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_REGISTRATIONID);
	        w.writeCharacters(registrationId);
	        w.writeEndElement();	        
	        w.writeEndElement();	        
	        w.writeEndDocument();
	
	        w.close();
	        strWriter.close();
	        res=strWriter.toString();
		}catch(Exception e){
			res="{\"ERROR\":{\"message\":\""+message+"\"}}";
		}
        
        return res;
	}
	
	/*
	 * @param strWriter
	 * @param w 
	 * @param jsonMode			JSON mode. {@code JSON_MODE_MAPPED}, {@code JSON_MODE_BADGERFISH} 
	 * @param registrationId	The registration id of this device.
	 * @param message			Message.
	 * @return					The message in JSON format. Example: 
	 */
	private static String getJSONStringInfo(StringWriter strWriter, AbstractXMLStreamWriter w, int jsonMode, String registrationId, String message){
		String res=null;
		
		try{
	        w.writeStartDocument();	        
	        w.writeStartElement("OK");	        	        
	        w.writeStartElement("message");
	        w.writeCharacters(message);
	        w.writeEndElement(); 
	        w.writeStartElement(RegistrationInterface.REGISTRATION_PARAM_REGISTRATIONID);
	        w.writeCharacters(registrationId);
	        w.writeEndElement();	        
	        w.writeEndElement();	        
	        w.writeEndDocument();
	
	        w.close();
	        strWriter.close();
	        res=strWriter.toString();
	        
		}catch(Exception e){
			res="{\"OK\":{\"message\":\""+message+"\"}}";
		}
        
        return res;
	}
	
	
}

