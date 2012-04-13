package org.javocsoft.push.srv.android.c2dm.json.exceptions;

/**
 * 
 * @author Javier González Serrano
 * @since 19-Oct-2011
 */
public class JSONException extends Exception{
	
	public JSONException(){
		super();
	}
	
	public JSONException(Throwable cause){
		super(cause);
	}

	public JSONException(String msg){
		super(msg);
	}

	public JSONException(String msg,Throwable cause){
		super(msg,cause);
	}
	
}