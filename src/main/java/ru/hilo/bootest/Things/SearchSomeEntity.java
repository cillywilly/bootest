package ru.hilo.bootest.Things;

public class SearchSomeEntity {

    /**
     * Проверка статуса джоба Интернет анкеты
     */
    public static String getJodInternetApplication(String guid, String siteProfileId) {
        String sql = "SELECT STATUS FROM SIEBEL.S_SRM_REQUEST " +
                "WHERE DESC_TEXT = 'Создание ИА. GUID: " + guid + ". SiteProfileId: " + siteProfileId + "'";
        String result = SqlHelper.SIEBEL.select(sql, "NULL");
        return result;
    }

}
