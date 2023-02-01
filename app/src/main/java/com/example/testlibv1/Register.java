package com.example.testlibv1;

public class Register {

    String uname;
    String uusername;
    String uemail;

    public Register(String uname, String uusername, String uemail) {
        this.uname = uname;
        this.uusername = uusername;
        this.uemail = uemail;
    }

    public String getUname() {
        return uname;
    }

    public String getUusername() {
        return uusername;
    }

    public String getUemail() {
        return uemail;
    }

}
