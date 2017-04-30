package org.study.stasy.Exeptions;

import java.io.IOException;

/**
 * Created by ASPA on 27.04.2017.
 */
public class SessionException extends Throwable {
    private String clName;

    public SessionException(String msg, String clName) {
        super(msg);
        this.clName = clName;
    }
    public SessionException(String msg) {
        super(msg);
    }

    public SessionException(String msg, IOException e) {
        super(msg, e);
    }
}
