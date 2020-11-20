package database;

import actor.Actor;

import java.util.List;

public class ActorDatabase {
    private List<Actor> actors;

    public ActorDatabase(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return "ActorDatabase{" +
                "actors=" + actors +
                '}';
    }
}
