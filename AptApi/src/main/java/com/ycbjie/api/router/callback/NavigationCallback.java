package com.ycbjie.api.router.callback;


import com.ycbjie.api.router.info.Postcard;



public interface NavigationCallback {

    /**
     * 找到跳转页面
     * @param postcard
     */
    void onFound(Postcard postcard);

    /**
     * 未找到
     * @param postcard
     */
    void onLost(Postcard postcard);

    /**
     * 成功跳转
     * @param postcard
     */
    void onArrival(Postcard postcard);


}
