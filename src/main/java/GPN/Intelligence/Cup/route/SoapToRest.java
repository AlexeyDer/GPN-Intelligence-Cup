package GPN.Intelligence.Cup.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.tempuri.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class SoapToRest extends RouteBuilder {

    @Override
    public void configure() {

        getCamelContext().setStreamCaching(true);

        onException(ArithmeticException.class).handled(true)
                .process(exchange -> {
                    exchange.getIn().setBody("{\"error\": \"\"Division by zero is not allowed\"}\"");
                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                })
                .end();

        onException(IllegalArgumentException.class).handled(true)
                .process(exchange -> {
                    exchange.getIn().setBody("{\"error\": \"Bad Request}\"");
                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                })
                .end();

        from("direct:add")
                .removeHeaders("CamelHttp*")
                .process(exchange -> {
                    Add add = exchange.getIn().getBody(Add.class);
                    List<Object> params = new ArrayList<>();
                    params.add(add.getIntA());
                    params.add(add.getIntB());

                    exchange.getIn().setBody(params);
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.add}}"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
                .to("cxf:bean:cxfCalculator")
                .process(exchange -> {
                    MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
                    AddResponse r = new AddResponse();
                    r.setAddResult((Integer) response.get(0));

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    String res = objectMapper.writeValueAsString(r);

                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                    exchange.getIn().setBody(res);
                })
                .unmarshal().json(JsonLibrary.Jackson)
                .to("mock:output");


        from("direct:divide")
                .removeHeaders("CamelHttp*")
                .process(exchange -> {
                    Divide div = exchange.getIn().getBody(Divide.class);
                    List<Object> params = new ArrayList<>();
                    int intA = div.getIntA();
                    int intB = div.getIntB();
                    if (intB != 0) {
                        params.add(intA);
                        params.add(intB);
                        exchange.getIn().setBody(params);
                    } else {
                        throw new ArithmeticException();
                    }
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.divide}}"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
                .to("cxf:bean:cxfCalculator")
                .process(exchange -> {
                    MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
                    DivideResponse r = new DivideResponse();
                    r.setDivideResult((Integer) response.get(0));

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    String res = objectMapper.writeValueAsString(r);

                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                    exchange.getIn().setBody(res);
                })
                .unmarshal().json(JsonLibrary.Jackson)
                .to("mock:output");

        from("direct:multiply")
                .removeHeaders("CamelHttp*")
                .process(exchange -> {
                    Multiply mult = exchange.getIn().getBody(Multiply.class);
                    List<Object> params = new ArrayList<>();
                    params.add(mult.getIntA());
                    params.add(mult.getIntB());
                    exchange.getIn().setBody(params);
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.multiply}}"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
                .to("cxf:bean:cxfCalculator")
                .process(exchange -> {
                    MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
                    MultiplyResponse r = new MultiplyResponse();
                    r.setMultiplyResult((Integer) response.get(0));

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    String res = objectMapper.writeValueAsString(r);

                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                    exchange.getIn().setBody(res);
                })
                .unmarshal().json(JsonLibrary.Jackson)
                .to("mock:output");

        from("direct:subtract")
                .removeHeaders("CamelHttp*")
                .process(exchange -> {
                    Subtract sub = exchange.getIn().getBody(Subtract.class);
                    List<Object> params = new ArrayList<>();
                    params.add(sub.getIntA());
                    params.add(sub.getIntB());
                    exchange.getIn().setBody(params);
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.subtract}}"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
                .to("cxf:bean:cxfCalculator")
                .process(exchange -> {
                    MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
                    SubtractResponse r = new SubtractResponse();
                    r.setSubtractResult((Integer) response.get(0));

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    String res = objectMapper.writeValueAsString(r);

                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                    exchange.getIn().setBody(res);
                })
                .unmarshal().json(JsonLibrary.Jackson)
                .to("mock:output");
    }

}
