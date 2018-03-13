package id.co.integravity.gasstation.util.api;

/**
 * Created by user on 15/02/2018.
 */

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost. hanya untuk emulator
    //192.168.100.18 127.0.0.1
    public static final String BASE_URL_API = "http://sevianapungkyb.000webhostapp.com/api/";
    //192.168.100.18
    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
