package main;

import Actions.CommandExecutor;
import Actions.QueryExecutor;
import Actions.RecommendationExecutor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import database.*;
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

        //TODO add here the entry point to your implementation

        DatabaseCreator creatorBazaDeDate = new DatabaseCreator(input);

        ActorDatabase actoriBazaDeDate = creatorBazaDeDate.generateActorDatabase();
        UserDatabase useriBazaDeDate = creatorBazaDeDate.generateUserDatabase();
        MovieDatabase filmeBazaDeDate = creatorBazaDeDate.generateMovieDatabase();
        ShowDatabase serialeBazaDeDate = creatorBazaDeDate.generateShowDatabase();

        CommandExecutor executorComenzi = new CommandExecutor();
        QueryExecutor executorQuery = new QueryExecutor();
        RecommendationExecutor executorRecomandari = new RecommendationExecutor();

        List<ActionInputData> actiuni = input.getCommands();

        for(ActionInputData aux: actiuni) {
            if (aux.getActionType().equals("command")) {
                if (aux.getType().equals("favorite")) {
                    executorComenzi.addFavorite(aux.getUsername(), aux.getTitle(), useriBazaDeDate.getUsers());
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorComenzi.getCommandResult()));
                }
                if (aux.getType().equals("view")) {
                    executorComenzi.addView(aux.getUsername(), aux.getTitle(), useriBazaDeDate.getUsers());
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorComenzi.getCommandResult()));
                }
                if (aux.getType().equals("rating")) {
                    executorComenzi.addRating(aux.getUsername(), aux.getTitle(), useriBazaDeDate.getUsers(),
                            aux.getGrade(), aux.getSeasonNumber(), filmeBazaDeDate, serialeBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorComenzi.getCommandResult()));
                }
            }
            if (aux.getActionType().equals("query")) {
                if (aux.getObjectType().equals("actors")) {
                    if (aux.getCriteria().equals("average")) {
                        executorQuery.getAverage(aux.getNumber(), aux.getSortType(), actoriBazaDeDate, filmeBazaDeDate,
                                serialeBazaDeDate);
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("awards")) {
                        executorQuery.getByAwards(aux.getSortType(), actoriBazaDeDate, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("filter_description")) {
                        executorQuery.getByDescription(aux.getSortType(), actoriBazaDeDate, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));

                    }
                }

                if (aux.getObjectType().equals("movies")) {
                    if (aux.getCriteria().equals("ratings")) {
                        executorQuery.getRatedMovies(aux.getNumber(), aux.getSortType(), filmeBazaDeDate,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("longest")) {
                        executorQuery.getLongestMovies(aux.getNumber(), aux.getSortType(), filmeBazaDeDate,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("favorite")) {
                        executorQuery.getFavoriteMovies(aux.getNumber(), aux.getSortType(), filmeBazaDeDate,
                                useriBazaDeDate, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("most_viewed")) {
                        executorQuery.getViewedMovies(aux.getNumber(), aux.getSortType(), filmeBazaDeDate,
                                useriBazaDeDate, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                }

                if (aux.getObjectType().equals("shows")) {
                    if (aux.getCriteria().equals("ratings")) {
                        executorQuery.getRatedShows(aux.getNumber(), aux.getSortType(), serialeBazaDeDate,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("longest")) {
                        executorQuery.getLongestShows(aux.getNumber(), aux.getSortType(), serialeBazaDeDate,
                                aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("favorite")) {
                        executorQuery.getFavoriteShows(aux.getNumber(), aux.getSortType(), serialeBazaDeDate,
                                useriBazaDeDate, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                    if (aux.getCriteria().equals("most_viewed")) {
                        executorQuery.getViewedShows(aux.getNumber(), aux.getSortType(), serialeBazaDeDate,
                                useriBazaDeDate, aux.getFilters());
                        arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                    }
                }

                if (aux.getObjectType().equals("users")) {
                    executorQuery.getNumberOfRatings(aux.getNumber(), aux.getSortType(), useriBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorQuery.getQueryResult()));
                }
            }
            if (aux.getActionType().equals("recommendation")) {
                if (aux.getType().equals("standard")) {
                    executorRecomandari.stdRecomm(aux.getUsername(), filmeBazaDeDate, serialeBazaDeDate, useriBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorRecomandari.getRecommendResult()));
                }
                if (aux.getType().equals("best_unseen")) {
                    executorRecomandari.bestUnseenRecomm(aux.getUsername(), filmeBazaDeDate, serialeBazaDeDate,
                            useriBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorRecomandari.getRecommendResult()));
                }
                if (aux.getType().equals("search")) {
                    executorRecomandari.searchRecomm(aux.getUsername(), aux.getGenre(), filmeBazaDeDate,
                            serialeBazaDeDate, useriBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorRecomandari.getRecommendResult()));
                }
                if (aux.getType().equals("favorite")) {
                    executorRecomandari.favoriteRecomm(aux.getUsername(), filmeBazaDeDate, serialeBazaDeDate,
                            useriBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorRecomandari.getRecommendResult()));
                }
                if (aux.getType().equals("popular")) {
                    executorRecomandari.popularRecomm(aux.getUsername(), filmeBazaDeDate, serialeBazaDeDate,
                            useriBazaDeDate);
                    arrayResult.add(fileWriter.writeFile(aux.getActionId(), "", executorRecomandari.getRecommendResult()));
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
