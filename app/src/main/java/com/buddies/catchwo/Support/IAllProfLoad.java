package com.buddies.catchwo.Support;

import com.buddies.catchwo.Model.ProfModel;

import java.util.List;

public interface IAllProfLoad {

    void onAllProfLoadsSuccess(List<ProfModel> areaNameList);
    void onAllProfLoadsFailed(String message);

}
