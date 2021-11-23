package main.standard.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public interface Entity {
    EntityID getID();
    String getURN();


    Entity copy();
}
