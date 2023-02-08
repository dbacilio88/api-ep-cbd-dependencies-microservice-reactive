package pe.mil.microservices.controllers.implementations;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.mil.microservices.constants.ProcessConstants;
import pe.mil.microservices.controllers.contracts.IHelloDemoController;
import pe.mil.microservices.dto.requests.HelloRequest;
import pe.mil.microservices.dto.responses.SayBusinessResponseDto;
import pe.mil.microservices.dto.responses.SayResponse;
import pe.mil.microservices.utils.components.enums.ResponseCode;
import pe.mil.microservices.utils.dtos.base.BusinessResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Log4j2
@RestController
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HelloDemoController implements IHelloDemoController {

    private final BusinessResponse businessResponse;
    private final String helloDemoControllerId;

    public HelloDemoController(BusinessResponse businessResponse) {
        this.businessResponse = businessResponse;
        this.helloDemoControllerId = UUID.randomUUID().toString();
        log.debug("helloDemoControllerId {}", helloDemoControllerId);
        log.debug("HelloDemoController loaded successfully");
    }

    @Override
    @PostMapping(path = ProcessConstants.GET_SAY_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> say(Mono<HelloRequest> request) {

        log.debug("method say initialized successfully");
        log.debug("helloDemoControllerId {}", helloDemoControllerId);

        return request
            .subscribeOn(Schedulers.parallel())
            .flatMap(helloRequest ->
                helloRequest.getName().equals("fail")
                    ? Mono.error(new IllegalArgumentException("Error in the request"))
                    : Mono.just(helloRequest))
            .flatMap(helloRequest -> Mono.just(businessResponse
                    .getResponse(SayBusinessResponseDto
                            .builder()
                            .sayDataResponseDto(
                                SayResponse
                                    .builder()
                                    .sayMessage(String.format("Say %s !!!", helloRequest.getName()))
                                    .build()
                            ).build(),
                        ResponseCode.PROCESS_OK.getResponseCodeValue())
                )
            )
            .onErrorResume(throwable -> Mono.just(
                businessResponse
                    .getResponse(
                        ResponseCode.ERROR_PARAMETERS_INVALID.getResponseCodeValue()
                    )
            ))
            .doOnSuccess(
                success -> log.info("finish process say {}", success)
            )
            .doOnError(
                throwable -> log.error("exception error in process say, error: {}", throwable.getMessage())
            )
            ;//.log();
    }
}
