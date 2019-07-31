package com.chen.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class ValidateIdCard {

    //身份证验证正则表达式
    String idFormatRegex = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    //火车票面身份信息字符串格式
    String ticketFormatRegex = "^\\d{10}\\*{4}\\d{3}[0-9Xx]$";

    /**
     * 身份证号权重规则算法
     * @param idCardNo
     * @return
     */
    public static String idCardNoWeight(String idCardNo){
        int[] weight = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};//加权数组
        int[] sfz = new int[17];//身份证前十七位
        int s = 0;//权重之和
        int rs = 0;//权重%11
        int[] last = {1,0,10,9,8,7,6,5,4,3,2};//最后一位对比数组
        int lastNo = 0;//最后一位数字
        String lastStr = null;//最后一位字符（考虑到10需要转为X）

        //将需要验证的身份证号码返回前17位数并且组成一个数组
        for(int i=0;i<sfz.length;i++){
            sfz[i] = Character.getNumericValue(idCardNo.charAt(i));//char类型不能强转为int
        }
        //加权验证
        for(int i=0;i<weight.length;i++){
            s = s + weight[i]*sfz[i];
        }
        rs = s%11;
        //获取最后一位数
        lastNo = last[rs];
        if(lastNo==10){
            lastStr = "X";
        }else{
            lastStr = String.valueOf(lastNo);
        }
        return lastStr;
    }

    /**
     * 入参为完整的身份证号，验证最后一位是否符合加权规则
     * @param idCardNo
     * @return
     */
    public boolean verifyLastDigit(String idCardNo){
        boolean result = false;
        //idCardNo = "420117199701190022";
        String lastStr = idCardNo.substring(idCardNo.length()-1);
        if(!idCardNo.matches(idFormatRegex)){
            System.out.println("抱歉，身份证格式不正确，请检查输入...");
        }else{
            result = lastStr.equals(ValidateIdCard.idCardNoWeight(idCardNo))?true:false;
        }
        return result;
    }

    /**
     * 入参为身份证号前14位，性别，求后四位，并打印出完整身份证号
     * @param idCardDb
     * @param isMale
     * @return
     */
    public List<String> figureIdLastFour(String idCardDb, boolean isMale){
        String atr = null;//身份证号后三位组合
        String sfza = null;//身份证号前十七位组合
        String idCard = null;//完整的身份证号字符串
        String lastStr = null;//身份证最后一位字符
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
            lastStr = ValidateIdCard.idCardNoWeight(sfza);

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

    /**
     * 根据火车票面身份信息（码掉生日），遍历出可能的结果
     * @param ticketFormatIdNo
     * @return
     * @throws Exception
     */
    public List<String> foreachBirthday(String ticketFormatIdNo) throws Exception{
        List<String> idNos = new ArrayList<String>();
        if(!ticketFormatIdNo.matches(ticketFormatRegex)){
            System.out.println("输入不符合火车票面身份证字符串格式");
        }else{
            String birthyear = ticketFormatIdNo.substring(6,10);
            String birthdate = null;
            String calcuateYear = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(birthyear + "-01-01");
            do{//遍历当年日期，组成数组
                birthdate = sdf.format(date).replace("-","").substring(4,8);
                idNos.add(ticketFormatIdNo.replace("****",birthdate));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR,1);
                date = calendar.getTime();
                calcuateYear = sdf.format(date).substring(0,4);

            }while(calcuateYear.equals(birthyear));
        }
        //直接这么写不行，会报ConcurrentModificationException异常
        //解决方案见https://www.cnblogs.com/tonz/p/9662236.html
        /*for(String idCardNo:idNos){
            ValidateIdCard vi = new ValidateIdCard();
            if(vi.verifyLastDigit(idCardNo)==false){
                idNos.remove(idCardNo);
            }
        }*/
        Iterator<String> it = idNos.iterator();
        while (it.hasNext()){
            String idCardNo = it.next();
            ValidateIdCard vi = new ValidateIdCard();
            if(vi.verifyLastDigit(idCardNo)==false){
                it.remove();
            }
        }
        return idNos;
    }

}
