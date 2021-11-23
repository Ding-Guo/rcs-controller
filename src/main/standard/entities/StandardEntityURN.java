package main.standard.entities;

public enum StandardEntityURN {
//    WORLD("urn:rescuecore2.standard:entity:world"),
    ROAD("urn:main.standard.entities:road"),
    ROLLER("urn:main.standard.entities:roller"),
    PAVER("urn:main.standard.entities:paver"),
    RIGHT_TRACK("urn:main.standard.entities:rightTrack"),
    LEFT_TRACK("urn:main.standard.entities:leftTrack");
    private String urn;

    private StandardEntityURN(String urn) {
        this.urn = urn;
    }

    public String toString() {
        return this.urn;
    }

    public static StandardEntityURN fromString(String s) {
        StandardEntityURN[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            StandardEntityURN next = var1[var3];
            if (next.urn.equals(s)) {
                return next;
            }
        }

        throw new IllegalArgumentException(s);
    }
}