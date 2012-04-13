package org.javocsoft.push.srv.android.c2dm.operation.info.type;

/**
 * A C2DM operation info message type.
 * 
 * @author Javier González Serrano
 * @since 13-Oct-2011
 */
public class C2DMInfoData {
	
	public static final int INFO_DATA_SRV_TOKEN=1;
	public static final int INFO_DATA_PUSH_NOT_ID=2;
	public static final String INFO_DATA_PUSH_NOT_INFO_SEPARATOR="<|>";
	
	private int code;
	private String info;
	
	/**
	 * A C2DM operation info message type.
	 * 
	 * @param code
	 * @param info
	 */
	public C2DMInfoData(int code, String info) {
		this.code = code;
		this.info = info;
	}
	
	public C2DMInfoData(int code) {
		this.code = code;
	}
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return this.code+":"+this.info;
	}
	
	
}
