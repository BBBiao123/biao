package com.biao.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UserCoinVolumeEventEnum .
 * 操作 bb,wallet,coinVolume op操作信息.
 * ctime: 2018/9/27 19:52
 *
 *  "" sixh
 */
public enum UserCoinVolumeEventEnum {
    /**
     * Sub lockvolume online coin volume op enum.
     */
    SUB_LOCKVOLUME(2, "减少lockVolume"),

    /**
     * Add lockvolume online coin volume op enum.
     */
    ADD_LOCKVOLUME(4, "增加lockVolume"),

    /**
     * Add volume online coin volume op enum.
     */
    ADD_VOLUME(8, "增加volume"),

    /**
     * Sub volume online coin volume op enum.
     */
    SUB_VOLUME(16, "减少volume");
    /**
     * The Text.
     */
    private String text;

    /**
     * The Event.
     */
    private Integer event;

    /**
     * Instantiates a new Online coin volume op enum.
     *
     * @param event the event
     * @param text  the text
     */
    UserCoinVolumeEventEnum(int event, String text) {
        this.event = event;
        this.text = text;
    }

    /**
     * 转换不同的状态组合显示.
     *
     * @param eventEnum the evnet enum
     * @return the integer
     */
    public static Integer parseEvent(UserCoinVolumeEventEnum... eventEnum) {
        int event = 0;
        for (UserCoinVolumeEventEnum o : eventEnum) {
            if (o != null) {
                event = event | o.getEvent();
            }
        }
        return event;
    }

    /**
     * Gets event text.
     *
     * @param event the event
     * @return the event text
     */
    public static String getEventText(Integer event) {
        UserCoinVolumeEventEnum[] values = UserCoinVolumeEventEnum.values();
        List<String> list = new ArrayList<>();
        for (UserCoinVolumeEventEnum value : values) {
            boolean equals = Objects.equals(event & value.getEvent(), value.getEvent());
            if (equals) {
                list.add(value.getText());
            }
        }
        return String.join("/", list);
    }

    /**
     * Check boolean.
     *
     * @param event the event
     * @return the boolean
     */
    public boolean check(Integer event) {
        return Objects.equals(event & this.getEvent(), this.getEvent());
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets event.
     *
     * @return the event
     */
    public Integer getEvent() {
        return event;
    }

    public static void main(String[] args) {
        System.out.println(UserCoinVolumeEventEnum.getEventText(18));
    }
}
