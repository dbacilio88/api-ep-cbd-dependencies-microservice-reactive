package pe.mil.microservices.components.validations;

import pe.mil.microservices.components.enums.CustomerValidationResult;
import pe.mil.microservices.dto.requests.RegisterCustomerRequest;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface ICustomerRegisterValidation
    extends Function<RegisterCustomerRequest, CustomerValidationResult> {

    static ICustomerRegisterValidation isICustomerNameValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getName() != null
                && !registerCustomerRequest.getName().isEmpty()
                && !registerCustomerRequest.getName().isBlank()

                ? CustomerValidationResult.CUSTOMER_VALID
                : CustomerValidationResult.INVALID_CUSTOMER_NAME;
    }

    static ICustomerRegisterValidation isICustomerIdValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getCustomerId() != null
                ? CustomerValidationResult.CUSTOMER_VALID
                : CustomerValidationResult.INVALID_CUSTOMER_ID;
    }

    static ICustomerRegisterValidation isICustomerDescriptionValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getDescription() != null
                && !registerCustomerRequest.getDescription().isBlank()
                && !registerCustomerRequest.getDescription().isEmpty()
                ? CustomerValidationResult.CUSTOMER_VALID
                : CustomerValidationResult.INVALID_CUSTOMER_DESCRIPTION;
    }

    static ICustomerRegisterValidation customValidation(Predicate<RegisterCustomerRequest> validate) {
        return registerCustomerRequest -> validate.test(registerCustomerRequest)
            ? CustomerValidationResult.CUSTOMER_VALID
            : CustomerValidationResult.INVALID_CUSTOMER_NAME;
    }

    default ICustomerRegisterValidation and(ICustomerRegisterValidation andValidation) {
        return registerCustomerRequest -> {
            CustomerValidationResult validation = this.apply(registerCustomerRequest);
            return validation.equals(CustomerValidationResult.CUSTOMER_VALID)
                ? andValidation.apply(registerCustomerRequest)
                : validation;
        };
    }

    default ICustomerRegisterValidation or(ICustomerRegisterValidation orValidation) {
        return registerCustomerRequest -> {
            CustomerValidationResult validation = this.apply(registerCustomerRequest);
            return validation.equals(CustomerValidationResult.CUSTOMER_VALID)
                ? orValidation.apply(registerCustomerRequest)
                : validation;
        };
    }
}
