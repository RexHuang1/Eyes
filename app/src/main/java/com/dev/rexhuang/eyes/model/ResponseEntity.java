package com.dev.rexhuang.eyes.model;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 */
public class ResponseEntity {
    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
