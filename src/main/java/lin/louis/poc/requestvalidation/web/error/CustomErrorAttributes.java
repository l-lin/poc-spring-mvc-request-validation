package lin.louis.poc.requestvalidation.web.error;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
	private final ObjectMapper objectMapper;

	public CustomErrorAttributes(ObjectMapper objectMapper) {this.objectMapper = objectMapper;}

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Throwable error = getError(webRequest);

		ErrorDTO dto = ErrorDTOBuilder.from(error).buildWith(webRequest);
		setResponseStatus(webRequest, dto);

		return objectMapper.convertValue(dto, Map.class);
	}

	private void setResponseStatus(WebRequest webRequest, ErrorDTO dto) {
		webRequest.setAttribute("javax.servlet.error.status_code", dto.getStatus(), RequestAttributes.SCOPE_REQUEST);
	}


}
