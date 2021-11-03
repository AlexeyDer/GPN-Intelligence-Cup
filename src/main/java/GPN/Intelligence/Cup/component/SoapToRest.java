package GPN.Intelligence.Cup.component;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.stereotype.Component;
import org.tempuri.Add;
import org.tempuri.AddResponse;

@Component
public class SoapToRest extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:Add")
                .removeHeaders("CamelHttp*")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Add add = new Add();
                        add.setIntA(Integer.parseInt(exchange.getIn().getHeader("numA").toString()));
                        add.setIntB(Integer.parseInt(exchange.getIn().getHeader("numB").toString()));
                        exchange.getIn().setBody(add);
                    }
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.add}}"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
                .to("cxf:bean:cxfConvertTemp")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
                        AddResponse r = (AddResponse) response.get(0);
                        exchange.getIn().setBody("Result add: "+ r.getAddResult());
                    }
                })
                .to("mock:output");
    }
}
