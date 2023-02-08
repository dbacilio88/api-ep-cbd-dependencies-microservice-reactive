package pe.mil.microservices.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class SayResponse implements Serializable {

    private static final long serialVersionUID = -6951550954051995262L;

    @JsonProperty(value = "sayData")
    @JsonbProperty(value = "sayData", nillable = true)
    String sayMessage;
}
