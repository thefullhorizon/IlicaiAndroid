package com.ailicai.app.ui.html5;

import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.model.response.Iwjwh5UrlResponse;

/**
 * Created by duo.chen on 2016/6/3.
 */

public class SupportUrl {

    public static void saveUrls(Iwjwh5UrlResponse response) {
        MyPreference.getInstance().write(response);
    }

    public static Iwjwh5UrlResponse getSupportUrlsResponse() {
        Iwjwh5UrlResponse response = MyPreference.getInstance().read(Iwjwh5UrlResponse.class);
        if(response == null) {
            response = new Iwjwh5UrlResponse();
        }
        return response;
    }
}
