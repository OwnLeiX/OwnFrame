package lx.own.frame.tools.utils;

import lx.own.frame.frame.base.BaseConfig;
import lx.own.frame.frame.base.BaseKeys;

public class ThemeUtil {
    /**
     * 保存主题设置
     *
     * @param theme 主题
     */
    public static void setTheme(String theme) {
        SPUtil.$().putString(BaseKeys.Sp.THEME_MODE, theme);
    }

    /**
     * 取出当前主题
     *
     * @return 主题
     */
    public static String getTheme() {
        return SPUtil.$().getString(BaseKeys.Sp.THEME_MODE, BaseConfig.Theme.DEFAULT);
    }
}
