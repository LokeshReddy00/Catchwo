package com.buddies.catchwo.Support;

import java.util.List;

public interface IAllProfLoadList {

    void onAllProfLoadSuccess(List<String> areaNameList);
    void onAllProfLoadFailed(String message);

}
