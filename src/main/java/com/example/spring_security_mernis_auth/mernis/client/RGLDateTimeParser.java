
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
public interface RGLDateTimeParser
{
    String getStringFromDateTime(java.time.ZonedDateTime date);
    String getStringFromDate(java.time.LocalDate date);
    String getStringFromTime(java.time.LocalTime date);
    String getStringFromDuration(java.time.Duration date);
    java.time.ZonedDateTime parse(String value);
    java.time.LocalDate parseLocal(String value);
    java.time.Duration parseDuration(String value);
    java.time.LocalTime parseTime(String value);
}
