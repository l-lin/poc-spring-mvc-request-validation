# Validating request body, request parameters & path variables

## Validation with default validator

Add the annotation `@Validated` in your controller, then you can use the bean validation annotations for your path
variables and request parameters.

Add the annotation `@Valid` in your controller method to validate the request body.

## Validation with custom validator

See the configuration [WebMvcConfig](src/main/java/lin/louis/poc/requestvalidation/requestvalidation/web/WebMvcConfig.java).
