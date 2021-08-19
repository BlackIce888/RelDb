/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.data;

import java.util.List;
import reldb.bdo.MovieCompany;

/**
 *
 * @author Rainer Stoermer
 */
public interface MovieCompanyDAO {

    public List<MovieCompany> getMovieCompaniesByTitleId(Integer titleId);

    public List<MovieCompany> getMovieCompaniesByKeyword(String keywords, int match);

}
