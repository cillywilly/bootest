package ru.hilo.bootest.Things.internetApplicationAnalysis;

import com.github.javafaker.Faker;
import efr.helpers.Constants;
import io.restassured.RestAssured;
import io.restassured.config.DecoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import ru.hilo.bootest.Things.SearchSomeEntity;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import static efr.helpers.Constants.CIRCUIT;

public class InternetApplicationAnalysisLongGenerator {

    public enum Circuit {
        // DSO
        defb1("defb1efrweb01.corp.dev.vtb/eai_anon_rus/start.swe"),
        defb2("defb2efrweb01.corp.dev.vtb/eai_anon_rus/start.swe"),
        defb3("defb3efrweb01.corp.dev.vtb/eai_anon_rus/start.swe"),
        defb4("defb4efrweb01.corp.dev.vtb/eai_anon_rus/start.swe"),
        dec("decefrweb01.corp.dev.vtb/eai_anon_rus/start.swe");

        public final String URL;

        Circuit(String URL) {
            this.URL = URL;
        }
    }

    public static String generateInternetApplicationAnalysisLong(String contactId, String siteProfileId) {

        Random random = new Random();
        int num = random.nextInt(9000) + 1000;
        String idApplication = SearchSomeEntity.getApplicationId(contactId);
        String rqUID = "AutoTestLong";

        final String URL;
        if (CIRCUIT.URL.contains("testefrapp")) {
            URL = "http://testefrapp.corp.dev.vtb/eai_anon_rus/start.swe?SWEExtSource=AnonWebService&SWEExtCmd=Execute";
        } else {
            URL = Constants.CIRCUIT.URL + "/eai_anon_rus/start.swe?SWEExtSource=AnonWebService&SWEExtCmd=Execute";
        }
        final String FILE_NAME = "src/test/resources/internetApplicationAnalysisXML/long.xml";

        try {
            String xml = FileUtils.readFileToString(new File(FILE_NAME), StandardCharsets.UTF_8);
            Faker faker = new Faker(new Locale("ru"));

            xml = xml.replace("${ContactId}", contactId);
            xml = xml.replace("${SiteProfileId}", siteProfileId);
            xml = xml.replace("${UnifiedApplicationID}", idApplication);
            xml = xml.replace("${RqUID}", rqUID);


            sendRequest(URL, xml);

        } catch (Exception e) {
            e.printStackTrace();
        }

        checkJodStatus(rqUID, siteProfileId);

        String resultStatus = SearchSomeEntity.getJodInternetApplication(rqUID, siteProfileId);
        System.out.println("____________________");
        System.out.println("Статус отработки Джоба длинной анкеты -" + resultStatus);
//        waitingApplicationId(idApplication);


        return siteProfileId;
    }

    private static String sendRequest(String url, String xml) {
        RestAssuredConfig config = RestAssured.config();
        RestAssured.config = config.decoderConfig(DecoderConfig.decoderConfig().noContentDecoders());
        Response response = RestAssured.given().urlEncodingEnabled(false)
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "\"document/http://siebel.com/asi/:InternetApplicationAnalysis\"")
                .body(xml)
                .relaxedHTTPSValidation()
                .when()
                .post(url)
                .then()
                .extract()
                .response();

        return response.body().asString();
    }

    private static void checkJodStatus(String rqUID, String siteProfileId) {
        for (int i = 0; i <= 100; i++) {
            String resultStatus = SearchSomeEntity.getJodInternetApplication(rqUID, siteProfileId);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resultStatus.equals("SUCCESS")) {
                break;
            }
            assert resultStatus.equals("ERROR");
        }
        String resultStatus = SearchSomeEntity.getJodInternetApplication(rqUID, siteProfileId);
        assert !resultStatus.equals("SUCCESS");
    }
//    // Временный костыль по ожиданию апика и прочей хуиты
//    private static void waitingApplicationId(String applicationId){
//        for(int i = 0; i <= 45; i++){
//            String ns = SearchSomeEntity.checkApplicationStage(applicationId);
//            if(ns.equals("АПИБ")){
//                break;
//            }
//            else{
//                WaitHelper.sleep(5);
//            }
//        }
//    }

}
