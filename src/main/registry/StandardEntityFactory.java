package main.registry;

import main.standard.entities.Road;
import main.standard.entities.Roller;
import main.standard.entities.StandardEntity;
import main.standard.entities.StandardEntityURN;
import main.standard.model.EntityID;

public class StandardEntityFactory {
    public static final StandardEntityFactory INSTANCE = new StandardEntityFactory();

    private StandardEntityFactory() {
    }

    public static StandardEntity makeEntity(StandardEntityURN urn, EntityID id) {
        switch(urn) {
            case ROAD:
                return new Road(id);
            case ROLLER:
                return new Roller(id);
            default:
                throw new IllegalArgumentException("Unrecognised entity urn: " + urn);
        }
    }
}
