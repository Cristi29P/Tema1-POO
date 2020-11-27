package action;


import database.ActorDB;
import database.CreatorDB;
import database.MovieDB;
import database.ShowDB;
import database.UserDB;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.util.List;


public final class StarterExec {
    private final Writer fileWriter;
    private final JSONArray arrayResult;
    private final Input input;

    public StarterExec(final Writer fileWriter, final JSONArray arrayResult, final Input input) {
        this.fileWriter = fileWriter;
        this.arrayResult = arrayResult;
        this.input = input;
    }

    /**
     * Starts the main execution of tasks on all databases
     * @throws IOException exception
     */
    @SuppressWarnings("unchecked")
    public void startExecution() throws IOException {
        CreatorDB creatorDB = new CreatorDB(input);
        ActorDB actorsDB = creatorDB.generateActorDB();
        UserDB usersDB = creatorDB.generateUserDB();
        MovieDB moviesDB = creatorDB.generateMovieDB();
        ShowDB showsDB = creatorDB.generateShowDB();

        CommandExec execComm = new CommandExec();
        QueryExec execQuery = new QueryExec();
        RecExec execRec = new RecExec();

        List<ActionInputData> actions = input.getCommands();

        for (ActionInputData aux: actions) {
            if (aux.getActionType().equals("command")) {
                if (aux.getType().equals("favorite")) {
                    execComm.addFavorites(aux.getUsername(), aux.getTitle(), usersDB.getUsers());
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execComm.getResult()));
                }
                if (aux.getType().equals("view")) {
                    execComm.addView(aux.getUsername(), aux.getTitle(), usersDB.getUsers());
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execComm.getResult()));
                }
                if (aux.getType().equals("rating")) {
                    execComm.addRating(aux.getUsername(), aux.getTitle(), usersDB.getUsers(),
                            aux.getGrade(), aux.getSeasonNumber(), moviesDB, showsDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execComm.getResult()));
                }
            }
            if (aux.getActionType().equals("query")) {
                if (aux.getObjectType().equals("actors")) {
                    if (aux.getCriteria().equals("average")) {
                        execQuery.getAverage(aux.getNumber(), aux.getSortType(), actorsDB, moviesDB,
                                showsDB);
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("awards")) {
                        execQuery.getByAwards(aux.getSortType(), actorsDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("filter_description")) {
                        execQuery.getByDescription(aux.getSortType(), actorsDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                }
                if (aux.getObjectType().equals("movies")) {
                    if (aux.getCriteria().equals("ratings")) {
                        execQuery.getRatedMovies(aux.getNumber(), aux.getSortType(), moviesDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("longest")) {
                        execQuery.getLongestMovies(aux.getNumber(), aux.getSortType(), moviesDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("favorite")) {
                        execQuery.getFavoriteMovies(aux.getNumber(), aux.getSortType(), moviesDB,
                                usersDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("most_viewed")) {
                        execQuery.getViewedMovies(aux.getNumber(), aux.getSortType(), moviesDB,
                                usersDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                }
                if (aux.getObjectType().equals("shows")) {
                    if (aux.getCriteria().equals("ratings")) {
                        execQuery.getRatedShows(aux.getNumber(), aux.getSortType(), showsDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("longest")) {
                        execQuery.getLongestShows(aux.getNumber(), aux.getSortType(), showsDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("favorite")) {
                        execQuery.getFavoriteShows(aux.getNumber(), aux.getSortType(), showsDB,
                                usersDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("most_viewed")) {
                        execQuery.getViewedShows(aux.getNumber(), aux.getSortType(), showsDB,
                                usersDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                }
                if (aux.getObjectType().equals("users")) {
                    execQuery.getNumberOfRatings(aux.getNumber(), aux.getSortType(), usersDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execQuery.getResult()));
                }
            }
            if (aux.getActionType().equals("recommendation")) {
                if (aux.getType().equals("standard")) {
                    execRec.stdRec(aux.getUsername(), moviesDB, showsDB, usersDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRec.getResult()));
                }
                if (aux.getType().equals("best_unseen")) {
                    execRec.bestUnseenRec(aux.getUsername(), moviesDB, showsDB,
                            usersDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRec.getResult()));
                }
                if (aux.getType().equals("search")) {
                    execRec.searchRec(aux.getUsername(), aux.getGenre(), moviesDB,
                            showsDB, usersDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRec.getResult()));
                }
                if (aux.getType().equals("favorite")) {
                    execRec.favRec(aux.getUsername(), moviesDB, showsDB,
                            usersDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRec.getResult()));
                }
                if (aux.getType().equals("popular")) {
                    execRec.popularRec(aux.getUsername(), moviesDB, showsDB,
                            usersDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRec.getResult()));
                }
            }
        }
    }

    public JSONArray getArrayResult() {
        return arrayResult;
    }

}
