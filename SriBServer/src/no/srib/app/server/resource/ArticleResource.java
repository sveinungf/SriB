package no.srib.app.server.resource;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.ArticleDAO;
import no.srib.app.server.model.json.Article;

@Path("news")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class ArticleResource {

    @EJB
    private ArticleDAO articleDAO;

    @GET
    public List<Article> getArticles(
            @DefaultValue("10") @QueryParam("c") final int count,
            @DefaultValue("false") @QueryParam("content") final boolean content,
            @QueryParam("q") final String query) {

        if (count <= 0) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        List<Article> list = null;

        try {
            if (query == null || query.isEmpty()) {
                list = articleDAO.getRecentArticles(count);
            } else {
                list = articleDAO.search(query, count);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || list.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        } else if (!content) {
            for (Article article : list) {
                article.setContentDisplay(null);
            }
        }

        return list;
    }
}
