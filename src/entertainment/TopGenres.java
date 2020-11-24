package entertainment;

public final class TopGenres {
    private String genre;
    private int popularity;

    public TopGenres(final String genre, final int popularity) {
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
}
