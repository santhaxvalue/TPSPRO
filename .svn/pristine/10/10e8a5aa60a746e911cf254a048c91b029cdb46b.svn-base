package com.panelManagement.controllers;

import com.panelManagement.listener.OnQuestionType;

public enum QuestionTypeId implements OnQuestionType {

    OPENTEXTSINGLELINE(1) {
        @Override
        public int onAccessRevoked() {
            return ONE;
        }
    },
    OPENTEXTMULTILINE(2) {
        @Override
        public int onAccessRevoked() {
            return TWO;
        }
    },
    NUMERIC(3) {
        @Override
        public int onAccessRevoked() {
            return THREE;
        }
    },
    DATE(4) {
        @Override
        public int onAccessRevoked() {
            return FOUR;
        }
    },
    INFO(5) {
        @Override
        public int onAccessRevoked() {
            return FIVE;
        }
    },
    SINGLECHOICERADIOBUTTON(6) {
        @Override
        public int onAccessRevoked() {
            return SIX;
        }
    },
    SIGNLECHOICEDROPDOWN(7) {
        @Override
        public int onAccessRevoked() {
            return SEVEN;
        }
    },
    MULTICHOICE(8) {
        @Override
        public int onAccessRevoked() {
            return EIGHT;
        }
    },
    GRIDTYPE(9) {
        @Override
        public int onAccessRevoked() {
            return NINE;
        }
    };

    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private int value;

    private QuestionTypeId(int value) {
        this.value = value;
    }

    public static QuestionTypeId getValue(int questionTypeId) {
        if (questionTypeId == OPENTEXTSINGLELINE.onAccessRevoked())
            return QuestionTypeId.OPENTEXTSINGLELINE;
        else if (questionTypeId == OPENTEXTMULTILINE.onAccessRevoked())
            return QuestionTypeId.OPENTEXTMULTILINE;
        else if (questionTypeId == NUMERIC.onAccessRevoked())
            return QuestionTypeId.NUMERIC;
        else if (questionTypeId == DATE.onAccessRevoked())
            return QuestionTypeId.DATE;
        else if (questionTypeId == INFO.onAccessRevoked())
            return QuestionTypeId.INFO;
        else if (questionTypeId == SINGLECHOICERADIOBUTTON.onAccessRevoked())
            return QuestionTypeId.SINGLECHOICERADIOBUTTON;
        else if (questionTypeId == SIGNLECHOICEDROPDOWN.onAccessRevoked())
            return QuestionTypeId.SIGNLECHOICEDROPDOWN;
        else if (questionTypeId == MULTICHOICE.onAccessRevoked())
            return QuestionTypeId.MULTICHOICE;
        else if (questionTypeId == GRIDTYPE.onAccessRevoked())
            return QuestionTypeId.GRIDTYPE;
        return null;
    }

}
