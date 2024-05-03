/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.health.api.model;

import java.io.Serializable;

/**
 *
 * @author rasintha_j
 */
public class TestBean {
    String responseCode;
    String responseMsg;
    Serializable content;

    public TestBean() {
    }

    public TestBean(String responseCode, String responseMsg, Serializable content) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.content = content;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Serializable getContent() {
        return content;
    }

    public void setContent(Serializable content) {
        this.content = content;
    }
    
    
}
