package GPN.Intelligence.Cup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String index() {
        return "Greetings!";
    }

//    @GetMapping("/get")
//    public String getSoap() {
//        String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
//                "  <soap:Body>\n" +
//                "    <Add xmlns=\"http://tempuri.org/\">\n" +
//                "      <intA>int</intA>\n" +
//                "      <intB>int</intB>\n" +
//                "    </Add>\n" +
//                "  </soap:Body>\n" +
//                "</soap:Envelope>";
//
//
//        return "Greetings!";
//    }
}
