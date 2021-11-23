package main.standard.model;

public enum Action {

    FORWARD("action:forward"),
    BACKWARD("action:backward"),
//    CHANG_TRACK("action:changTrack"),
    RIGHT("action:right"),
    LEFT("action:left");
    private String action;

    private Action(String action) {
        this.action = action;
    }

    public String toString() {
        return this.action;
    }
}
