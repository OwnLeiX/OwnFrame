/*
 * Copyright 2016 Freelander
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lx.own.frame.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;

import lx.own.frame.frame.base.BaseApplication;
import lx.own.frame.tools.log.Logger;

public class SPUtil {

    private static final String TAG = "SPUtil";
    private final static String SP = "michong_sp";

    private volatile static SPUtil mInstance;

    public static SPUtil $() {
        if (mInstance == null) {
            synchronized (SPUtil.class) {
                if (mInstance == null)
                    mInstance = new SPUtil(BaseApplication.mContext);
            }
        }
        return mInstance;
    }


    private SharedPreferences mPreferences;

    private SPUtil(Context context) {
        if (context != null)
            mPreferences = context.getSharedPreferences(SP, Context.MODE_PRIVATE);
    }

    public boolean putString(String key, String value) {
        boolean returnValue = false;
        try {
            returnValue = mPreferences.edit().putString(key, value).commit();
        } catch (NullPointerException e) {
            retryInit();
        } catch (Exception e) {
            Logger.e(TAG,"异常：",e);
        }
        return returnValue;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defValue) {
        String returnValue = defValue;
        try {
            returnValue = mPreferences.getString(key, defValue);
        } catch (NullPointerException e) {
            retryInit();
        } catch (Exception e) {
            Logger.e(TAG,"异常：",e);
        }
        return returnValue;
    }

    public boolean removeString(String key) {
        boolean returnValue = false;
        try {
            returnValue = mPreferences.edit().remove(key).commit();
        } catch (NullPointerException e) {
            retryInit();
        } catch (Exception e) {
            Logger.e(TAG,"异常：",e);
        }
        return returnValue;
    }

    private void retryInit() {
        try {
            mPreferences = BaseApplication.mContext.getSharedPreferences(SP, Context.MODE_PRIVATE);
        } catch (Exception e) {
            Logger.e(TAG, "尝试重新初始化失败", e);
        }
    }

    public boolean putInt(String key, int value) {
        boolean returnValue = false;
        try {
            returnValue = mPreferences.edit().putInt(key, value).commit();
        } catch (NullPointerException e) {
            retryInit();
        } catch (Exception e) {
            Logger.e(TAG,"异常：",e);
        }
        return returnValue;
    }

    public int getInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    public boolean putBoolean(String key, boolean value) {
        boolean returnValue = false;
        try {
            returnValue = mPreferences.edit().putBoolean(key, value).commit();
        } catch (NullPointerException e) {
            retryInit();
        } catch (Exception e) {
            Logger.e(TAG,"异常：",e);
        }
        return returnValue;
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

}
