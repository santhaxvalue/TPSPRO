package com.panelManagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.panelManagement.model.AnswerChoiceModel;
import com.panelManagement.model.GlobalSettingsModel;
import com.panelManagement.model.LogicsModel;
import com.panelManagement.model.MaskinglogicsModel;
import com.panelManagement.model.PagesModel;
import com.panelManagement.model.PipinglogicsModel;
import com.panelManagement.model.QuestionModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/*
 * Its a singletone database adaptor class.  how to use it.
 *
 * From any of your activity create object of this class and use that object to get and insert data
 *
 *  Example : DBAdapter database = DBAdapter.getDBAdapter(this);
 *
 * @auther Manoj Prasad
 */
@SuppressWarnings("serial")
public class DBAdapter implements Serializable {

    public static final String DATABASENAME = "tpssurvey.db";
    public static final String DATABASE_TABLE_GLOBALSETTING = "globalSetting";
    public static final String DATABASE_TABLE_PAGES = "pages";
    public static final String DATABASE_TABLE_QUESTIONIERS = "questioniers";
    public static final String DATABASE_TABLE_ANSWERCHOICE = "answers";
    public static final String DATABASE_TABLE_PIPING = "piping";
    public static final String DATABASE_TABLE_MASKING = "masking";
    public static final String DATABASE_TABLE_LOGICS = "skiplogic";
    public static final String DATABASE_TABLE_METERING_LOG = "meteringlog";
    public static final String CAMPAIGNINID = "campaignid";
    public static final String CAMPAIGNNAME = "campaignname";
    // Global settings declaration
    public static final String PAGESID = "pageid";
    public static final String PAGENAME = "pagename";
    // Global settings declaration
    public static final String IPLOGGING = "ipLogging";
    public static final String CAPTCHA = "capcha";
    public static final String XGEOLOCKING = "xgeolocking";
    public static final String GLOBALSETTINGNAME = "globalsettingname";
    public static final String SEQUENCE = "sequence";
    public static final String VISIBLE = "visible";
    public static final String QUESTIONID = "questionid";
    public static final String PAGENO = "pageno";
    public static final String MAXLENGTH = "maxlength";
    public static final String MANDATORY = "mandatory";
    public static final String QUESTIONTYPEID = "questionTypeid";
    public static final String QUESTIONTEXT = "questiontext";
    public static final String ISACTIVE = "isactive";
    public static final String USERANSWER = "useranswer";
    public static final String ANSWERID = "answerid";
    public static final String ANSWERCHOICETEXT = "answertext";
    public static final String ISCHECKED = "ischecked";
    public static final String ISMASKINGAPPLIED = "ismaskingapplied";
    public static final String ACTION = "actions";
    public static final String SKIPQUESTIONNAME = "skipname";
    public static final String CONDITIONIN_QUESTIONID = "condiquestionid";
    public static final String CONDITIONIN_OPERATOR = "condioperator";
    public static final String CONDITIONIN_JOIN = "jointext";
    public static final String CONDITIONIN_ANSWERID = "answerid";
    public static final String CREATEDDATE = "createddate";
    public static final String MODIFIEDDATE = "modifieddate";
    public static final String PIPINGID = "id";
    public static final String PIPINGNAME = "name";
    public static final String PIPINGSOURCEID = "source";
    public static final String PIPINGTARGETID = "target";
    public static final String Metering_LOGTIME = "logtime";
    public static final String MASKINGID = "id";
    public static final String MASKINGNAME = "name";
    public static final String MASKINGSOURCEID = "source";
    public static final String MASKINGTARGETID = "target";
    public static final String MASKINGTARGETANSWER = "answer";
    public static final String MASKINGSOURCEANSWERID = "sourceanswerid";
    public static final String ISNONEDITABLE = "isnoneditable";
    public static final String MAPPEDQUESTION = "mappedquestionid";
    public static final String ISNONEOFTHEABOVE = "IsNoneoftheAbove";
    private static final int DATABASE_VERSION = 4;
    private static final String VALIDATION = "validation";
    private static final String LOGICID = "logicid";
    private static SQLiteDatabase db = null;
    private static DBAdapter mInstance;
    private DatabaseHelper dbHelper = null;

    public DBAdapter() {
    }

    public DBAdapter(Context ctx) {
        dbHelper = new DatabaseHelper(ctx);
        open();
    }

    public static DBAdapter getDBAdapter(Context ctx) {
        if (mInstance == null) {
            mInstance = new DBAdapter(ctx);
        }
        return mInstance;
    }

    public DBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        if (db != null && db.isOpen()) {
            dbHelper.close();
            db = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }





    public boolean clearLog() {
        try {
            db.delete(DATABASE_TABLE_METERING_LOG, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }






    // Pages Models
    public boolean insertPagesModel(PagesModel data) {
        try {
            if (!updatePagesModel(data)) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(CAMPAIGNINID, data.getCampaignId());
                initialValues.put(PAGESID, data.getPagesId());
                initialValues.put(PAGENAME, data.getName());
                db.insert(DATABASE_TABLE_PAGES, null, initialValues);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updatePagesModel(PagesModel data) {
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d'", CAMPAIGNINID,
                    data.getCampaignId(), PAGESID, data.getPagesId());
            ContentValues initialValues = new ContentValues();
            initialValues.put(PAGENAME, data.getName());
            return db.update(DATABASE_TABLE_PAGES, initialValues, WHERE, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<PagesModel> getPagesByCampaignID(String campaignid) {
        ArrayList<PagesModel> model = new ArrayList<PagesModel>();
        String WHERE = "";
        if (campaignid != null)
            WHERE = String.format("%s='%s'", CAMPAIGNINID, campaignid);
        else
            WHERE = null;
        Cursor cursor = db.query(DATABASE_TABLE_PAGES, new String[]{}, WHERE,
                null, null, null, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                model.add(new PagesModel(cursor.getInt(cursor
                        .getColumnIndex(PAGESID)), cursor.getString(cursor
                        .getColumnIndex(CAMPAIGNINID)), cursor.getString(cursor
                        .getColumnIndex(PAGENAME))));
            }
        }
        cursor.close();
        return model;
    }

    public String getPagesIdByName(String campaignid, int pageid) {
        String WHERE = String.format("%s='%s' AND %s='%s' AND pagename IN('ThankYou','Thank You','Terminated' , 'Terminate')", CAMPAIGNINID, campaignid, PAGESID, pageid);

        Cursor cursor = db.query(DATABASE_TABLE_PAGES, new String[]{}, WHERE,
                null, null, null, null);
        String pagename = "";
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                pagename = cursor.getString(cursor.getColumnIndex(PAGENAME));
            }
        }
        cursor.close();
        return pagename;
    }



    // Questioniers
    public boolean insertQuestioniers(QuestionModel data) {
        try {
            if (!updateQuestioniers(data)) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(CAMPAIGNINID, data.getCampaignId());
                initialValues.put(PAGENO, data.getPageNo());
                initialValues.put(PAGESID, data.getPageId());
                initialValues.put(MAXLENGTH, data.getMaxLength());
                initialValues.put(MANDATORY, data.isMandatory());
                initialValues.put(ISNONEDITABLE, data.isIsnoneditable() ? 1 : 0);
                initialValues.put(QUESTIONTEXT, data.getQuestionText());
                initialValues.put(VALIDATION, data.getValidation());
                initialValues.put(QUESTIONID, data.getQuestionId());
                initialValues.put(QUESTIONTYPEID, data.getQuestionTypeId());
                initialValues.put(USERANSWER, data.getuserAnswer());
                initialValues.put(CREATEDDATE, data.getCreatedDate());
                initialValues.put(MODIFIEDDATE, data.getModifiedDate());
                initialValues.put(ISACTIVE, data.isActive());
                db.insert(DATABASE_TABLE_QUESTIONIERS, null, initialValues);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateQuestioniers(QuestionModel data) {
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s'",
                    CAMPAIGNINID, data.getCampaignId(), PAGESID,
                    data.getPageId(), QUESTIONID, data.getQuestionId());
            ContentValues initialValues = new ContentValues();
            initialValues.put(MAXLENGTH, data.getMaxLength());
            initialValues.put(MANDATORY, data.isMandatory());
            initialValues.put(ISNONEDITABLE, data.isIsnoneditable() ? 1 : 0);
            initialValues.put(QUESTIONTEXT, data.getQuestionText());
            initialValues.put(VALIDATION, data.getValidation());
            initialValues.put(QUESTIONID, data.getQuestionId());
            initialValues.put(QUESTIONTYPEID, data.getQuestionTypeId());
            initialValues.put(USERANSWER, data.getuserAnswer());
            initialValues.put(CREATEDDATE, data.getCreatedDate());
            initialValues.put(MODIFIEDDATE, data.getModifiedDate());
            initialValues.put(ISACTIVE, data.isActive());
            return db.update(DATABASE_TABLE_QUESTIONIERS, initialValues, WHERE,
                    null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public ArrayList<QuestionModel> getQuestionsOnPage(String campaignid, int pageid) {

        ArrayList<QuestionModel> model = new ArrayList<QuestionModel>();
        String WHERE = "";
        if (pageid == 0)
            WHERE = String.format("%s='%s'", CAMPAIGNINID, campaignid);
        else if (campaignid != null)
            WHERE = String.format(Locale.US, "%s='%s' AND %s='%d'", CAMPAIGNINID,
                    campaignid, PAGESID, pageid);
        else
            WHERE = null;
        Cursor cursor = db.query(DATABASE_TABLE_QUESTIONIERS, new String[]{},
                WHERE, null, null, null, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                model.add(new QuestionModel(cursor.getString(cursor
                        .getColumnIndex(CAMPAIGNINID)), cursor.getInt(cursor
                        .getColumnIndex(PAGENO)), cursor.getInt(cursor
                        .getColumnIndex(PAGESID)), cursor.getString(cursor
                        .getColumnIndex(QUESTIONID)), cursor.getString(cursor
                        .getColumnIndex(MAXLENGTH)), cursor.getString(cursor
                        .getColumnIndex(VALIDATION)), cursor.getInt(cursor
                        .getColumnIndex(MANDATORY)), cursor.getInt(cursor
                        .getColumnIndex(ISNONEDITABLE)) == 1, cursor.getInt(cursor
                        .getColumnIndex(QUESTIONTYPEID)), cursor
                        .getString(cursor.getColumnIndex(QUESTIONTEXT)), cursor
                        .getString(cursor.getColumnIndex(USERANSWER)), cursor
                        .getInt(cursor.getColumnIndex(ISACTIVE)) == 1, cursor.getString(cursor
                        .getColumnIndex(CREATEDDATE)), cursor.getString(cursor
                        .getColumnIndex(MODIFIEDDATE))));
            }
        }
        cursor.close();
        return model;
    }

    public QuestionModel getQuestionsByTargetId(String campaignid, int pageid, int questionid) {
        QuestionModel model = null;
        String WHERE = null;
        if (pageid == -1)
            WHERE = String.format(Locale.US, "%s='%s'  AND %s='%d'",
                    CAMPAIGNINID, campaignid, QUESTIONID,
                    questionid);
        else
            WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%d'",
                    CAMPAIGNINID, campaignid, PAGESID, pageid, QUESTIONID,
                    questionid);

        Cursor cursor = db.query(DATABASE_TABLE_QUESTIONIERS, new String[]{},
                WHERE, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            model = new QuestionModel(cursor.getString(cursor
                    .getColumnIndex(CAMPAIGNINID)), cursor.getInt(cursor
                    .getColumnIndex(PAGENO)), cursor.getInt(cursor
                    .getColumnIndex(PAGESID)), cursor.getString(cursor
                    .getColumnIndex(QUESTIONID)), cursor.getString(cursor
                    .getColumnIndex(MAXLENGTH)), cursor.getString(cursor
                    .getColumnIndex(VALIDATION)), cursor.getInt(cursor
                    .getColumnIndex(MANDATORY)), cursor.getInt(cursor
                    .getColumnIndex(ISNONEDITABLE)) == 1, cursor.getInt(cursor
                    .getColumnIndex(QUESTIONTYPEID)), cursor.getString(cursor
                    .getColumnIndex(QUESTIONTEXT)), cursor.getString(cursor
                    .getColumnIndex(USERANSWER)), cursor.getInt(cursor
                    .getColumnIndex(ISACTIVE)) == 1,
                    cursor.getString(cursor.getColumnIndex(CREATEDDATE)),
                    cursor.getString(cursor.getColumnIndex(MODIFIEDDATE)));
        }
        cursor.close();
        return model;
    }

    public int getPageNoByAction(String campaignid, int pageid) {
        String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d'", CAMPAIGNINID,
                campaignid, PAGESID, pageid);
        Cursor cursor = db.query(DATABASE_TABLE_QUESTIONIERS, new String[]{},
                WHERE, null, null, null, null);
        int pageno = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            pageno = cursor.getInt(cursor.getColumnIndex(PAGENO));
        }
        cursor.close();
        return pageno;
    }

    public boolean updateUserAnswer(String campaignId, String questionid, String value) {
        try {
            String WHERE = String.format("%s='%s' AND %s='%s'", CAMPAIGNINID,
                    campaignId, QUESTIONID, questionid);
            ContentValues initialValues = new ContentValues();
            initialValues.put(USERANSWER, value);
            return db.update(DATABASE_TABLE_QUESTIONIERS, initialValues, WHERE,
                    null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    // AnswerChoice
    public boolean insertAnswersChoice(AnswerChoiceModel data) {
        try {
            if (!updateAnswersChoice(data)) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(CAMPAIGNINID, data.getCampaignId());
                initialValues.put(QUESTIONID, data.getQuestionId());
                initialValues.put(ANSWERID, data.getAnswerID());
                initialValues.put(ANSWERCHOICETEXT, data.getAnswerChoiceText());
                initialValues.put(ISNONEOFTHEABOVE, data.isIsNoneoftheAbove() ? 1 : 0);
                initialValues.put(ISCHECKED, data.isChecked() ? 1 : 0);
                initialValues.put(ISMASKINGAPPLIED, data.getMaskingActive());
                initialValues.put(ISACTIVE, data.isIsActive());
                db.insert(DATABASE_TABLE_ANSWERCHOICE, null, initialValues);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateAnswersChoice(AnswerChoiceModel data) {
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s'",
                    CAMPAIGNINID, data.getCampaignId(), QUESTIONID,
                    data.getQuestionId(), ANSWERID, data.getAnswerID());
            ContentValues initialValues = new ContentValues();
            initialValues.put(ANSWERCHOICETEXT, data.getAnswerChoiceText());
            initialValues.put(ISACTIVE, data.isIsActive());
            initialValues.put(ISNONEOFTHEABOVE, data.isIsNoneoftheAbove() ? 1 : 0);
            initialValues.put(ISMASKINGAPPLIED, data.getMaskingActive());
            initialValues.put(ISCHECKED, data.isChecked() ? 1 : 0);
            return db.update(DATABASE_TABLE_ANSWERCHOICE, initialValues, WHERE, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<AnswerChoiceModel> getAnswerChoice(String campaignId, String questionId, int type) {
        ArrayList<AnswerChoiceModel> optionsAnswers = new ArrayList<AnswerChoiceModel>();
        String WHERE = null;
        if (type == 1) {
            WHERE = String.format(Locale.US, "%s='%s' AND %s='%s' AND %s='%d'",
                    CAMPAIGNINID, campaignId, QUESTIONID, questionId,
                    ISCHECKED, type);
        } else {
            WHERE = String.format(Locale.US, "%s='%s' AND %s='%s' AND %s='%d'",
                    CAMPAIGNINID, campaignId, QUESTIONID, questionId,
                    ISMASKINGAPPLIED, type);
        }

        Cursor cursor = db.query(DATABASE_TABLE_ANSWERCHOICE, new String[]{},
                WHERE, null, null, null, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                optionsAnswers
                        .add(new AnswerChoiceModel(
                                cursor.getString(cursor
                                        .getColumnIndex(CAMPAIGNINID)),
                                cursor.getString(cursor
                                        .getColumnIndex(ANSWERCHOICETEXT)),
                                0,
                                "",
                                false,
                                false,
                                0,
                                "",
                                "",
                                cursor.getInt(cursor.getColumnIndex(ISNONEOFTHEABOVE)) == 1,
                                cursor.getInt(cursor.getColumnIndex(ISACTIVE)) == 1, "", "", cursor
                                .getString(cursor
                                        .getColumnIndex(ANSWERID)),
                                Integer.parseInt(cursor.getString(cursor
                                        .getColumnIndex(QUESTIONID))), Integer
                                .parseInt(cursor.getString(cursor
                                        .getColumnIndex(ISCHECKED))),
                                cursor.getInt(cursor
                                        .getColumnIndex(ISMASKINGAPPLIED))));
            }
        }
        cursor.close();
        return optionsAnswers;
    }

    public boolean updateMaskingOnAnswers(AnswerChoiceModel data) {
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s'",
                    CAMPAIGNINID, data.getCampaignId(), QUESTIONID,
                    data.getQuestionId(), ANSWERID, data.getAnswerID());
            ContentValues initialValues = new ContentValues();
            initialValues.put(ISMASKINGAPPLIED, data.getMaskingActive());
            return db.update(DATABASE_TABLE_ANSWERCHOICE, initialValues, WHERE,
                    null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // skip logic tables
    public boolean insertSkipLogic(LogicsModel data) {

        try {
            if (!updateSkipLogic(data)) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(CAMPAIGNINID, data.getCampaignId());
                initialValues.put(PAGESID, data.getPagesId());
                initialValues.put(ACTION, data.getAction());
                initialValues.put(SKIPQUESTIONNAME, data.getName());
                initialValues.put(LOGICID, data.getLogicId());
                initialValues.put(CONDITIONIN_QUESTIONID,
                        data.getLookqustionid());
                initialValues.put(CONDITIONIN_OPERATOR, data.getOperator());
                initialValues.put(CONDITIONIN_JOIN, data.getJoin());
                initialValues.put(MAPPEDQUESTION, data.getMaskedQuestionId());
                initialValues.put(CONDITIONIN_ANSWERID, data.getAnswerid());

                db.insert(DATABASE_TABLE_LOGICS, null, initialValues);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateSkipLogic(LogicsModel data) {
        try { // jayesh changed string identifier
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%d' AND %s='%s'", CAMPAIGNINID,
                            data.getCampaignId(), PAGESID, data.getPagesId(),
                            LOGICID, data.getLogicId(), CONDITIONIN_ANSWERID, data.getAnswerid());
            ContentValues initialValues = new ContentValues();
            initialValues.put(ACTION, data.getAction());
            initialValues.put(SKIPQUESTIONNAME, data.getName());
            initialValues.put(LOGICID, data.getLogicId());
            initialValues.put(CONDITIONIN_QUESTIONID, data.getLookqustionid());
            initialValues.put(CONDITIONIN_OPERATOR, data.getOperator());
            initialValues.put(MAPPEDQUESTION, data.getMaskedQuestionId());
            initialValues.put(CONDITIONIN_JOIN, data.getJoin());
            return db.update(DATABASE_TABLE_LOGICS, initialValues, WHERE, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<LogicsModel> getskipLogics(String campaignId, int pageid) {
        ArrayList<LogicsModel> mLogicListbygroup = new ArrayList<LogicsModel>();
        String
                WHERE = String.format(Locale.US, "%s='%s' AND %s='%d'", CAMPAIGNINID,
                campaignId, PAGESID, pageid);
        Cursor cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                null, CONDITIONIN_QUESTIONID, null, CONDITIONIN_QUESTIONID + " DESC");
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                mLogicListbygroup.add(new LogicsModel(
                        cursor.getString(cursor
                                .getColumnIndex(CAMPAIGNINID)),
                        cursor.getInt(cursor.getColumnIndex(PAGESID)),
                        cursor.getInt(cursor.getColumnIndex(ACTION)),
                        cursor.getInt(cursor.getColumnIndex(LOGICID)),
                        cursor.getString(cursor
                                .getColumnIndex(SKIPQUESTIONNAME)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_QUESTIONID)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_OPERATOR)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_JOIN)),
                        cursor.getString(cursor
                                .getColumnIndex(MAPPEDQUESTION)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_ANSWERID))));
            }
        }
        cursor.close();
        return mLogicListbygroup;
    }

    //select  * from skiplogic where campaignid='15458318' AND pageid='1' group by condiquestionid;

    public LogicsModel getskipLogicForCurrentPage(String campaignId, int pageid, String questionid) {
        String
                WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s'", CAMPAIGNINID,
                campaignId, PAGESID, pageid, CONDITIONIN_QUESTIONID, questionid);
        Cursor cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                null, CONDITIONIN_QUESTIONID, null, null);
        LogicsModel optionsAnswers = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            optionsAnswers = new LogicsModel(
                    cursor.getString(cursor
                            .getColumnIndex(CAMPAIGNINID)),
                    cursor.getInt(cursor.getColumnIndex(PAGESID)),
                    cursor.getInt(cursor.getColumnIndex(ACTION)),
                    cursor.getInt(cursor.getColumnIndex(LOGICID)),
                    cursor.getString(cursor
                            .getColumnIndex(SKIPQUESTIONNAME)),
                    cursor.getString(cursor
                            .getColumnIndex(CONDITIONIN_QUESTIONID)),
                    cursor.getString(cursor
                            .getColumnIndex(CONDITIONIN_OPERATOR)),
                    cursor.getString(cursor
                            .getColumnIndex(CONDITIONIN_JOIN)),
                    cursor.getString(cursor
                            .getColumnIndex(MAPPEDQUESTION)),
                    cursor.getString(cursor
                            .getColumnIndex(CONDITIONIN_ANSWERID)));
        }
        cursor.close();
        return optionsAnswers;
    }

    public List<LogicsModel> getskipLogicForCurrentPageList(String campaignId, int pageid, String questionid) {
        List<LogicsModel> model = new ArrayList<LogicsModel>();
        String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s'", CAMPAIGNINID,
                campaignId, PAGESID, pageid, CONDITIONIN_QUESTIONID, questionid);
        Cursor cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                null, null, null, null);
        LogicsModel optionsAnswers = null;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                optionsAnswers = new LogicsModel(
                        cursor.getString(cursor
                                .getColumnIndex(CAMPAIGNINID)),
                        cursor.getInt(cursor.getColumnIndex(PAGESID)),
                        cursor.getInt(cursor.getColumnIndex(ACTION)),
                        cursor.getInt(cursor.getColumnIndex(LOGICID)),
                        cursor.getString(cursor
                                .getColumnIndex(SKIPQUESTIONNAME)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_QUESTIONID)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_OPERATOR)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_JOIN)),
                        cursor.getString(cursor
                                .getColumnIndex(MAPPEDQUESTION)),
                        cursor.getString(cursor
                                .getColumnIndex(CONDITIONIN_ANSWERID)));
                model.add(optionsAnswers);
            }
        }
        cursor.close();
        return model;
    }





    public int getConditionQueryCount(String campaignId, int pageid, String questionid, String useranswer, String JoinType) {
        int count = -1;
        String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s' AND %s='%s' AND skipname NOT IN('Thank you','Terminate')", CAMPAIGNINID,
                campaignId, PAGESID, pageid, CONDITIONIN_QUESTIONID, questionid, CONDITIONIN_ANSWERID, useranswer.trim());
        Cursor cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                null, null, null, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getConditionQueryCount(QuestionModel questionModel, String JoinType) {
        int count = -1;
        Cursor cursor = null;
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s' AND %s='%s' AND skipname NOT IN('Thank you','Terminate')", CAMPAIGNINID,
                    questionModel.getCampaignId(), PAGESID, questionModel.getPageId(), CONDITIONIN_QUESTIONID, questionModel.getQuestionId(), CONDITIONIN_ANSWERID, questionModel.getuserAnswer().trim());
            cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                    null, null, null, null);
            count = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return count;
    }

    public int getConditionQueryCount(QuestionModel questionModel, String JoinType, String queryOperator) {
        int count = -1;
        Cursor cursor = null;
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%s' AND %s" + queryOperator + "'%s' AND skipname NOT IN('Thank you','Terminate')", CAMPAIGNINID,
                    questionModel.getCampaignId(), PAGESID, questionModel.getPageId(), CONDITIONIN_QUESTIONID, questionModel.getQuestionId(), CONDITIONIN_ANSWERID, questionModel.getuserAnswer().trim());
            cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                    null, null, null, null);
            count = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return count;
    }

    public int generateMultiConditionQuery(QuestionModel questionModel, ArrayList<AnswerChoiceModel> answerlist, String operator, int actions, String QueryType) {
        int count = -1;
        Cursor cursor = null;
        try {
            //is equal to
            // generate format
            StringBuffer queryFormatGenerator = new StringBuffer();
            StringBuffer queryGenerator = new StringBuffer();
            queryFormatGenerator.append(" AND " + CONDITIONIN_ANSWERID + " " + QueryType + " (");
            String tokan = "";

            for (Iterator iterator = answerlist.iterator(); iterator.hasNext(); ) {
                AnswerChoiceModel answerChoiceModel = (AnswerChoiceModel) iterator.next();
                if (answerChoiceModel.isChecked()) {
                    queryGenerator.append(tokan + answerChoiceModel.getAnswerID());
                    tokan = ",";
                }
            }
            if (queryGenerator.toString().trim().equalsIgnoreCase("")) {
                queryFormatGenerator = new StringBuffer();
            } else {
                queryFormatGenerator.append(queryGenerator);
                queryFormatGenerator.append(")");
            }

		/*String  WHERE = String.format("%s='%s' AND %s='%s' "+queryFormatGenerator, CAMPAIGNINID,
				questionModel.getCampaignId(),  QUESTIONID, questionModel.getQuestionId());*/
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%s' AND %s='%s' AND %s='%d' " + queryFormatGenerator, CAMPAIGNINID,
                    questionModel.getCampaignId(), PAGESID, questionModel.getPageId(), CONDITIONIN_QUESTIONID, questionModel.getQuestionId(), ACTION, actions);


            cursor = db.query(DATABASE_TABLE_LOGICS, new String[]{}, WHERE,
                    null, null, null, null);
            count = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return count;
    }



    // piping tables
    public boolean insertPipingLogic(PipinglogicsModel data) {

        try {
            if (!updatePipingLogic(data)) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(CAMPAIGNINID, data.getCampaignid());
                initialValues.put(PAGESID, data.getPageid());
                initialValues.put(PIPINGID, data.getPipingid());
                initialValues.put(PIPINGNAME, data.getPipingName());
                initialValues.put(PIPINGSOURCEID, data.getSourceid());
                initialValues.put(PIPINGTARGETID, data.getTargetid());

                db.insert(DATABASE_TABLE_PIPING, null, initialValues);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updatePipingLogic(PipinglogicsModel data) {
        try {
            String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d' AND %s='%d'",
                    CAMPAIGNINID, data.getCampaignid(), PAGESID,
                    data.getPageid(), PIPINGID, data.getSourceid());
            ContentValues initialValues = new ContentValues();
            initialValues.put(PIPINGNAME, data.getPipingName());
            initialValues.put(PIPINGSOURCEID, data.getSourceid());
            initialValues.put(PIPINGTARGETID, data.getTargetid());
            return db.update(DATABASE_TABLE_PIPING, initialValues, WHERE, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // piping tables
    public boolean insertMaskingLogic(MaskinglogicsModel data) {

        try {
            if (!updateMaskingLogic(data)) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(CAMPAIGNINID, data.getCampaignid());
                initialValues.put(PAGESID, data.getPageid());
                initialValues.put(MASKINGID, data.getMaskingid());
                initialValues.put(MASKINGNAME, data.getMaskingName());
                initialValues.put(MASKINGSOURCEID, data.getSourceid());
                initialValues.put(MASKINGSOURCEANSWERID,
                        data.getSourceAnswerId());
                initialValues.put(MASKINGTARGETID, data.getTargetid());
                initialValues
                        .put(MASKINGTARGETANSWER, data.getTargetAnswerId());
                db.insert(DATABASE_TABLE_MASKING, null, initialValues);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateMaskingLogic(MaskinglogicsModel data) {
        try { // jayesh changed string identifier
            String WHERE = String.format(Locale.US,
                    "%s='%s' AND %s='%d' AND %s='%d' AND %s='%s'",
                    CAMPAIGNINID, data.getCampaignid(), PAGESID,
                    data.getPageid(), MASKINGID, data.getMaskingid(),
                    MASKINGTARGETANSWER, data.getTargetAnswerId());
            ContentValues initialValues = new ContentValues();
            initialValues.put(MASKINGNAME, data.getMaskingName());
            initialValues.put(MASKINGSOURCEID, data.getSourceid());
            initialValues.put(MASKINGSOURCEANSWERID, data.getSourceAnswerId());
            initialValues.put(MASKINGTARGETID, data.getTargetid());
            return db.update(DATABASE_TABLE_MASKING, initialValues, WHERE, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<MaskinglogicsModel> getMaskingLogicsData(
            String campaignId, String string) {

        ArrayList<MaskinglogicsModel> optionsAnswers = new ArrayList<MaskinglogicsModel>();
        String WHERE = "";
        if (campaignId != null)
            WHERE = String.format("%s='%s' AND %s='%s'", CAMPAIGNINID,
                    campaignId, MASKINGSOURCEANSWERID, string);
        else
            WHERE = null;
        Cursor cursor = db.query(DATABASE_TABLE_MASKING, new String[]{},
                WHERE, null, null, null, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                optionsAnswers.add(new MaskinglogicsModel(cursor
                        .getString(cursor.getColumnIndex(CAMPAIGNINID)), cursor
                        .getInt(cursor.getColumnIndex(PAGESID)), cursor
                        .getInt(cursor.getColumnIndex(MASKINGID)), cursor
                        .getString(cursor.getColumnIndex(MASKINGNAME)), cursor
                        .getInt(cursor.getColumnIndex(MASKINGSOURCEID)),
                        cursor.getString(cursor
                                .getColumnIndex(MASKINGSOURCEANSWERID)),
                        cursor.getInt(cursor.getColumnIndex(MASKINGTARGETID)),
                        cursor.getString(cursor
                                .getColumnIndex(MASKINGTARGETANSWER))));
            }
        }
        cursor.close();
        return optionsAnswers;
    }

    public ArrayList<PipinglogicsModel> getPipingLogicsData(String campaignId,
                                                            int pageid) {

        ArrayList<PipinglogicsModel> optionsAnswers = new ArrayList<PipinglogicsModel>();
        String WHERE = String.format(Locale.US, "%s='%s' AND %s='%d'", CAMPAIGNINID,
                campaignId, PAGESID, pageid);

        Cursor cursor = db.query(DATABASE_TABLE_PIPING, new String[]{}, WHERE,
                null, null, null, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                optionsAnswers.add(new PipinglogicsModel(cursor
                        .getString(cursor.getColumnIndex(CAMPAIGNINID)), cursor
                        .getInt(cursor.getColumnIndex(PIPINGID)), cursor
                        .getInt(cursor.getColumnIndex(PAGESID)), cursor
                        .getString(cursor.getColumnIndex(PIPINGNAME)), cursor
                        .getInt(cursor.getColumnIndex(PIPINGSOURCEID)), cursor
                        .getInt(cursor.getColumnIndex(PIPINGTARGETID))));
            }
        }
        cursor.close();
        return optionsAnswers;
    }

    public void deleteAllRecords() {
        db.delete(DATABASE_TABLE_GLOBALSETTING, null, null);
        db.delete(DATABASE_TABLE_ANSWERCHOICE, null, null);
        db.delete(DATABASE_TABLE_LOGICS, null, null);
        db.delete(DATABASE_TABLE_MASKING, null, null);
        db.delete(DATABASE_TABLE_PAGES, null, null);
        db.delete(DATABASE_TABLE_PIPING, null, null);
        db.delete(DATABASE_TABLE_QUESTIONIERS, null, null);
            }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASENAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {

                final String globalsetting = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_GLOBALSETTING + "(" + CAMPAIGNINID
                        + "  integer, " + CAMPAIGNNAME + " text, " + IPLOGGING
                        + " text, " + CAPTCHA + " text, " + XGEOLOCKING
                        + " text, " + GLOBALSETTINGNAME + " text, " + SEQUENCE
                        + " text, " + VISIBLE + " text);";
                db.execSQL(globalsetting);

                final String pages = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_PAGES + "(" + CAMPAIGNINID
                        + "  text, " + PAGESID + " integer, " + PAGENAME
                        + " text);";
                db.execSQL(pages);

                final String questioniers = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_QUESTIONIERS + "(" + CAMPAIGNINID
                        + "  text, " + PAGENO + " integer, " + PAGESID
                        + " integer, " + QUESTIONID + " integer, " + MAXLENGTH
                        + " text, " + VALIDATION + " text, " + MANDATORY
                        + " text, " + ISNONEDITABLE + " integer default 0, "
                        + QUESTIONTYPEID + " integer, "
                        + QUESTIONTEXT + " text, " + USERANSWER + " text, "
                        + CREATEDDATE + " text, " + MODIFIEDDATE + " text, "
                        + ISACTIVE + " text);";
                db.execSQL(questioniers);

                final String answerchoice = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_ANSWERCHOICE + "(" + CAMPAIGNINID
                        + "  text, " + QUESTIONID + " integer, "
                        + ISNONEDITABLE + " integer default 0, "
                        + ISNONEOFTHEABOVE + " integer default 0, "
                        + ANSWERID + " text, " + ANSWERCHOICETEXT + " text, "
                        + ISCHECKED + " integer default 0, "

                        + ISMASKINGAPPLIED + " integer default 0, " + ISACTIVE + " text);";
                db.execSQL(answerchoice);

                final String skiplogic = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_LOGICS + "(" + CAMPAIGNINID
                        + "  text, " + PAGESID + " integer, " + ACTION
                        + " integer, " + SKIPQUESTIONNAME + " text, "
                        + LOGICID + " integer, "
                        + CONDITIONIN_QUESTIONID + " text, "
                        + CONDITIONIN_OPERATOR + " text, "
                        + CONDITIONIN_JOIN + " text, "
                        + MAPPEDQUESTION + " text, "
                        + CONDITIONIN_ANSWERID + " text);";
                db.execSQL(skiplogic);

                final String pipinglogic = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_PIPING + "(" + CAMPAIGNINID
                        + "  text, " + PAGESID + " integer, " + PIPINGID
                        + " integer, " + PIPINGNAME + " text, "
                        + PIPINGSOURCEID + " integer, " + PIPINGTARGETID
                        + " integer);";
                db.execSQL(pipinglogic);

                final String masking = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_MASKING + "(" + CAMPAIGNINID
                        + "  text, " + PAGESID + " integer, " + MASKINGID
                        + " integer, " + MASKINGNAME + " text, "
                        + MASKINGSOURCEID + " integer, "
                        + MASKINGSOURCEANSWERID + " text, " + MASKINGTARGETID
                        + " integer, " + MASKINGTARGETANSWER + " integer);";
                db.execSQL(masking);

                final String meteringLog = "CREATE TABLE IF NOT EXISTS "
                        + DATABASE_TABLE_METERING_LOG +
                        "(" + Metering_LOGTIME + " integer);";
                db.execSQL(meteringLog);

            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GLOBALSETTING);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_QUESTIONIERS);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PIPING);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_MASKING);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LOGICS);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_METERING_LOG);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ANSWERCHOICE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PAGES);
            onCreate(db);
        }
    }

}