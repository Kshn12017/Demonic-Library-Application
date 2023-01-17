package com.example.testlibv1;

public class Register {

    String uname;
    String uusername;
    String uemail;
    String upassword;

    public Register(String uname, String uusername, String uemail, String upassword) {
        this.uname = uname;
        this.uusername = uusername;
        this.uemail = uemail;
        this.upassword = upassword;
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

    public String getUpassword() {
        return upassword;
    }
}
