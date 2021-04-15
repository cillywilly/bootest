package ru.hilo.bootest.Things.internetApplicationAnalysis;

import com.github.javafaker.Faker;
import efr.helpers.Constants;
import efr.helpers.FailHelper;
import efr.helpers.WaitHelper;
import efr.helpers.db.SearchSomeEntity;
import efr.stepdefs.helpersteps.StubsSteps;
import io.restassured.RestAssured;
import io.restassured.config.DecoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import static efr.helpers.Constants.CIRCUIT;

public class InternetApplicationAnalysisShortGenerator {

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

    /**
     * отправляет короткую анкету, проверяет что контур нормально ее обработал
     *
     * @param contactId
     * @return
     */
    public static String generateInternetApplicationAnalysisShort(String contactId) {

        Random random = new Random();
        int num = random.nextInt(9000) + 1000;
        String randResult = String.valueOf(num);
        String siteProfileId = contactId + randResult;
        String rqUID = "AutoTestShort";
        System.out.println("ID Профайл - " + siteProfileId);

        final String URL;
        if (CIRCUIT.URL.contains("testefrapp")) {
            URL = "http://testefrapp.corp.dev.vtb/eai_anon_rus/start.swe?SWEExtSource=AnonWebService&SWEExtCmd=Execute";
        } else {
            URL = Constants.CIRCUIT.URL + "/eai_anon_rus/start.swe?SWEExtSource=AnonWebService&SWEExtCmd=Execute";
        }
        final String FILE_NAME = "src/test/resources/internetApplicationAnalysisXML/short.xml";

        try {
            String xml = FileUtils.readFileToString(new File(FILE_NAME), StandardCharsets.UTF_8);
            Faker faker = new Faker(new Locale("ru"));

            xml = xml.replace("${ContactId}", contactId);
            xml = xml.replace("${SiteProfileId}", siteProfileId);
            xml = xml.replace("${RqUID}", rqUID);
            if (!Constants.CIRCUIT.isIntegration) {
                new StubsSteps().createStubGetInternetApplicationAnalysisRs(contactId, "Интернет-анкета.Короткая", siteProfileId);
            }

            sendRequest(URL, xml);

        } catch (Exception e) {
            e.printStackTrace();
        }


        checkJodStatus(rqUID, siteProfileId);

        String resultStatus2 = SearchSomeEntity.getJodInternetApplication(rqUID, siteProfileId);
        System.out.println("Статус отработки Джоба короткой анкеты - " + resultStatus2);
        String resultApplicationStatus = null;
        for (int i = 0; i < 30; i++) {
            resultApplicationStatus = SearchSomeEntity.getApplicationId(contactId);
            if (resultApplicationStatus==null) {
                WaitHelper.sleep(1);
            } else break;
        }
        if (resultApplicationStatus==null) {
            FailHelper.illegalState("Запрос : " +
                    "SELECT ROW_ID FROM SIEBEL.S_OPTY WHERE PR_CON_ID = '" + contactId + "'" +
                    "не дал результатов");
        }
        System.out.println("Заявка - " + resultApplicationStatus);

        // Как приходит коротокая анкета, она автоматом должна перейти в статус "Заведение анкеты"
        // Код который внизу делает проверку на этот статус
        // Это сделано для того, чтобы когда придет длинная анкета, небыло конфликтов со статусом сделки
//        SearchSomeEntity.waitApplication(resultApplicationStatus,"Заведение анкеты");

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
            WaitHelper.sleep(5);
            if (resultStatus.equals("SUCCESS")) {
                break;
            } else if (resultStatus.equals("ERROR")) {
                FailHelper.illegalState("Джоб отработал с ошибкой, для ТИ InternetApplicationAnalysis");
            }
        }
        String resultStatus = SearchSomeEntity.getJodInternetApplication(rqUID, siteProfileId);
        if (!resultStatus.equals("SUCCESS")) {
            FailHelper.illegalState("Джоб отработал с ошибкой, для ТИ InternetApplicationAnalysis его статус = " + resultStatus);
        }
    }
}
