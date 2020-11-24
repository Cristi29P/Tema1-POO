package database;
import actor.Actor;
import java.util.ArrayList;


public final class ActorDB {
    private ArrayList<Actor> actors;

    public ActorDB(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }
}
