package pe.mil.microservices.services.contracts;

import pe.mil.microservices.dto.Customer;
import pe.mil.microservices.dto.requests.RegisterCustomerRequest;
import pe.mil.microservices.utils.dtos.process.BusinessProcessResponse;
import pe.mil.microservices.utils.service.interfaces.*;
import reactor.core.publisher.Mono;

public interface ICustomerDemoServices
    extends
    IGetDomainEntityById<Mono<BusinessProcessResponse>, Long>,
    IGetAllDomainEntity<Mono<BusinessProcessResponse>>,
    ISaveDomainEntity<Mono<BusinessProcessResponse>, Mono<RegisterCustomerRequest>>,
    IUpdateDomainEntity<Mono<BusinessProcessResponse>, Mono<RegisterCustomerRequest>>,
    IDeleteDomainEntity<Customer>{
}
