/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.app;

import java.util.List;
import javafx.collections.ObservableList;
import reldb.bdo.*;
import reldb.data.CharNameDAOImpl;
import reldb.data.MovieCompanyDAOImpl;
import reldb.data.PersonDAOImpl;
import reldb.data.TitleDAOImpl;


public class SearchApp {
    
    public ObservableList<Title> titleList;
    public ObservableList<Person> personList;
    public ObservableList<CharName> charNameList;
    public ObservableList<MovieCompany> companyList;
    
    
    public SearchApp(){}
    
    public void searchTitle(String keywords, int match){
        TitleDAOImpl Titles = new TitleDAOImpl();
        
        titleList = Titles.getTitlesByKeyword(keywords,match);
        
    }

    public void searchExtendedTitle(ExtendedSearch input) {
        TitleDAOImpl Titles = new TitleDAOImpl();

        titleList = Titles.getTitlesByExtendedSearch(input);
    }
    
    public void searchPerson(String keywords, int match){
        PersonDAOImpl Persons = new PersonDAOImpl();
        
        personList = Persons.getPersonsByKeyword(keywords,match);
        
    }
        
    public void searchMovieCompany(String keywords, int match){
        MovieCompanyDAOImpl Company = new MovieCompanyDAOImpl();
        
        companyList = Company.getMovieCompaniesByKeyword(keywords,match);
        
    }
     
    public void searchCharName(String keywords, int match){
        CharNameDAOImpl Names = new CharNameDAOImpl();
        
        charNameList = Names.getPersonsByKeyword(keywords,match);
        
    }
    
}
