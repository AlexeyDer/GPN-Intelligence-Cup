package GPN.Intelligence.Cup.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import org.tempuri.*;

@Component
public class RestConfig extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .component("servlet").host("0.0.0.0").port(8080).bindingMode(RestBindingMode.auto).scheme("http")
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel2Soap")
                .apiProperty("api.version", "1.0")
                .apiProperty("host","");

        rest("/api").description("Calculator rest service")
                .consumes("application/json")
                .produces("application/json")
                .post("/add")
                    .description("Adds two integers")
                    .type(Add.class)
                    .outType(AddResponse.class)
                .to("direct:add")
                .post("/divide")
                    .description("Divide two integers")
                    .type(Divide.class)
                    .outType(DivideResponse.class)
                .to("direct:divide")
                .post("/multiply")
                    .description("Multiply two integers")
                    .type(Multiply.class)
                    .outType(MultiplyResponse.class)
                .to("direct:multiply")
                .post("/subtract")
                    .description("Subtract two integers")
                    .type(Subtract.class)
                    .outType(SubtractResponse.class)
                .to("direct:subtract");


    }
}