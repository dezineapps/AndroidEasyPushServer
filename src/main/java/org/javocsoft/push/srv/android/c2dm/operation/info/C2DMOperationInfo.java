package org.javocsoft.push.srv.android.c2dm.operation.info;

import java.util.ArrayList;
import java.util.List;

import org.javocsoft.push.srv.android.c2dm.exceptions.C2DMException;
import org.javocsoft.push.srv.android.c2dm.operation.info.type.C2DMExceptionData;
import org.javocsoft.push.srv.android.c2dm.operation.info.type.C2DMInfoData;


/**
 * Class that will hold operation data including the result.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMOperationInfo {
	
	public static final String C2DM_SRV_REGISTER="RegisterServer";
	public static final String C2DM_SRV_RETRIEVE_TOKEN="RetrieveServerToken";
	public static final String C2DM_SEND_MSG="SendMessage";
	
	private String operation;
	private ArrayList<C2DMInfoData> info;
	private ArrayList<C2DMExceptionData> exceptions;
	
	/**
	 * 
	 * @param operation
	 */
	public C2DMOperationInfo(String operation) {
		this.operation = operation;
		info=new ArrayList<C2DMInfoData>();
		exceptions=new ArrayList<C2DMExceptionData>();
	}
	
	/**
	 * Allows to add a non exception message.
	 * 
	 * @param code
	 * @param infoData
	 */
	public void addInfo(int code,String infoData){
		C2DMInfoData c2dmInfoData=new C2DMInfoData(code, infoData);
		info.add(c2dmInfoData);
	}
	
	/**
	 * Allows to add an exception message.
	 * 
	 * @param code
	 * @param exceptionData
	 * @param exception
	 */
	public void addException(int code,String exceptionData, C2DMException exception){
		C2DMExceptionData c2dmExceptionData=new C2DMExceptionData(code, exceptionData, exception);
		exceptions.add(c2dmExceptionData);
	}
	
	//Getters & Setters
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List<C2DMInfoData> getInfoList() {
		return info;
	}

	public String[] getInfo() {
		String[] res=new String[info.size()];
		for(int i=0;i<info.size();i++){
			res[i]=((C2DMInfoData)info.get(i)).toString();
		}
		return res;
	}
	
	public void setInfo(ArrayList<C2DMInfoData> info) {
		this.info = info;
	}
	
	public void setExceptions(ArrayList<C2DMExceptionData> exceptions) {
		this.exceptions = exceptions;
	}
	
	public List<C2DMExceptionData> getExceptionsList() {
		return exceptions;
	}

	public String[] getExceptions() {
		String[] res=new String[exceptions.size()];
		for(int i=0;i<exceptions.size();i++){
			res[i]=((C2DMExceptionData)exceptions.get(i)).toString();
		}
		return res;
	}
	
	/**
	 * Returns the C2DM Server Authorization token from
	 * the list of info items.
	 * 
	 * @return	The token or null if there is none.
	 */
	public String getC2DMSrvToken(){
		String res=null;
		for(C2DMInfoData iData:info){
			if(iData.getCode()==C2DMInfoData.INFO_DATA_SRV_TOKEN){
				res=iData.getInfo();
			}
		}
		return res;
	}
	
	/**
	 * This method gets the sent push notifications info in
	 * a list.
	 * 
	 * @return	An array string containing the list of sent push notifications info.
	 */
	public String[] getC2DMSentPushNotifications(){
		String[] res=new String[info.size()];
		int count=0;
		for(C2DMInfoData iData:info){
			if(iData.getCode()==C2DMInfoData.INFO_DATA_PUSH_NOT_ID){
				res[count++]=iData.getInfo();
			}
		}
		return res;
	}
}
