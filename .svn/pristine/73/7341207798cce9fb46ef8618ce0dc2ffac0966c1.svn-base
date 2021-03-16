package com.panelManagement.controllers;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.model.AnswerChoiceModel;
import com.panelManagement.model.MaskinglogicsModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author manojp This class handle spinner to display defalut message instead
 *         of showing 0th position of array. It Intercepts getView() to display
 *         the prompt if position < 0
 */
public class NoDefaultSpinner extends Spinner implements
        android.widget.AdapterView.OnItemSelectedListener {

    Context mContext;
    DBAdapter database;

    public NoDefaultSpinner(Context context) {
        super(context);
        mContext = context;
        database = DBAdapter.getDBAdapter(mContext);
        setPadding(15, 0, 150, 0);
        setOnItemSelectedListener(this);
        setBackgroundResource(R.drawable.dropdown_default);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setGravity(Gravity.CENTER);
        }
    }

    public NoDefaultSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemSelectedListener(this);
    }

    public NoDefaultSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnItemSelectedListener(this);
    }

    @Override
    public void setAdapter(SpinnerAdapter orig) {
        final SpinnerAdapter adapter = newProxy(orig);

        super.setAdapter(adapter);

        try {
            final Method m = AdapterView.class.getDeclaredMethod(
                    "setNextSelectedPositionInt", int.class);
            m.setAccessible(true);
            m.invoke(this, -1);

            final Method n = AdapterView.class.getDeclaredMethod(
                    "setSelectedPositionInt", int.class);
            n.setAccessible(true);
            n.invoke(this, -1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected SpinnerAdapter newProxy(SpinnerAdapter obj) {
        return (SpinnerAdapter) java.lang.reflect.Proxy.newProxyInstance(obj
                        .getClass().getClassLoader(),
                new Class[]{SpinnerAdapter.class}, new SpinnerAdapterProxy(
                        obj));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        AnswerChoiceModel object = (AnswerChoiceModel) parent
                .getItemAtPosition(position);

        if (object != null) {
            setBackgroundResource(R.drawable.dropdown_default);
            ArrayList<MaskinglogicsModel> maskingArray = database.getMaskingLogicsData(object.getCampaignId(), object.getAnswerID());
            if (maskingArray.size() > 0) {
                //doing this for reseting answers
                MaskinglogicsModel resetAnswers = maskingArray.get(0);
                ArrayList<AnswerChoiceModel> allAnswers = database.getAnswerChoice(object.getCampaignId(), String.valueOf(resetAnswers.getTargetid()), 1);
                for (int i = 0; i < allAnswers.size(); i++) {
                    MaskinglogicsModel modelMasking = maskingArray
                            .get(i);
                    AnswerChoiceModel tempObject = new AnswerChoiceModel(
                            object.getCampaignId(), modelMasking.getTargetid(),
                            modelMasking.getTargetAnswerId(), 0);
                    database.updateMaskingOnAnswers(tempObject);
                }

                for (int maskingi = 0; maskingi < maskingArray.size(); maskingi++) {
                    MaskinglogicsModel modelMasking = maskingArray
                            .get(maskingi);
                    AnswerChoiceModel tempObject = new AnswerChoiceModel(
                            object.getCampaignId(), modelMasking.getTargetid(),
                            modelMasking.getTargetAnswerId(), 1);
                    database.updateMaskingOnAnswers(tempObject);
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Intercepts getView() to display the prompt if position < 0
     */
    protected class SpinnerAdapterProxy implements InvocationHandler {

        protected SpinnerAdapter obj;
        protected Method getView;

        protected SpinnerAdapterProxy(SpinnerAdapter obj) {
            this.obj = obj;
            try {
                this.getView = SpinnerAdapter.class.getMethod("getView",
                        int.class, View.class, ViewGroup.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object invoke(Object proxy, Method m, Object[] args)
                throws Throwable {
            try {
                return m.equals(getView) && (Integer) (args[0]) < 0 ? getView(
                        (Integer) args[0], (View) args[1], (ViewGroup) args[2])
                        : m.invoke(obj, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected View getView(int position, View convertView, ViewGroup parent)
                throws IllegalAccessException {

            if (position < 0) {
                final TextView v = (TextView) ((LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(android.R.layout.simple_spinner_item, parent,
                                false);
                v.setText(getPrompt());
                return v;
            }
            return obj.getView(position, convertView, parent);
        }
    }
}