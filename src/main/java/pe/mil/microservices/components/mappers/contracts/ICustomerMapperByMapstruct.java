package pe.mil.microservices.components.mappers.contracts;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pe.mil.microservices.dto.requests.RegisterCustomerRequest;
import pe.mil.microservices.repositories.entities.CustomerEntity;

import static pe.mil.microservices.components.mappers.contracts.ICustomerMapperByMapstruct.CustomerFields.*;

@Mapper
public interface ICustomerMapperByMapstruct {

    ICustomerMapperByMapstruct CUSTOMER_DEMO_MAPPER = Mappers
        .getMapper(ICustomerMapperByMapstruct.class);

    /*  @Condition
      default boolean isNotEmpty(String value) {
          return value != null && value.length() > 0;
      }
  */

    @Mapping(source = FIELD_CUSTOMER_ID, target = FIELD_CUSTOMER_ID)
    @Mapping(source = FIELD_CUSTOMER_NAME, target = FIELD_CUSTOMER_NAME)
    @Mapping(source = FIELD_CUSTOMER_DESCRIPTION, target = FIELD_CUSTOMER_DESCRIPTION)
    CustomerEntity mapCustomerEntityByRegisterCustomerRequest(RegisterCustomerRequest source);


    class CustomerFields {
        public static final String FIELD_CUSTOMER_ID = "customerId";
        public static final String FIELD_CUSTOMER_NAME = "name";
        public static final String FIELD_CUSTOMER_DESCRIPTION = "description";
    }
}
