package no.srib.app.server.dao.jpa;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.BackendUserDAO;
import no.srib.app.server.model.jpa.Backenduser;

@Stateless
public class BackendUserDAOImpl extends AbstractModelDAOImpl<Backenduser>
        implements BackendUserDAO {

    public BackendUserDAOImpl() {
        super(Backenduser.class);
    }

    @Override
    public Backenduser getByUserName(String username) throws DAOException {
        Backenduser result = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Backenduser> criteria = cb.createQuery(Backenduser.class);
        Root<Backenduser> backenduser = criteria.from(Backenduser.class);
        Predicate condition = cb.equal(backenduser.get("username"), username);
        criteria.where(condition);

        TypedQuery<Backenduser> query = em.createQuery(criteria);

        try {
            result = query.getSingleResult();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }
}
