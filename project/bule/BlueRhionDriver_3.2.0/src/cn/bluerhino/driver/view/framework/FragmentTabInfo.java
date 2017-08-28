package cn.bluerhino.driver.view.framework;

import android.os.Bundle;

/**
 * 
 * Describe:Tab信息类
 * 
 * Date:2015-6-26
 * 
 * Author:liuzhouliang
 */
public class FragmentTabInfo {

    private String tabId;
    private Class<?> clss;
    private int titleResID;
    private int iconResID;
    private Bundle bundle;

    private FragmentTabInfo(Builder builder) {
        this.tabId = builder.tabId;
        this.titleResID = builder.titleResID;
        this.iconResID = builder.iconResID;
        this.clss = builder.clss;
        this.bundle = builder.bundle;
    }

    public final String getTabId() {
        return tabId;
    }

    public final Class<?> getFragmentClass() {
        return clss;
    }

    public final int getTitleResID() {
        return titleResID;
    }

    public final int getIconResID() {
        return iconResID;
    }

    public final Bundle getBundle() {
        return bundle;
    }

    @Override
    public String toString() {
        return "FragmentTabInfo [tabId=" + tabId + ", clss=" + clss
                + ", titleResID=" + titleResID + ", iconResID=" + iconResID
                + ", bundle=" + bundle + "]";
    }

    public static class Builder {
        private String tabId;
        private Class<?> clss;
        private int titleResID;
        private int iconResID;
        private Bundle bundle;

        public Builder() {
        }

        public final Builder setTabId(String tabId) {
            this.tabId = tabId;
            return this;
        }

        public final Builder setFragmentClass(Class<?> clss) {
            this.clss = clss;
            return this;
        }

        public final Builder setTitleResID(int titleResID) {
            this.titleResID = titleResID;
            return this;
        }

        public final Builder setIconResID(int iconResID) {
            this.iconResID = iconResID;
            return this;
        }

        public final Builder setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public FragmentTabInfo build() {
            return new FragmentTabInfo(this);
        }
    }

}