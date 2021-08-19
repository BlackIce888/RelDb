/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class RentTitle {
    
    private Integer id;
    private Title title;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private String note;
    private Integer ratingId;
    private Double rating;
    private String ratingNote;
    
    public RentTitle(){
           title = new Title();
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return this.id;
    }
    
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }
    
    public void setRentDate(LocalDate rentDate){
        this.rentDate = rentDate;
    }
    
    public LocalDate getRentDate(){
        return this.rentDate;
    }
    
    public void setReturnDate(LocalDate returnDate){
        this.returnDate = returnDate;
    }
    
    public LocalDate getReturnDate(){
        return this.returnDate;
    }
    
    public void setNote(String note){
        this.note = note;
    }
    
    public String getNote(){
        return this.note;
    }
    
    
    public void setRatingId(Integer ratingId){
        this.ratingId = ratingId;
    }
    
    public Integer getRatingId(){
        return this.ratingId;
    }
    
    public void setRating(Double rating){
        this.rating = rating;
    }
    
    public Double getRating(){
        return this.rating;
    }
    
    
    public void setRatingNote(String ratingNote){
        this.ratingNote = ratingNote;
    }
    
    public String getRatingNote(){
        return this.ratingNote;
    }
    
    public String getTitleName(){
        return title.getTitle();
    }
    
}
