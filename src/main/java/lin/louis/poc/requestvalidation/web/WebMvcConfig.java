package lin.louis.poc.requestvalidation.web;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public Validator getValidator() {
		// This validator is used when validating request body DTOs
		return validator();
	}

	@Bean
	LocaleResolver localeResolver() {
		// Force english for Spring Security error messages
		return new FixedLocaleResolver(Locale.ENGLISH);
	}

	/**
	 * We have to declare our own validator to add our custom message interpolation.
	 *
	 * See https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-custom-message-interpolation
	 * @return the bean validation validator
	 */
	@Bean
	LocalValidatorFactoryBean validator() {
		// This validator is used when validating services annoted by @Validated, e.g. controllers to check PathVariables
		try (LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean()) {
			MessageSource messageSource = messageSource();
			factory.setValidationMessageSource(messageSource);
			factory.setMessageInterpolator(
					new ResourceBundleMessageInterpolator(
							new MessageSourceResourceBundleLocator(messageSource)
					));
			return factory;
		}
	}

	@Bean
	MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
		// Validator to validate query parameters & path variables
		methodValidationPostProcessor.setValidatorFactory((LocalValidatorFactoryBean) validator);
		return methodValidationPostProcessor;
	}

	private MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("classpath:lin/louis/poc/requestvalidation/validation/ValidationMessages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
		return messageSource;
	}
}
