package GPN.Intelligence.Cup.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.tempuri.Add;
import org.tempuri.AddResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class SoapToRest extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:add")
                .streamCaching()
                .removeHeaders("CamelHttp*")
                .doTry()
                    .process(exchange -> {
                        Add add = exchange.getIn().getBody(Add.class);
                        List<Object> params = new ArrayList<>();
                        params.add(add.getIntA());
                        params.add(add.getIntB());

                        exchange.getIn().setBody(params);
                    })
                .doCatch(Exception.class, InvalidFormatException.class)
                    .log(LoggingLevel.WARN, "exception")
                    .process(exchange -> {
                        exchange.getIn().setBody("{'error': Bad Request}");
                        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
                        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                    })
                    .to("mock:catch")
                .end()
                .setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.add}}"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
                .to("cxf:bean:cxfCalculator")
                .process(exchange -> {
                    MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
                    AddResponse r = new AddResponse();
                    r.setAddResult((Integer)response.get(0));
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
