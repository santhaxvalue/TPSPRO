package com.panelManagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.panelManagement.activity.R;
import com.panelManagement.webservices.HttpConfig;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HelperBase {
    public static final String REGISTRATION_URL = "http://tpsapi.informatemi.com/tps/api_register.php?";
    public static final String AUTHENTICATION_URL = "http://tpsapi.informatemi.com/tps/api_isActive.php?";
    public static final String PINGtoIMI_URL = "http://tpsapi.informatemi.com/tps/api_ping.php?";
    public static final String SIMINFO = "siminfo";
    public static final String POSTING_URL = "http://www.informatesm.com/datadiary/DMAServlet?";
    public static String DeviceApproved = "", country = "";
    public static String email;
    public static int INTERVAL = 1, isTablet = 1, isActive = 1;
    public static int isAuthenticate = -1;
    public static boolean isValidLoginDetails = false;
    public static boolean isAsynchClassFinish = false;
    public static Activity mContext = null;
    public static String userID = "";
    public static String status, message;

    public static String getAppStatus() {
        String appStatus = "Disable";
        try {
            if (HelperBase.DeviceApproved.equalsIgnoreCase("Yes")) {
                appStatus = "Enabled";
            } else {
                appStatus = "Disabled";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appStatus;
    }

    public static String getDataDiaryID(Context mContext) {
        try {
            SharedPreferences prefs = mContext.getSharedPreferences("Login", 0);
            userID = prefs.getString("user_ID", null);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return userID;
    }

    public static boolean deviceRegistration(Context con) {
        boolean isValidLoginDetails = false;
        try {
            HelperBase.isAuthenticate = -1;
            HelperBase.DeviceApproved = "Yes";
            WebHelper diviceDector = new WebHelper(con);
            //String url = LOGIN_URL + diviceDector.getLoginURLParameter(con);
//			System.out.println("Login API Url is " + url);
            //String result = HttpConfig.sendHTTPSPost(LOGIN_URL,diviceDector.getLoginURLParameter(con));
            HttpConfig httpconfig = new HttpConfig();
            String result = httpconfig.httpget(REGISTRATION_URL + diviceDector.getRegistrationURLParameter());
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));

                Document doc = db.parse(is);
                NodeList nodes = doc.getElementsByTagName("response");

                // iterate the employees
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    status = getElementValue(element, "status");
                    message = getElementValue(element, "message");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //MeterDatabaseHelper.saveRegistration(con, status, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidLoginDetails;
    }

    public static boolean isAuthenticate(Context con) {
        boolean isValidLoginDetails = false;
        try {
            HelperBase.isAuthenticate = -1;
            HelperBase.DeviceApproved = "Yes";
            WebHelper diviceDector = new WebHelper(con);
            HttpConfig httpconfig = new HttpConfig();
            String result = httpconfig.httpget(AUTHENTICATION_URL + diviceDector.getLoginURLParameter(con));
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));

                Document doc = db.parse(is);
                NodeList nodes = doc.getElementsByTagName("response");

                // iterate the employees
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    DeviceApproved = getElementValue(element, "status");
                    message = getElementValue(element, "message");
                    country = getElementValue(element, "country");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidLoginDetails;
    }

    public static String getElementValue(Element element, String key) {
        String value = "";
        try {
            NodeList message = element.getElementsByTagName(key);
            Element line = (Element) message.item(0);
            value = getCharacterDataFromElement(line);
            System.out.println(key + " : " + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    /*REMOVE-LOCALE*/
    public static String getTermsConditionText(Context mContext) {
        String terms = "";
        try {
            /*terms = mLocaleModel.get(DatabaseHelper.I_AGREE)+" <a href='"+ mContext.getString(R.string.lbl_privacy_policy_url)+"'>"+mLocaleModel.get(DatabaseHelper.PRIVACY_POLICY)+"</a>"+
					" "+mLocaleModel.get(DatabaseHelper.AND_TEXT)+" <a href='"+ mContext.getString(R.string.lbl_terms_cond_url)+"'>"+mLocaleModel.get(DatabaseHelper.TERMS_CONDITION)+"</a>";*/
            terms = mContext.getString(R.string.txt_iaggree) + " <a href='" + mContext.getString(R.string.lbl_privacy_policy_url) + "'>" + mContext.getString(R.string.lbl_privacy_policy) + "</a>" +
                    " " + mContext.getString(R.string.txt_and_text) + " <a href='" + mContext.getString(R.string.lbl_terms_cond_url) + "'>" + mContext.getString(R.string.lbl_terms_cond) + "</a>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms;
    }

    /*REMOVE-LOCALE*/
    public static String getVisitPrivacypolicyText(Context mContext) {
        String terms = "";
        try {
            //String s = mLocaleModel.get(DatabaseHelper.VISIT);
            terms = mContext.getString(R.string.txt_visit) + " <a href=" + mContext.getString(R.string.lbl_privacy_policy_url) + ">" + mContext.getString(R.string.lbl_privacy_policy) + "</a>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms;
    }

    /*REMOVE-LOCALE*/
    public static String getVisitTermsConditionText(Context mContext) {
        String terms = "";
        try {
            terms = mContext.getString(R.string.txt_visit) + " <a href='" + mContext.getString(R.string.lbl_terms_cond_url) + "'>" + mContext.getString(R.string.lbl_terms_cond) + "</a>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms;
    }
}
