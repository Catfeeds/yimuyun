package com.yimuyun.lowraiseapp.base.contract.diagnosis;


import com.yimuyun.lowraiseapp.base.BasePresenter;
import com.yimuyun.lowraiseapp.base.BaseView;
import com.yimuyun.lowraiseapp.model.bean.DiagnosisTreatmentVo;
import com.yimuyun.lowraiseapp.model.bean.EquipmentDetailVo;

import java.util.List;

/**
 * @author will on 2017/6/6 21:57
 * @email pengweiqiang64@163.com
 * @description
 * @Version
 */

public interface DiagnosisContract {
    interface View extends BaseView {

        void setLivestockInfo(EquipmentDetailVo equipmentDetailVo);
        void setDiagnosisTreatment(List<DiagnosisTreatmentVo> diagnosisTreatmentVoList);

        void insertDiagnosisTreatmentSuccess();

    }

    interface Presenter extends BasePresenter<View> {
        void getLiveStockInfo(String equipmentId);

        void getDiagnosisTreatment(String equipmentId);
        void insertDiagnosisTreatment(String equipmentId,String personnalId,String treatmentPlanId,
                                      String symptoms,String result,
                                      String dragId,String time);
    }

}
