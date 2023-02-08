package pe.mil.microservices.controllers.contracts;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.mil.microservices.constants.ProcessConstants;
import pe.mil.microservices.dto.requests.HelloRequest;
import reactor.core.publisher.Mono;

@Validated
@RequestMapping(
    path = ProcessConstants.MICROSERVICE_HELLO_PATH,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public interface IHelloDemoController {

    Mono<ResponseEntity<Object>> say(@RequestBody Mono<HelloRequest> request);
}
