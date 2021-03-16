package com.panelManagement.utils;

import com.panelManagement.model.GetLanguageDetailsModel;
import com.panelManagement.model.PanelistIdModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiInterface {

    @POST("GetLanguageDetails")
    Call<GetLanguageDetailsModel> getLanguageDetails(@Body PanelistIdModel panelistIdModel);

}
