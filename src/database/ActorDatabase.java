package database;
import actor.Actor;
import java.util.ArrayList;


public class ActorDatabase {
    private ArrayList<Actor> actors;

    public ActorDatabase(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }
}
