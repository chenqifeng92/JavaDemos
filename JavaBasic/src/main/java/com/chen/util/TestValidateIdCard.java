package com.chen.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestValidateIdCard {

    /**
     * 输入完整的身份证号验证是否符合加权规律
     * @throws Exception
     */
    @Test
    public void getResult() throws Exception{
        String idCardNo = "420117199701190022";//邓薇
        ValidateIdCard vi = new ValidateIdCard();
        vi.verifyLastDigit(idCardNo);
    }

    /*
     * 已知身份证前十四位求后四位
     */
    @Test
    public void getLast4Digits(){
        String idCardDb = "42030219880930";//雷蕾
        boolean isMale = false;//输入性别

        ValidateIdCard vr = new ValidateIdCard();
        List<String> idCardList = vr.figureIdLastFour(idCardDb,isMale);

        //排除最后一位为X的身份证号
        List<String> idCardListX = new ArrayList<String>();
        /*for(int i=0;i<idCardList.size();i++){
            String idCard = idCardList.get(i);
            String aaa =  String.valueOf(idCard.charAt(17));
            if (aaa.equals("X")){
                idCardList.remove(i);
                idCardListX.add(idCard);
            }
        }*/

        //打印包含X的身份证号
        for(String idCard : idCardListX){
            System.out.println(idCard);
        }
        System.out.println(idCardListX.size());

        //遍历身份证号码数组的结果集
        for(String idCard : idCardList){
            if(isMale==true){
                System.out.println(idCard+","+"男");
            }else if(isMale==false){
                System.out.println(idCard+","+"女");
            }
        }
        System.out.println(idCardList.size());
    }

    /**
     * 根据火车票面身份信息（码掉生日），遍历出可能的结果
     * @throws Exception
     */
    @Test
    public void getBirthIdNo() throws Exception{
        String ticketFormatIdNo = "3411021997****6427";//程璇
        ValidateIdCard vi = new ValidateIdCard();
        vi.foreachBirthday(ticketFormatIdNo);
        List<String> idNos = vi.foreachBirthday(ticketFormatIdNo);
        System.out.println(idNos.size());
        for(String idCardNo:idNos){
            System.out.println(idCardNo);
        }
    }
}
