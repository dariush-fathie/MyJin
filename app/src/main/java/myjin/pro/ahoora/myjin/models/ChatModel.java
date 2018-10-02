package myjin.pro.ahoora.myjin.models;

public class ChatModel {
    private Boolean modeMsg = true;
    private String contextMsg = "";
    private String timeMsg = "";


    public Boolean getModeMsg() {
        return modeMsg;
    }

    public void setModeMsg(Boolean modeMsg) {
        this.modeMsg = modeMsg;
    }

    public String getContextMsg() {
        return contextMsg;
    }

    public void setContextMsg(String contextMsg) {
        this.contextMsg = contextMsg;
    }

    public String getTimeMsg() {
        return timeMsg;
    }

    public void setTimeMsg(String timeMsg) {
        this.timeMsg = timeMsg;
    }
}
