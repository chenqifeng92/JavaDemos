package com.chen.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Iterator;
import java.util.List;

@DisplayName("身份证验证工具测试")
public class TestValidateIdCard {

    /**
     * 输入完整的身份证号验证是否符合加权规律
     * @throws Exception
     */
    @Test
    @DisplayName("验证完整身份证号的加权规律")
    public void getResult() throws Exception{
        String idCardNo = "370613199603182018";//奈格利奇
        ValidateIdCard vi = new ValidateIdCard();
        boolean result = vi.verifyLastDigit(idCardNo);
        if (result==true){
            System.out.println("该身份证号符合身份证加权规律，可能真实。");
        }else {
            System.out.println("该身份证号一定不真实！");
        }
    }

    /*
     * 已知身份证前十四位求后四位
     */
    @Test
    @DisplayName("已知前14位推算后4位")
    public void getLast4Digits(){
        String idCardDb = "42030219880930";//雷蕾
        boolean isMale = false;//输入性别，true为男，false为女
        boolean haveX = false;//最后一位是否可以是X

        String gender = isMale?"男":"女";
        String lastX = haveX?"最后一位包含X":"最后一位不包含X";

        ValidateIdCard vr = new ValidateIdCard();
        List<String> idCardList = vr.figureIdLastFour(idCardDb,isMale);

        if (haveX==true){
            System.out.println("可能的结果数："+idCardList.size()+", "+lastX);
            for(String idCard : idCardList){
                System.out.println(idCard+" "+gender);
            }
        }else {
            Iterator<String> it = idCardList.iterator();
            while (it.hasNext()){
                String idCardNo = it.next();
                ValidateIdCard vi = new ValidateIdCard();
                if(String.valueOf(idCardNo.charAt(17)).equals("X")){
                    it.remove();
                }
            }
            System.out.println("可能的结果数："+idCardList.size()+", "+lastX);

            for(String idCard : idCardList){
                System.out.println(idCard+" "+gender);
            }
        }
    }

    /**
     * 根据火车票面身份信息（码掉生日），遍历出可能的结果
     * @throws Exception
     */
    @Test
    @DisplayName("根据脱敏身份证号反推生日")
    public void getBirthIdNo() throws Exception{
        String ticketFormatIdNo = "3411021997****6427";//程璇
        ValidateIdCard vi = new ValidateIdCard();
        vi.foreachBirthday(ticketFormatIdNo);
        List<String> idNos = vi.foreachBirthday(ticketFormatIdNo);
        System.out.println("遍历出"+idNos.size()+"条可能的结果。");
        for(String idCardNo:idNos){
            System.out.println(idCardNo);
        }
    }
}
