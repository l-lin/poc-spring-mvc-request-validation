package lin.louis.poc.requestvalidation.web.error;

import java.time.ZonedDateTime;
import java.util.List;


public class ErrorDTO {
	private ZonedDateTime timestamp;
	private int status;
	private String error;
	private String path;
	private ErrorDetailsDTO details;

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ErrorDetailsDTO getDetails() {
		return details;
	}

	public void setDetails(ErrorDetailsDTO details) {
		this.details = details;
	}

	public static class ErrorDetailsDTO {
		private List<ErrorDetailDTO> fields;

		public List<ErrorDetailDTO> getFields() {
			return fields;
		}

		public void setFields(List<ErrorDetailDTO> fields) {
			this.fields = fields;
		}
	}

	public static class ErrorDetailDTO {
		private String field;

		private String rejectedValue;

		private String detail;

		public ErrorDetailDTO(String field, String rejectedValue, String detail) {
			this.field = field;
			this.rejectedValue = rejectedValue;
			this.detail = detail;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getRejectedValue() {
			return rejectedValue;
		}

		public void setRejectedValue(String rejectedValue) {
			this.rejectedValue = rejectedValue;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}
	}
}
