

package com.example.spring_security_mernis_auth.mernis.client;
//------------------------------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 9.3.3.2
//
// Created by Quasar Development 
//
// This class has been generated for test purposes only and cannot be used in any commercial project.
// To use it in commercial project, you need to generate this class again with Premium account.
// Check https://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account.
//
// Licence: E71EDDEC80849816633F4E25B077CDC8B4B9AE6A4456FB1610FC94F1925A010AD45348CC01782506937212A4F78FE3BB81AE1BF92D23BD3DA90D9594F9146FFE
//------------------------------------------------------------------------

public class RGLSoapException extends java.lang.Exception
{
    private org.w3c.dom.Element _details=null;
    private Object _fault = null;
    
    public RGLSoapException(String message,org.w3c.dom.Element details)
    {
        super(message);
        _details=details;
    }

    public RGLSoapException(Object fault)
    {
        _fault=fault;
    }

    public Object getFault() {
        return _fault;
    }


    public org.w3c.dom.Element getDetails() {
        return _details;
    }
}