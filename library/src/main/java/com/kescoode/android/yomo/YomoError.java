package com.kescoode.android.yomo;

/**
 * Yomo库的异常类
 *
 * @author Kesco Lin
 * @since 2014-12-05
 */
public class YomoError extends Exception {
    private static final long serialVersionUID = -1291755365277907607L;

    private Throwable[] causes;

    public YomoError(String detailMessage, Throwable[] causes) {
        super(detailMessage, causes != null && causes.length > 0 ? causes[0] : null);

        this.causes = causes != null && causes.length > 0 ? causes : null;
    }

    public Throwable[] getCauses() {
        return causes;
    }
}
