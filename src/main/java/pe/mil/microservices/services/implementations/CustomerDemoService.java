package pe.mil.microservices.services.implementations;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pe.mil.microservices.components.enums.CustomerValidationResult;
import pe.mil.microservices.components.mappers.contracts.ICustomerMapperByMapstruct;
import pe.mil.microservices.components.validations.ICustomerRegisterValidation;
import pe.mil.microservices.dto.Customer;
import pe.mil.microservices.dto.requests.RegisterCustomerRequest;
import pe.mil.microservices.dto.responses.RegisterCustomerResponse;
import pe.mil.microservices.repositories.contracts.ICustomerDemoRepository;
import pe.mil.microservices.repositories.entities.CustomerEntity;
import pe.mil.microservices.services.contracts.ICustomerDemoServices;
import pe.mil.microservices.utils.components.enums.ResponseCode;
import pe.mil.microservices.utils.components.exceptions.CommonBusinessProcessException;
import pe.mil.microservices.utils.components.helpers.ObjectMapperHelper;
import pe.mil.microservices.utils.dtos.generics.GenericBusinessResponse;
import pe.mil.microservices.utils.dtos.process.BusinessProcessResponse;
import reactor.core.publisher.Mono;

import java.util.*;

@Log4j2
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerDemoService implements ICustomerDemoServices {

    private final ICustomerDemoRepository customerRepository;
    private final String customerDemoServiceId;


    @Autowired
    public CustomerDemoService(
        final ICustomerDemoRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
        customerDemoServiceId = UUID.randomUUID().toString();
        log.debug("customerDemoServiceId {}", customerDemoServiceId);
        log.debug("CustomerService loaded successfully");
    }

    @Override
    public Mono<BusinessProcessResponse> getById(Long id) {

        log.info("this is in services getById demo method");
        log.debug("customerDemoServiceId {}", customerDemoServiceId);

        GenericBusinessResponse<Customer> genericMessagesBusinessResponse = new GenericBusinessResponse<>();

        return Mono
            .just(genericMessagesBusinessResponse)
            .flatMap(generic -> {
                // description process: data to customer repository, using ObjectMapperHelper
                // find entity by id
                final Optional<CustomerEntity> entity = this.customerRepository.findById(id);
                // validate entity is empty
                if (entity.isEmpty()) {
                    // case is empty return CommonBusinessProcessException with response code ERROR_IN_REQUESTED_DATA
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                } else {
                    // return customer entity
                    return Mono.just(entity.get());
                }
            })
            .flatMap(entity -> {
                // using ObjectMapperHelper to transform entity to dto
                final Customer target = ObjectMapperHelper.map(entity, Customer.class);
                // return GenericMessagesBusinessResponse
                log.info("customer {} ", target.toString());
                GenericBusinessResponse<Customer> data = new GenericBusinessResponse<>(target);
                return Mono.just(data);
            })
            .flatMap(response -> {
                //return BusinessProcessResponse transform to BusinessProcessResponse
                return Mono.just(BusinessProcessResponse.setEntitySuccessfullyResponse(response));
            })
            .doOnSuccess(success ->
                log.info("finish process getById, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Mono<BusinessProcessResponse> getAll() {

        log.info("this is in services getAll demo method");
        log.debug("customerDemoServiceId {}", customerDemoServiceId);
        GenericBusinessResponse<List<Customer>> genericMessagesBusinessResponse = new GenericBusinessResponse<>();

        return Mono.just(genericMessagesBusinessResponse)
            // description process: data to customer repository, using ObjectMapperHelper
            .flatMap(generic -> {
                generic.setData(ObjectMapperHelper.mapAll(Lists.newArrayList(this.customerRepository.findAll()), Customer.class));
                return Mono.just(generic);
            })
            .flatMap(response -> {
                log.info("response {} ", response.getData().toString());
                //return BusinessProcessResponse transform to BusinessProcessResponse
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(response));
            }).flatMap(process -> Mono.just(BusinessProcessResponse.setEntitySuccessfullyResponse(process.getBusinessResponse())))
            .doOnSuccess(success ->
                log.info("finish process getById, success: {}", success.toString())
            )
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Iterable<Mono<BusinessProcessResponse>> getAllEntities() {
        return new ArrayList<>();
    }

    @Override
    public Mono<BusinessProcessResponse> save(Mono<RegisterCustomerRequest> entity) {

        log.info("this is in services save demo method");
        log.debug("customerDemoServiceId {}", customerDemoServiceId);

        return entity
            .flatMap(create -> {
                // validate request
                log.debug("this is in services save demo method");

                create.setName("");

                final CustomerValidationResult result =
                    ICustomerRegisterValidation
                        .isICustomerIdValidation()
                        .and(ICustomerRegisterValidation.isICustomerNameValidation())
                        .and(ICustomerRegisterValidation.isICustomerDescriptionValidation())
                        .apply(create);
                // validate if request is valid
                log.info("result {} ", result);
                if (!CustomerValidationResult.CUSTOMER_VALID.equals(result)) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }

                return Mono.just(create);
            })
            .flatMap(request -> {
                // demo set to orm entity to save
                log.debug("log in flatMap context #2");
                // create demo entity to save
                final CustomerEntity save =
                    ICustomerMapperByMapstruct.CUSTOMER_DEMO_MAPPER.mapCustomerEntityByRegisterCustomerRequest(request);
                // validate result
                log.info("customer entity {} ", save);

                boolean exists = this.customerRepository.existsByCustomerId(save.getCustomerId());
                log.info("customer exists entity {} ", exists);

                if (exists) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }

                final CustomerEntity saved = this.customerRepository.save(save);
                log.info("customer saved entity {} ", saved);

                if (Objects.isNull(saved.getCustomerId())) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }

                return Mono.just(saved);
            })
            .flatMap(customer -> {
                log.info("customer customer entity {} ", customer);
                final RegisterCustomerResponse response = ObjectMapperHelper
                    .map(customer, RegisterCustomerResponse.class);
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(new GenericBusinessResponse<>(response)));
            })
            .doOnSuccess(success ->
                log.info("finish process save, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Boolean delete(Customer entity) {

        log.debug("this is in services delete demo method");
        log.debug("customerDemoServiceId {}", customerDemoServiceId);

        final Optional<CustomerEntity> result = this.customerRepository.findById(entity.getCustomerId());

        if (result.isEmpty()) {
            return false;
        }

        this.customerRepository.delete(result.get());

        return true;
    }

    @Override
    public Mono<BusinessProcessResponse> update(Mono<RegisterCustomerRequest> entity) {

        log.info("this is in services update demo method");
        log.debug("customerDemoServiceId {}", customerDemoServiceId);

        return entity.
            flatMap(update -> {
                log.debug("this is in services update demo method");
                final CustomerValidationResult result = ICustomerRegisterValidation
                    .isICustomerNameValidation().apply(update);
                // validate if request is valid
                if (!CustomerValidationResult.CUSTOMER_VALID.equals(result)) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(update);
            })
            .flatMap(request -> {
                // demo set to orm entity to update
                log.debug("log in flatMap context #2");
                // create demo entity to update
                final CustomerEntity update = ICustomerMapperByMapstruct
                    .CUSTOMER_DEMO_MAPPER
                    .mapCustomerEntityByRegisterCustomerRequest(request);

                final CustomerEntity updated = this.customerRepository.save(update);

                if (Objects.isNull(updated.getCustomerId())) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(updated);

            })
            .flatMap(customer -> {
                final RegisterCustomerResponse response = ObjectMapperHelper
                    .map(customer, RegisterCustomerResponse.class);
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(new GenericBusinessResponse<>(response)));
            }).doOnSuccess(success ->
                log.info("finish process save, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }
}
