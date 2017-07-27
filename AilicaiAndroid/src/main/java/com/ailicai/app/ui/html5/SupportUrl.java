package com.ailicai.app.ui.html5;

import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.model.response.Iwjwh5UrlResponse;
import com.huoqiu.framework.util.ConvertUtil;

/**
 * Created by duo.chen on 2016/6/3.
 */

public class SupportUrl {

    public static String defaultH5UrlResponse = "{\"errorCode\":0,\"message\":\"\",\"bizCode\":0,\"porosWebUrl\":\"https://static.iwlicai.com/licai/index\",\"bankCardUrl\":\"\",\"recommondUrl\":\"https://static.iwlicai.com/licai/recommend\",\"monetaryFundUrl\":\"https://static.iwlicai.com/licai/fund\",\"netLoanUrl\":\"https://static.iwlicai.com/licai/loan\",\"transferUrl\":\"https://static.iwlicai.com/licai/transfer\",\"aboutAiLiCaiUrl\":\"https://static.iwlicai.com/static/know-alc\",\"openAccountUrl\":\"https://static.iwlicai.com/account/password-all\",\"helpCenterUrl\":\"https://m.iwjw.com/help\",\"loanHelpUrl\":\"https://m.iwjw.com/helpRight/6\",\"safecardExplainUrl\":\"https://m.iwlicai.com/app/licai/safecard\",\"supportcardsByAllUrl\":\"https://m.iwlicai.com/app/licai/supportcards\",\"supportcardsByBankUrl\":\"https://m.iwlicai.com/app/licai/supportcards?type=bank\",\"supportcardsByCreditUrl\":\"https://m.iwlicai.com/app/licai/supportcards?type=credit\",\"accountProtocol\":\"https://m.iwjw.com/licai/protocal/eaccount\",\"ailicaiProtocol\":\"https://m.iwjw.com/licai/protocal/service\",\"cardUrl\":\"https://static.iwlicai.com/voucher/readapp/card/\",\"tradeEnsureCardUrl\":\"https://m.iwjw.com/card/servicecard\",\"ailicaiUrl\":\"https://m.iwlicai.com/app/licai/licaiindex\",\"aboutUrl\":\"https://m.iwjw.com/about/\",\"alicaiType\":1,\"rentHouseCommissionUrl\":\"https://m.iwjw.com/jiaizhai/chuzumanager\",\"rentOrderDetailH5Url\":\"https://m.iwjw.com/myorder#!/rent_detail\",\"rentBillDetailH5Url\":\"https://m.iwjw.com/myorder#!/rent_bill\",\"payRentH5Url\":\"https://m.iwjw.com/myorder#!/payrent?_router=server\",\"payRentBillDetailH5Url\":\"https://m.iwjw.com/myorder#!/payrent_bill\",\"tiyanbaoDetailUrl\":\"https://m.iwjw.com/licai/index#!/tiyanbao\",\"productDetailUrl\":\"https://m.iwjw.com/licai/index#!/detail\",\"rebateUrl\":\"voucher/rebate-detail\",\"brandRentUrl\":\"\",\"brandShareUrl\":\"https://mrent.iwjw.com/static/free-commission\",\"h5AccountUrl\":\"https://m.iwjw.com/account/password-all\",\"bindNewOpenAccountUrl\":\"https://static.iwlicai.com/account/id-binded\",\"inviteUrl\":\"https://m.iwlicai.com/invite_activity\"}";

    public static void saveUrls(Iwjwh5UrlResponse response) {
        MyPreference.getInstance().write(response);
    }

    public static Iwjwh5UrlResponse getSupportUrlsResponse() {
        Iwjwh5UrlResponse response = MyPreference.getInstance().read(Iwjwh5UrlResponse.class);
        if(response == null) {
            if(AILICAIBuildConfig.isProduction()) {
                response = generateDefaultUrlResponse();
            } else {
                response = new Iwjwh5UrlResponse();
            }
        }
        return response;
    }

    public static Iwjwh5UrlResponse generateDefaultUrlResponse() {
        Iwjwh5UrlResponse response = null;
        try {
            response = ConvertUtil.json2Obj(defaultH5UrlResponse,Iwjwh5UrlResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Iwjwh5UrlResponse();
        }
        return response;
    }
}
