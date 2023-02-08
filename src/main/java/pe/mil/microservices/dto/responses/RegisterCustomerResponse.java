package pe.mil.microservices.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterCustomerResponse implements Serializable {

    private static final long serialVersionUID = 5230901019196076399L;
    private Long customerId;
    private String name;
    private String description;
}
