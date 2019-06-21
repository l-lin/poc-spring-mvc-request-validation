package lin.louis.poc.requestvalidation.web.error;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;


public abstract class ErrorDTOBuilder<T extends Throwable> {
	private final T t;

	abstract HttpStatus getStatus();
	abstract Optional<ErrorDTO.ErrorDetailsDTO> buildDetails();

	private ErrorDTOBuilder(T t) {this.t = t;}

	public static <T extends Throwable> ErrorDTOBuilder from(T t) {
		// There should be a better way to do if instead of those ifs... Like using a dico
		if (MethodArgumentNotValidException.class.equals(t.getClass())) {
			return new MethodArgumentNotValidErrorDTOBuilder((MethodArgumentNotValidException) t);
		} else if (ConstraintViolationException.class.equals(t.getClass())) {
			return new ConstraintViolationErrorDTOBuilder((ConstraintViolationException) t);
		} else if (NullPointerException.class.equals(t.getClass())) {
			return new NPEErrorDTOBuilder((NullPointerException) t);
		} else if (BindException.class.equals(t.getClass())) {
			return new BindExceptionErrorDTOBuilder((BindException) t);
		}
		return new DefaultErrorDTOBuilder(t);
	}

	protected T getException() {
		return t;
	}

	public ErrorDTO buildWith(WebRequest webRequest) {
		ErrorDTO dto = new ErrorDTO();
		dto.setTimestamp(ZonedDateTime.now());
		dto.setError(t.getMessage());
		getPath(webRequest).ifPresent(dto::setPath);
		dto.setStatus(getStatus().value());
		buildDetails().ifPresent(dto::setDetails);
		return dto;
	}

	private static Optional<String> getPath(RequestAttributes requestAttributes) {
		return Optional.ofNullable(getAttribute(requestAttributes, "javax.servlet.error.request_uri"));
	}

	private static <T> T getAttribute(RequestAttributes requestAttributes, String name) {
		return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

	private static class DefaultErrorDTOBuilder extends ErrorDTOBuilder<Throwable> {
		private DefaultErrorDTOBuilder(Throwable t) {
			super(t);
		}

		@Override
		HttpStatus getStatus() {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}

		@Override
		Optional<ErrorDTO.ErrorDetailsDTO> buildDetails() {
			return Optional.empty();
		}
	}

	private static class MethodArgumentNotValidErrorDTOBuilder extends ErrorDTOBuilder<MethodArgumentNotValidException> {
		private MethodArgumentNotValidErrorDTOBuilder(MethodArgumentNotValidException t) {
			super(t);
		}

		@Override
		HttpStatus getStatus() {
			return HttpStatus.BAD_REQUEST;
		}

		@Override
		Optional<ErrorDTO.ErrorDetailsDTO> buildDetails() {
			MethodArgumentNotValidException err = getException();
			ErrorDTO.ErrorDetailsDTO dto = new ErrorDTO.ErrorDetailsDTO();
			dto.setFields(err.getBindingResult().getFieldErrors().stream().map(fieldError -> {
				Object rejectedValue = fieldError.getRejectedValue();
				String rejectedValueStr = rejectedValue == null ? "null" : rejectedValue.toString();
				return new ErrorDTO.ErrorDetailDTO(fieldError.getField(), rejectedValueStr, fieldError.getDefaultMessage());
			}).collect(Collectors.toList()));
			return Optional.of(dto);
		}
	}

	private static class BindExceptionErrorDTOBuilder extends ErrorDTOBuilder<BindException> {
		private BindExceptionErrorDTOBuilder(BindException t) {
			super(t);
		}

		@Override
		HttpStatus getStatus() {
			return HttpStatus.BAD_REQUEST;
		}

		@Override
		Optional<ErrorDTO.ErrorDetailsDTO> buildDetails() {
			BindException err = getException();
			ErrorDTO.ErrorDetailsDTO dto = new ErrorDTO.ErrorDetailsDTO();
			dto.setFields(err.getBindingResult().getFieldErrors().stream().map(fieldError -> {
				Object rejectedValue = fieldError.getRejectedValue();
				String rejectedValueStr = rejectedValue == null ? "null" : rejectedValue.toString();
				return new ErrorDTO.ErrorDetailDTO(fieldError.getField(), rejectedValueStr, fieldError.getDefaultMessage());
			}).collect(Collectors.toList()));
			return Optional.of(dto);
		}
	}

	private static class ConstraintViolationErrorDTOBuilder extends ErrorDTOBuilder<ConstraintViolationException> {
		private ConstraintViolationErrorDTOBuilder(ConstraintViolationException e) {
			super(e);
		}

		@Override
		HttpStatus getStatus() {
			return HttpStatus.BAD_REQUEST;
		}

		@Override
		Optional<ErrorDTO.ErrorDetailsDTO> buildDetails() {
			ConstraintViolationException err = getException();
			ErrorDTO.ErrorDetailsDTO dto = new ErrorDTO.ErrorDetailsDTO();
			dto.setFields(err.getConstraintViolations().stream().map(violation -> {
				Object rejectedValue = violation.getInvalidValue();
				String rejectedValueStr = rejectedValue == null ? "null" : rejectedValue.toString();
				return new ErrorDTO.ErrorDetailDTO(violation.getPropertyPath().toString(), rejectedValueStr, violation.getMessage());
			}).collect(Collectors.toList()));
			return Optional.of(dto);
		}
	}

	private static class NPEErrorDTOBuilder extends ErrorDTOBuilder<NullPointerException> {
		private NPEErrorDTOBuilder(NullPointerException e) {
			super(e);
		}

		@Override
		HttpStatus getStatus() {
			return HttpStatus.NOT_FOUND;
		}

		@Override
		Optional<ErrorDTO.ErrorDetailsDTO> buildDetails() {
			return Optional.empty();
		}
	}
}
