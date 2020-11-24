package entertainment;

public final class PopularGenres {
    private String genre;
    private int popularity;

    public PopularGenres(final String genre, final int popularity) {
        this.genre = genre;
        this.popularity = popularity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(final int popularity) {
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
