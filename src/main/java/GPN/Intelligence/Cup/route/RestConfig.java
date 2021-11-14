package GPN.Intelligence.Cup.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import org.tempuri.Add;
import org.tempuri.AddResponse;

@Component
public class RestConfig extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .component("servlet")
                .port(8080).bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel2Soap")
                .apiProperty("api.version", "1.0")
                .apiProperty("host", "")
                .enableCORS(true);

        rest("/api").description("Calculator rest service")
                .consumes("application/json")
                .produces("application/json")
                .post("/add").description("Add request")
                    .type(Add.class)
                    .outType(AddResponse.class)
                .to("direct:add");

    }
}