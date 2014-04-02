package no.srib.sribapp.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.AbstractModelDAO;
import no.srib.sribapp.model.AbstractModel;

abstract class AbstractModelDAOImpl<T extends AbstractModel> implements
        AbstractModelDAO<T> {

    private static final EntityManagerFactory EMF;
    private final Class<T> TYPECLASS;
    private final String GET_ALL_NAMED_QUERY;

    static {
        EMF = Persistence.createEntityManagerFactory("heroku_test");
    }

    protected AbstractModelDAOImpl(final Class<T> typeClass) {
        this.TYPECLASS = typeClass;
        GET_ALL_NAMED_QUERY = typeClass.getSimpleName() + ".findAll";
    }

    protected EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    @Override
    public List<T> getList() throws DAOException {
        EntityManager em = getEntityManager();
        List<T> list = null;

        try {
            list = em.createNamedQuery(GET_ALL_NAMED_QUERY, TYPECLASS)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }

        return list;
    }
}
