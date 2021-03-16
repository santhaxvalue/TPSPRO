package com.panelManagement.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfilerSurveyModels implements Serializable {
    private static final long serialVersionUID = 1L;

    int id = 0;
    String name = "";
    int Sequence = 0;
    String PageType = "";
    int mode = 0;
    String ThemeId = "";
    ArrayList<PipinglogicsModel> arrayPipinglogics = null;
    ArrayList<QuestionModel> arrayQuestionModel = null;
    ArrayList<LogicsModel> arrayLogics = null;
    ArrayList<MaskinglogicsModel> arrayMaskinglogics = null;

    public ProfilerSurveyModels(int id, String name, int sequence,
                                String pageType, int mode, String themeId,
                                ArrayList<PipinglogicsModel> arrayPipinglogics,
                                ArrayList<QuestionModel> arrayQuestionModel,
                                ArrayList<LogicsModel> arrayLogics,
                                ArrayList<MaskinglogicsModel> arrayMaskinglogics) {
        this.id = id;
        this.name = name;
        Sequence = sequence;
        PageType = pageType;
        this.mode = mode;
        ThemeId = themeId;
        this.arrayPipinglogics = arrayPipinglogics;
        this.arrayQuestionModel = arrayQuestionModel;
        this.arrayLogics = arrayLogics;
        this.arrayMaskinglogics = arrayMaskinglogics;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSequence() {
        return Sequence;
    }

    public String getPageType() {
        return PageType;
    }

    public int getMode() {
        return mode;
    }

    public String getThemeId() {
        return ThemeId;
    }

    public ArrayList<PipinglogicsModel> getArrayPipinglogics() {
        return arrayPipinglogics;
    }

    public ArrayList<QuestionModel> getArrayQuestionModel() {
        return arrayQuestionModel;
    }

    public ArrayList<LogicsModel> getArrayLogics() {
        return arrayLogics;
    }

    public ArrayList<MaskinglogicsModel> getArrayMaskinglogics() {
        return arrayMaskinglogics;
    }

}
