package com.chen.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ValidateIdCard {
    //身份证前十七位加权权重
    int[] weight = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    //身份证前十七位数组
    int[] sfz = new int[17];
    //权重之和
    int s = 0;
    //对权重求余11
    int result = 0;
    //身份证最后一位比对结果数组
    int[] last = {1,0,10,9,8,7,6,5,4,3,2};
    //身份证最后一位结果数
    int lastNo = 0;
    String lastStr = null;

    //生日月
    int[] birthMonth = {1,2,3,4,5,6,7,8,9,10,11,12};
    //生日日

    /**
     * 入参为身份证号前17位，求最后一位；
     * 入参为18位身份证号，验证最后一位；
     * @param idCardNo
     * @return
     */
    public String validateIdLast(String idCardNo){
        //将需要验证的身份证号码返回前17位数并且组成一个数组
        for(int i=0;i<sfz.length;i++){
            sfz[i] = Character.getNumericValue(idCardNo.charAt(i));//char类型不能强转为int
        }
        //加权验证
        for(int i=0;i<weight.length;i++){
            s = s + weight[i]*sfz[i];
        }
        result = s%11;
        //获取最后一位数
        lastNo = last[result];
        if(lastNo==10){
            lastStr = "X";
        }else{
            lastStr = String.valueOf(lastNo);
        }

        return lastStr;
    }

    /**
     * 入参为身份证号前14位，性别，求后四位打印出完整身份证号
     * @param idCardDb
     * @param isMale
     * @return
     */
    public List<String> figureIdLastFour(String idCardDb,boolean isMale){
        String atr = null;//身份证号后三位组合
        String sfza = null;//身份证号前十七位组合
        String idCard = null;//完整的身份证号字符串
        List<String> idCardList = new ArrayList<String>();//完整的十八位身份证号


        for(int aaa=0;aaa<1000;aaa++){
            if(aaa<10){
                atr = "00"+String.valueOf(aaa);
            }else if(aaa>=10 && aaa<100){
                atr = "0"+String.valueOf(aaa);
            }else if(aaa>=100){
                atr = aaa+"";
            }

            sfza = idCardDb+atr;//身份证号前十七位
            ValidateIdCard sfzz = new ValidateIdCard();
            lastStr = sfzz.validateIdLast(sfza);

            idCard = sfza+lastStr;//完整的身份证号码字符串
            int flag = Character.getNumericValue(idCard.charAt(16));//第十七位数，奇偶性判断男女

            if(isMale==true && flag%2==1){
                idCardList.add(idCard);
            }else if(isMale==false && flag%2==0){
                idCardList.add(idCard);
            }

        }
        return idCardList;
    }

    /*
     * 验证身份证最后一位
     */
    @Test
    public void verifyLastDigit(){
        String idCardNo = "420117199701190022";
        ValidateIdCard vi = new ValidateIdCard();
        String lastStr = vi.validateIdLast(idCardNo);
        System.out.println(lastStr);
    }

    /*
     * 已知身份证前十四位求后四位
     */
    @Test
    public void getLast4Digits(){
        String idCardDb = "32068419970725";//输入身份证号码前十四位
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
     * 生日被码掉遍历可能的生日
     */
    @Test
    public void getBirthday(){

    }
}
