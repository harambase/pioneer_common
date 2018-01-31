package com.harambase.pioneer.common.support.util;

import com.harambase.pioneer.common.HaramMessage;
import com.harambase.pioneer.common.constant.FlagDict;

public class ReturnMsgUtil {

    public static HaramMessage success(Object data) {
        HaramMessage haramMessage = new HaramMessage();
        haramMessage.setData(data);
        haramMessage.setCode(FlagDict.SUCCESS.getV());
        haramMessage.setMsg(FlagDict.SUCCESS.getM());
        return haramMessage;
    }

    public static HaramMessage fail() {
        HaramMessage haramMessage = new HaramMessage();
        haramMessage.setCode(FlagDict.FAIL.getV());
        haramMessage.setMsg(FlagDict.FAIL.getM());
        return haramMessage;
    }

    public static HaramMessage systemError() {
        HaramMessage haramMessage = new HaramMessage();
        haramMessage.setCode(FlagDict.SYSTEM_ERROR.getV());
        haramMessage.setMsg(FlagDict.SYSTEM_ERROR.getM());
        return haramMessage;
    }

    public static HaramMessage custom(FlagDict flagDict) {
        HaramMessage haramMessage = new HaramMessage();
        haramMessage.setCode(flagDict.getV());
        haramMessage.setMsg(flagDict.getM());
        return haramMessage;
    }
}
