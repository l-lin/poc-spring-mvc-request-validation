package lin.louis.poc.requestvalidation.web.validation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;


public class AcceptedValuesValidator implements ConstraintValidator<AcceptedValues, String> {
	private Set<String> fields;

	@Override
	public void initialize(AcceptedValues constraintAnnotation) {
		fields = new HashSet<>(Arrays.asList(constraintAnnotation.fields()));
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (fields.size() <= 1) {
			return true;
		}

		if (!fields.contains(value)) {
			context
					.unwrap(HibernateConstraintValidatorContext.class)
					.addMessageParameter("fields", fields);
			context
					.buildConstraintViolationWithTemplate("{lin.louis.springframework.samples.validation.AcceptedValues.message}")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
