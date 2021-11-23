package main.standard.model;

public final class EntityID {
    private final int id;

    public EntityID(int id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o instanceof EntityID) {
            return this.id == ((EntityID)o).id;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id;
    }

    public int getValue() {
        return this.id;
    }

    public String toString() {
        return String.valueOf(this.id);
    }
}
