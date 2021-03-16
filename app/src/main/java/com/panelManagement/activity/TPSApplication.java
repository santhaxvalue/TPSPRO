package com.panelManagement.activity;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gcm.GCMRegistrar;
import com.kochava.base.Tracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.panelManagement.asyncimage.ImageCache;
import com.panelManagement.utils.PanelConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TPSApplication extends MultiDexApplication {

    public static final boolean DEVELOPER_MODE = false;
    private static final int CORE_POOL_SIZE = 5;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "GreenDroid thread #"
                    + mCount.getAndIncrement());
        }
    };
    public static Context context;
    SharedPreferences pref;
    private ExecutorService mExecutorService;
    private ImageCache mImageCache;
    private ArrayList<WeakReference<OnLowMemoryListener>> mLowMemoryListeners;

    public TPSApplication() {
        TPSApplication.context = this;
        mLowMemoryListeners = new ArrayList<>();

    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        if (DEVELOPER_MODE
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyDeath().build());
        }
        super.onCreate();

        initImageLoader(getApplicationContext());
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        Tracker.configure(new Tracker.Configuration(getApplicationContext())
                .setAppGuid("kothepanelstation-fuq9a3xx")
                .setLogLevel(Tracker.LOG_LEVEL_INFO));

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    public void GCMBinding(Context context) throws Exception {
        GCMRegistrar.checkDevice(context);
        if (PanelConstants.DEBUG)
            Log.d("GCM", GCMRegistrar.getRegistrationId(context));
        GCMRegistrar.checkManifest(context);

        String regId = GCMRegistrar.getRegistrationId(context);

        if (regId.equals("")) {
            GCMRegistrar.register(context, PanelConstants.GCM_SENDER_ID);

        } else {
            if (PanelConstants.DEBUG)
                Log.d("GCM", "Already registered");
            PanelConstants.GCM_REG_ID = regId;
            if (PanelConstants.DEBUG)
                Log.d("Tag", "Device Id" + PanelConstants.GCM_REG_ID);
            GCMRegistrar.setRegisteredOnServer(context, true);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        int i = 0;
        while (i < mLowMemoryListeners.size()) {
            final OnLowMemoryListener listener = mLowMemoryListeners.get(i)
                    .get();
            if (listener == null) {
                mLowMemoryListeners.remove(i);
            } else {
                listener.onLowMemoryReceived();
                i++;
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Return an ExecutorService (global to the entire application) that may be
     * used by clients when running long tasks in the background.
     *
     * @return An ExecutorService to used when processing long running tasks
     */
    public ExecutorService getExecutor() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE,
                    sThreadFactory);
        }
        return mExecutorService;
    }

    /**
     * Return this application {@link ImageCache}.
     *
     * @return The application {@link ImageCache}
     */
    public ImageCache getImageCache() {
        if (mImageCache == null) {
            mImageCache = new ImageCache(this);
        }
        return mImageCache;
    }

    /**
     * Add a new listener to registered {@link OnLowMemoryListener}.
     *
     * @param listener The listener to unregister
     * @see OnLowMemoryListener
     */
    public void registerOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            mLowMemoryListeners.add(new WeakReference<OnLowMemoryListener>(
                    listener));
        }
    }

    /**
     * Remove a previously registered listener
     *
     * @param listener The listener to unregister
     * @see OnLowMemoryListener
     */
    public void unregisterOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            int i = 0;
            while (i < mLowMemoryListeners.size()) {
                final OnLowMemoryListener l = mLowMemoryListeners.get(i).get();
                if (l == null || l == listener) {
                    mLowMemoryListeners.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public static interface OnLowMemoryListener {
        public void onLowMemoryReceived();
    }

    /**
     * It check Parse is inilise or not and Register it if not register or updating registrating status
     * */
	/*private void parseInit(){
		try{
			try{
				if(!ParseCrashReporting.isCrashReportingEnabled())
					ParseCrashReporting.enable(this);
			}catch(Exception e){
				e.printStackTrace();
			}

			Parse.initialize(this, getResources().getString(R.string.parser_appid),getResources().getString(R.string.parser_clientkey));
			pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			if(!pref.getBoolean("isParsePush", false) || !pref.getBoolean("ParsePushRegister", false)){
				ParseRegistration.iniliseParse(this);
			}
			String userName = pref.getString("user_Name", "");
			if(!userName.equalsIgnoreCase("") && !pref.getBoolean("ParsePushRegister", false)){
				ParseRegistration.isUpdateRegistration(getApplicationContext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
}
