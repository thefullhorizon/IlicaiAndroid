package com.huoqiu.framework.commhttp;

import com.android.volley.VolleyError;

public interface JsonResponsePreProcessor<T> {
    
    /**
     *  @return true - the response pass the pre-process, false the response failed to pass
     */
    boolean preProcess(T response);

    /**
     * pre-process error(something can ban done there like report,print,save)
     */
    void errorPreprocess(VolleyError error);
}
