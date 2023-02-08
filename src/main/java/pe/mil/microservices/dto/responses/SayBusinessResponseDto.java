package pe.mil.microservices.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.mil.microservices.utils.dtos.base.BaseBusinessResponseDto;

import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SayBusinessResponseDto extends BaseBusinessResponseDto implements Serializable {
    private static final long serialVersionUID = -7906968285714165295L;
    @JsonbProperty("data")
    @JsonProperty("data")
    SayResponse sayDataResponseDto;
}
