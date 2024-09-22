package com.example.appmanager.Model;

import java.util.List;

public class SalesReportModel {
    boolean success;
    String message;
    List<SalesReport> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SalesReport> getResult() {
        return result;
    }

    public void setResult(List<SalesReport> result) {
        this.result = result;
    }
}
