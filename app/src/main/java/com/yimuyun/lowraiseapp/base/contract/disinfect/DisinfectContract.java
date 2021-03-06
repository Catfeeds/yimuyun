package com.yimuyun.lowraiseapp.base.contract.disinfect;


import com.yimuyun.lowraiseapp.base.BasePresenter;
import com.yimuyun.lowraiseapp.base.BaseView;
import com.yimuyun.lowraiseapp.model.bean.UserInfo;

/**
 * @author will on 2017/6/6 21:57
 * @email pengweiqiang64@163.com
 * @description
 * @Version
 */

public interface DisinfectContract {
    interface View extends BaseView {
        void setUserInfo(UserInfo userInfo);
        void insertDisinfectionSuccess();


    }

    interface Presenter extends BasePresenter<View> {
        void getUserInfo(String phoneNumber);
        void insertDisinfection(String disinfectantName,String disinfectionTime,
                                String disinfectionMethod,String enterpriseId,
                                String disinfectionPersonnelId);

    }

}
