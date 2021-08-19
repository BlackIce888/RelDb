/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reldb.bdo;

/**
 *
 * @author Kian Schmalenbach
 */
public class MovieCompany {

   private Integer id;
    private String note;
    private Integer company_id;
    private Integer company_type_id;
    private Integer movie_id;
    
    private String name;
    
    private String kind;

    public MovieCompany(){
    
    }
    
    public MovieCompany(Integer id, String note, Integer company_id, Integer company_type_id, Integer movie_id) {
        this.id = id;
        this.note = note;
        this.company_id = company_id;
        this.company_type_id = company_type_id;
        this.movie_id = movie_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public Integer getCompany_type_id() {
        return company_type_id;
    }

    public void setCompany_type_id(Integer company_type_id) {
        this.company_type_id = company_type_id;
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getKind(){
        return kind;
    }
    
    public void setKind(String kind){
        this.kind = kind;
    }
    
}
