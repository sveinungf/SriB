package no.srib.sribapp.dao.interfaces;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Backenduser;

public interface BackendUserDAO extends AbstractModelDAO<Backenduser>{
    
    /**
     * @throws DAOException 
     * Returns a backenduser from given username, or {@code null} if no backenduser match.
     * @param username
     * @return 
     * 
     */
    
    public Backenduser getByUserName(final String username) throws DAOException;

}