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
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.mgt.QueryEngineInfo;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String[] url = {"http://cdhekne.github.io/khan_acad.ttl", "http://cdhekne.github.io/tuts_video.ttl", "http://cdhekne.github.io/tuts_tutorials.ttl", "http://cdhekne.github.io/ocw.ttl"};
	private static final String db = "http://demo.openlinksw.com/sparql";
	//	private final OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null);
//	private QueryEngineInfo qi = new QueryEngineInfo();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchParameter;

		searchParameter = request.getParameter("searchBox");

		if(searchParameter==null) {
			// Send back to home page
			response.sendRedirect("index.html");
			return;
		}
		else {
			for(int i=0;i<url.length;i++){
				OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null);
				m.removeAll();
				m.read(url[i], "TURTLE");

				String queryString = "PREFIX edu: <http://www.semanticweb.org/cdhekne/ontologies/2015/10/untitled-ontology-8#>\n" +
						"SELECT DISTINCT ?name ?courseProvider ?courseLink ?desc\n" +
						"WHERE {"+
						"?course edu:courseName ?name ; edu:courseProvider ?courseProvider ; edu:courseLink ?courseLink ; "
						+ "edu:courseDescription ?desc.\n" +
						"FILTER regex(?desc , \""+searchParameter+"\", \"i\")\n" +
						"}";
				//				System.out.println(queryString);
				Query query = QueryFactory.create(queryString) ;
				QueryExecution qexec = QueryExecutionFactory.sparqlService(db, query);
				ResultSet rs = qexec.execSelect();
				while(rs.hasNext()){
					QuerySolution qs = rs.nextSolution();
					Literal name = qs.getLiteral("name");
					Literal courseProvider = qs.getLiteral("courseProvider");
					Literal courseLink = qs.getLiteral("courseLink");
					Literal desc = qs.getLiteral("desc");
					System.out.println(name + "\n" + courseLink + "\n" + courseProvider + "\n" + desc +"\n\n");
					//					System.out.println(qs.get("courseProvider"));
					//					System.out.println(qs.get("courseLink"));
					//					System.out.println(qs.get("desc"));
				}
				qexec.close();
				m.close();
			}

			return;
		}
	}
}
