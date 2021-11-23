package main.standard.entities;

import main.standard.model.EntityID;

public abstract class StandardEntity extends AbstractEntity{

    public StandardEntity(EntityID id) {
        super(id);
    }
    public abstract StandardEntityURN getStandardURN();

    public final String getURN() {
        return this.getStandardURN().toString();
    }
}
