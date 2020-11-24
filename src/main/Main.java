package main;

import action.CommandExec;
import action.QueryExec;
import action.RecommExec;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import database.ActorDB;
import database.CreatorDB;
import database.MovieDB;
import database.ShowDB;
import database.UserDB;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

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
        fileWriter.closeJSON(arrayResult);
    }
}
