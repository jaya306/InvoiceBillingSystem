package com.csi.email;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Mail {

    private String mailFrom;


    private String[] mailTo;

    private String mailSubject;

    private String mailContent;

    private String contentType;
    private String[] cc;

    private String[] bcc;

    public Mail() {
        contentType = "text/plain";
    }

    private String attachment;
}
