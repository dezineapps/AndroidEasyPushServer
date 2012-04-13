package org.javocsoft.push.srv.android.c2dm.operation.info.type;

/**
 * A C2DM operation exception message type.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMExceptionData {
	private int code;
	private String info;
	private Exception exception;
	
	/**
	 * A C2DM operation exception message type.
	 * 
	 * @param code
	 * @param info
	 * @param exception
	 */
	public C2DMExceptionData(int code, String info, Exception exception) {
		this.code = code;
		this.info = info;
		this.exception = exception;
	}
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getInfo() {
		if(exception!=null){
			return info+"["+exception.getMessage()+"]";
		}else{
			return info;
		}
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	
	@Override
	public String toString() {
		StringBuffer res=new StringBuffer();
		res.append(this.code);
		if(this.info!=null){
			res.append(":").append(this.info);
		}
		if(this.exception!=null){
			res.append("[").append(this.exception.getMessage()).append("]");
		}
		return res.toString();
	}
	
	
}
