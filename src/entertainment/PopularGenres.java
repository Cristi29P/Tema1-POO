package entertainment;

public class PopularGenres {
    private String genre;
    private int popularity;

    public PopularGenres(String genre, int popularity) {
        this.genre = genre;
        this.popularity = popularity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "PopularGenres{" +
                "genre='" + genre + '\'' +
                ", popularity=" + popularity +
                '}';
    }
}
