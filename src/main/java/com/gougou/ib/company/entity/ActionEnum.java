package com.gougou.ib.company.entity;

import com.ib.client.Types;
import lombok.Getter;
import org.springframework.util.StringUtils;

public enum ActionEnum {

    SELL("SELL"),
    BUY("BUY"),
    SHORT("SHORT"),
    COVER("COVER");

    @Getter
    private String action;

    public static ActionEnum getByAction(String action) {
        if (!StringUtils.isEmpty(action)) {
            for (ActionEnum actionEnum : ActionEnum.values()) {
                if (action.equalsIgnoreCase(actionEnum.getAction())) {
                    return actionEnum;
                }
            }
            if ("买入".equals(action) || "做多".equals(action) || "BUY".equalsIgnoreCase(action)) {
                return BUY;
            } else if ("卖出".equals(action) || "SELL".equalsIgnoreCase(action)) {
                return SELL;
            } else if ("做空".equals(action) || "SHORT".equalsIgnoreCase(action)) {
                return SHORT;
            } else if ("平仓".equals(action) || "COVER".equalsIgnoreCase(action)) {
                return COVER;
            }
        }
        throw new RuntimeException(String.format("[action=%s]没有符合的ActionEnum", action));
    }

    public static Types.Action getByAction(ActionEnum action) {
        if (SELL.equals(action) || SHORT.equals(action)) {
            return Types.Action.SELL;
        } else if (BUY.equals(action) || COVER.equals(action)) {
            return Types.Action.BUY;
        }
        throw new RuntimeException(String.format("交易动作[%s]转化为IB的交易类型失败", action.action));
    }

    /**
     * 判定是否开仓
     *
     * @param action
     * @return
     */
    public static boolean isOpenPosition(ActionEnum action) {
        return BUY.equals(action) || SHORT.equals(action);
    }

    ActionEnum(String action) {
        this.action = action;
    }
}
