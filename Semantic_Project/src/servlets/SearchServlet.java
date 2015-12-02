package servlets;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.mgt.QueryEngineInfo;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url = "http://cdhekne.github.io/khan_acad.ttl";
	private static final String db = "http://demo.openlinksw.com/sparql";
	private final OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null);
	private QueryEngineInfo qi = new QueryEngineInfo();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchParameter;

		searchParameter = request.getParameter("keyword");

		if(searchParameter.isEmpty()) {
			// Send back to home page
			response.sendRedirect("index.jsp");
			return;
		}
		else {
			m.read(url, "TURTLE");

			String queryString = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX mooc:<http://www.semanticweb.org/cdhekne/ontologies/2015/10/untitled-ontology-8#>\n" +
					"SELECT *\n" +
					"WHERE {"+
					"?person rdf:type mooc:Category .\n" +
					"?person mooc:categoryName ?name ." +
					"}";

			Query query = QueryFactory.create(queryString) ;
			QueryExecution qexec = QueryExecutionFactory.sparqlService(db, query);
			ResultSet rs = qexec.execSelect();
			while(rs.hasNext()){
				QuerySolution qs = rs.next();
				System.out.println(qs.get("Course"));
				System.out.println(qs.get("name"));
			}

			return;
		}
	}
}
