package reldb.bdo;

public class CharName {
    private Integer id;
    private String name;
    private String imdb_index;
    private Integer imdb_id;
    private String name_pcode_nf;
    private String surname_pcode;
    private String md5sum;

    private String actorName;
    private String movieName;
    private Integer movieYear;
    
    public CharName(){}

    public CharName(Integer id, String name, String imdb_index, Integer imdb_id, String name_pcode_nf, String surname_pcode, String md5sum) {
        this.id = id;
        this.name = name;
        this.imdb_index = imdb_index;
        this.imdb_id = imdb_id;
        this.name_pcode_nf = name_pcode_nf;
        this.surname_pcode = surname_pcode;
        this.md5sum = md5sum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImdb_index() {
        return imdb_index;
    }

    public void setImdb_index(String imdb_index) {
        this.imdb_index = imdb_index;
    }

    public Integer getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(Integer imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getName_pcode_nf() {
        return name_pcode_nf;
    }

    public void setName_pcode_nf(String name_pcode_nf) {
        this.name_pcode_nf = name_pcode_nf;
    }

    public String getSurname_pcode() {
        return surname_pcode;
    }

    public void setSurname_pcode(String surname_pcode) {
        this.surname_pcode = surname_pcode;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }
    
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
    
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    
    
    public Integer getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(int movieYear) {
        this.movieYear = movieYear;
    }
}
