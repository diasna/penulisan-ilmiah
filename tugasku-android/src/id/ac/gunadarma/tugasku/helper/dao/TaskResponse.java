package id.ac.gunadarma.tugasku.helper.dao;

public class TaskResponse {
	public String id;
	public String statusCode;
	public String statusMessage;
	@Override
	public String toString() {
		return "TaskResponse [id=" + id + ", statusCode=" + statusCode
				+ ", statusMessage=" + statusMessage + "]";
	}
	
}
