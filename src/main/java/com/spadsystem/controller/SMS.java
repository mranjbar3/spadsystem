package com.spadsystem.controller;

import com.kavenegar.sdk.KavenegarApi;
import com.kavenegar.sdk.excepctions.ApiException;
import com.kavenegar.sdk.excepctions.HttpException;
import com.kavenegar.sdk.models.SendResult;

public class SMS {
    public static boolean sendSms(String receiver, String text) {
        try {
            KavenegarApi api = new KavenegarApi("6A3155696A4F4D67566541713463664A794C593266413D3D");
            SendResult Result =
            api.send("30006703791791", receiver, text);
            return true;
        } catch (HttpException ex) {
            System.out.print("HttpException  : " + ex.getMessage());
        } catch (ApiException ex) {
            System.out.print("ApiException : " + ex.getMessage());
        }
        return false;
    }
}
