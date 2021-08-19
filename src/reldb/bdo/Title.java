package reldb.bdo;

import java.util.ArrayList;
import java.util.List;

public class Title {
    private Integer id;
    private String title;
    private String imdb_index;
    private Integer production_year;
    private Integer imdb_id;
    private String phonetic_code;
    private Integer season_nr;
    private Integer episode_nr;
    private String series_years;
    private String md5sum;
    private Integer kind_id;
    private Integer episode_of_id;
    private String kind;
    private String episodeOf;
    private String rating;
    private String votes;
    private List<String> fskGermany;
    private List<TitlePlot> plot;
    private List<String> genres;
    private List<String> books;
    private List<String> budgets;
    private List<String> taglines;
    private List<TitleRuntime> runtimes;
    private List<String> releaseDatesGermany;
    
    public Title(){
        genres = new ArrayList<>();
        books = new ArrayList<>();
        budgets = new ArrayList<>();
        runtimes = new ArrayList<>();
        plot = new ArrayList<>();
        fskGermany = new ArrayList<>();
        taglines = new ArrayList<>();
        releaseDatesGermany = new ArrayList<>();
    }
        
    public Title(
            Integer id,
            String title,
            String imdb_index,
            Integer production_year,
            Integer imdb_id,
            String phonetic_code,
            Integer season_nr,
            Integer episode_nr,
            String series_years,
            String md5sum,
            Integer kind_id,
            Integer episode_of_id,
            String kind
    ) {
        this.id = id;
        this.title = title;
        this.imdb_index = imdb_index;
        this.production_year = production_year;
        this.imdb_id = imdb_id;
        this.phonetic_code = phonetic_code;
        this.season_nr = season_nr;
        this.episode_nr = episode_nr;
        this.series_years = series_years;
        this.md5sum = md5sum;
        this.kind_id = kind_id;
        this.episode_of_id = episode_of_id;
        this.kind = kind;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdb_index() {
        return imdb_index;
    }

    public void setImdb_index(String imdb_index) {
        this.imdb_index = imdb_index;
    }

    public Integer getProduction_year() {
        return production_year;
    }

    public void setProduction_year(Integer production_year) {
        this.production_year = production_year;
    }

    public Integer getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(Integer imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getPhonetic_code() {
        return phonetic_code;
    }

    public void setPhonetic_code(String phonetic_code) {
        this.phonetic_code = phonetic_code;
    }

    public Integer getSeason_nr() {
        return season_nr;
    }

    public void setSeason_nr(Integer season_nr) {
        this.season_nr = season_nr;
    }

    public Integer getEpisode_nr() {
        return episode_nr;
    }

    public void setEpisode_nr(Integer episode_nr) {
        this.episode_nr = episode_nr;
    }

    public String getSeries_years() {
        return series_years;
    }

    public void setSeries_years(String series_years) {
        this.series_years = series_years;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public Integer getKind_id() {
        return kind_id;
    }

    public void setKind_id(Integer kind_id) {
        this.kind_id = kind_id;
    }

    public Integer getEpisode_of_id() {
        return episode_of_id;
    }

    public void setEpisode_of_id(Integer episode_of_id) {
        this.episode_of_id = episode_of_id;
    }
    
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    
    
    public String getEpisodeOf() {
        return episodeOf;
    }

    public void setEpisodeOf(String episodeOf) {
        this.episodeOf = episodeOf;
    }
    
    
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    
    
    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
    
    
    public List<String> getFskGermany() {
        return fskGermany;
    }

    public void setFskGermany(List<String> fskGermany) {
        this.fskGermany = fskGermany;
    }
    
    
    public List<TitlePlot> getPlot() {
        return plot;
    }

    public void setPlot(List<TitlePlot> plot) {
        this.plot = plot;
    }
    
    
    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    
    
    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
    
    
    public List<String> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<String> budgets) {
        this.budgets = budgets;
    }

    
    public List<String> getTaglines() {
        return taglines;
    }

    public void setTaglines(List<String> taglines) {
        this.taglines = taglines;
    }
    
    
    public List<TitleRuntime> getRuntimes() {
        return runtimes;
    }

    public void setruntimes(List<TitleRuntime> runtimes) {
        this.runtimes = runtimes;
    }
    
    
    public List<String> getReleaseDatesGermany() {
        return releaseDatesGermany;
    }

    public void setReleaseDatesGermany(List<String> releaseDatesGermany) {
        this.releaseDatesGermany = releaseDatesGermany;
    }
    
    

    public String toString() {
        return "ID: " + this.getId() + "\n" +
                "Title: " + this.getTitle() + "\n" +
                "Imdb Index: " + this.getImdb_index() + "\n" +
                "Production Year: " + this.getProduction_year() + "\n" +
                "Imdb ID: " + this.getImdb_id() + "\n" +
                "Phonetic Code: " + this.getPhonetic_code() + "\n" +
                "Season Nr.: " + this.getSeason_nr() + "\n" +
                "Episode Nr.: " + this.getEpisode_nr() + "\n" +
                "Series Years: " + this.getSeries_years() + "\n" +
                "MD5Sum: " + this.getMd5sum() + "\n" +
                "Kind ID: " + this.getKind_id() + "\n" +
                "Episode of ID: " + this.getEpisode_of_id() + "\n" +
                "Kind: " + this.getKind();
    }
    
    
}
