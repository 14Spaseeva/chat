package org.study.stasy.Exeptions;

public class ClientException extends Exception {

    String ctrlMsg;
    public ClientException() {
        super();
    }
    public ClientException(String message) {
        super(message);
    }
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
    public ClientException(Throwable cause) {
        super(cause);
    }
    public ClientException(String message, String ctrlMsg){
        super(message);
        this.ctrlMsg = ctrlMsg;
    }


}