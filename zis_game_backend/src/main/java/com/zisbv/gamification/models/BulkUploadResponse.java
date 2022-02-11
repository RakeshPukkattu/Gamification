package com.zisbv.gamification.models;

public class BulkUploadResponse {

	private String code;
	private String success;
	private String total;
	private Boolean errorLogGenereted;

	public BulkUploadResponse() {

	}

	public BulkUploadResponse(String code, String success, String total, Boolean errorLogGenereted) {
		this.code = code;
		this.success = success;
		this.total = total;
		this.errorLogGenereted = errorLogGenereted;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Boolean getErrorLogGenereted() {
		return errorLogGenereted;
	}

	public void setErrorLogGenereted(Boolean errorLogGenereted) {
		this.errorLogGenereted = errorLogGenereted;
	}

}
