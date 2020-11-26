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


public final class ExecStarter {
    private final Writer fileWriter;
    private final JSONArray arrayResult;
    private final Input input;

    public ExecStarter(final Writer fileWriter, final JSONArray arrayResult, final Input input) {
        this.fileWriter = fileWriter;
        this.arrayResult = arrayResult;
        this.input = input;
    }

    /**
     * Starts the main execution of tasks on all databases
     * @throws IOException
     */
    public void startExecution() throws IOException {
        CreatorDB creatorBazaDeDate = new CreatorDB(input);
        ActorDB actoriDB = creatorBazaDeDate.generateActorDB();
        UserDB useriDB = creatorBazaDeDate.generateUserDB();
        MovieDB filmeDB = creatorBazaDeDate.generateMovieDB();
        ShowDB serialeDB = creatorBazaDeDate.generateShowDB();

        CommandExec execComm = new CommandExec();
        QueryExec execQuery = new QueryExec();
        RecommExec execRecomm = new RecommExec();

        List<ActionInputData> actiuni = input.getCommands();

        for (ActionInputData aux: actiuni) {
            if (aux.getActionType().equals("command")) {
                if (aux.getType().equals("favorite")) {
                    execComm.addFavorites(aux.getUsername(), aux.getTitle(), useriDB.getUsers());
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execComm.getResult()));
                }
                if (aux.getType().equals("view")) {
                    execComm.addView(aux.getUsername(), aux.getTitle(), useriDB.getUsers());
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execComm.getResult()));
                }
                if (aux.getType().equals("rating")) {
                    execComm.addRating(aux.getUsername(), aux.getTitle(), useriDB.getUsers(),
                            aux.getGrade(), aux.getSeasonNumber(), filmeDB, serialeDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execComm.getResult()));
                }
            }
            if (aux.getActionType().equals("query")) {
                if (aux.getObjectType().equals("actors")) {
                    if (aux.getCriteria().equals("average")) {
                        execQuery.getAverage(aux.getNumber(), aux.getSortType(), actoriDB, filmeDB,
                                serialeDB);
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("awards")) {
                        execQuery.getByAwards(aux.getSortType(), actoriDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("filter_description")) {
                        execQuery.getByDescription(aux.getSortType(), actoriDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                }
                if (aux.getObjectType().equals("movies")) {
                    if (aux.getCriteria().equals("ratings")) {
                        execQuery.getRatedMovies(aux.getNumber(), aux.getSortType(), filmeDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("longest")) {
                        execQuery.getLongestMovies(aux.getNumber(), aux.getSortType(), filmeDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("favorite")) {
                        execQuery.getFavoriteMovies(aux.getNumber(), aux.getSortType(), filmeDB,
                                useriDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("most_viewed")) {
                        execQuery.getViewedMovies(aux.getNumber(), aux.getSortType(), filmeDB,
                                useriDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                }
                if (aux.getObjectType().equals("shows")) {
                    if (aux.getCriteria().equals("ratings")) {
                        execQuery.getRatedShows(aux.getNumber(), aux.getSortType(), serialeDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("longest")) {
                        execQuery.getLongestShows(aux.getNumber(), aux.getSortType(), serialeDB,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("favorite")) {
                        execQuery.getFavoriteShows(aux.getNumber(), aux.getSortType(), serialeDB,
                                useriDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                    if (aux.getCriteria().equals("most_viewed")) {
                        execQuery.getViewedShows(aux.getNumber(), aux.getSortType(), serialeDB,
                                useriDB, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                                execQuery.getResult()));
                    }
                }
                if (aux.getObjectType().equals("users")) {
                    execQuery.getNumberOfRatings(aux.getNumber(), aux.getSortType(), useriDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execQuery.getResult()));
                }
            }
            if (aux.getActionType().equals("recommendation")) {
                if (aux.getType().equals("standard")) {
                    execRecomm.stdRecomm(aux.getUsername(), filmeDB, serialeDB, useriDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRecomm.getResult()));
                }
                if (aux.getType().equals("best_unseen")) {
                    execRecomm.bestUnseenRecomm(aux.getUsername(), filmeDB, serialeDB,
                            useriDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRecomm.getResult()));
                }
                if (aux.getType().equals("search")) {
                    execRecomm.searchRecomm(aux.getUsername(), aux.getGenre(), filmeDB,
                            serialeDB, useriDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRecomm.getResult()));
                }
                if (aux.getType().equals("favorite")) {
                    execRecomm.favRecomm(aux.getUsername(), filmeDB, serialeDB,
                            useriDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRecomm.getResult()));
                }
                if (aux.getType().equals("popular")) {
                    execRecomm.popularRecomm(aux.getUsername(), filmeDB, serialeDB,
                            useriDB);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "",
                            execRecomm.getResult()));
                }
            }
        }
    }

    public JSONArray getArrayResult() {
        return arrayResult;
    }

}
