package main.standard.entities;

import main.standard.model.Entity;
import main.standard.model.EntityID;

public abstract class AbstractEntity implements Entity {
    protected final EntityID id;

    public AbstractEntity(EntityID id) {
        this.id = id;
    }
    public EntityID getID() {
        return this.id;
    }

}
