package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Asiakas;
import model.dao.Dao;

@WebServlet("/asiakkaat/*")
public class Asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Asiakkaat() {
		super();
		System.out.println("Asiakkaat.Asiakkaat()");
	}

	// GET /asiakkaat/{hakusana}
	// GET /asiakkaat/haeyksi/asiakas_id
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /audi			
		System.out.println("polku: "+pathInfo);		
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat;
		String strJSON="";
		if(pathInfo==null) { //Haetaan kaikki autot 
			asiakkaat = dao.listaaKaikki();
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		//polussa on sana "haeyksi", eli haetaan yhden auton tiedot
			int asiakas_id = Integer.parseInt(pathInfo.replace("/haeyksi/", "")); //poistetaan polusta "/haeyksi/", j‰ljelle j‰‰ rekno		
			Asiakas asiakas = dao.etsiAsiakas(asiakas_id);
			JSONObject JSON = new JSONObject();
			JSON.put("asiakas_id",asiakas_id);
			JSON.put("etunimi", asiakas.getEtunimi());
			JSON.put("sukunimi", asiakas.getSukunimi());
			JSON.put("puhelin", asiakas.getPuhelin());
			JSON.put("sposti", asiakas.getSposti());	
			strJSON = JSON.toString();		
		}else{ //Haetaan hakusanan mukaiset autot
			String hakusana = pathInfo.replace("/", "");
			asiakkaat = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		}	
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		Asiakas asiakas = new Asiakas();
		asiakas.setAsiakas_id(jsonObj.getInt("asiakas_id"));
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getString("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();
		if (dao.lisaaAsiakas(asiakas)) {
			out.println("{\"response\":1}");
		} else {
			out.println("{\"response\":0}");
		}
	}

		protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("Asiakkaat.doPut()");
			JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
			int asiakas_id = jsonObj.getInt("asiakas_id");
			Asiakas asiakas = new Asiakas();
			asiakas.setEtunimi(jsonObj.getString("etunimi"));
			asiakas.setSukunimi(jsonObj.getString("sukunimi"));
			asiakas.setPuhelin(jsonObj.getString("puhelin"));
			asiakas.setSposti(jsonObj.getString("sposti"));
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Dao dao = new Dao();			
			if(dao.muutaAsiakas(asiakas, asiakas_id)){ //metodi palauttaa true/false
				out.println("{\"response\":1}");  //Asiakkaan muuttaminen onnistui {"response":1}
			}else{
				out.println("{\"response\":0}");  //Asiakkaan muuttaminen ep‰onnistui {"response":0}
			}		
		}

		//DELETE  /asiakkaat/id
		protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("Asiakkaat.doDelete()");	
			String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /ABC-222		
			System.out.println("polku: "+pathInfo);
			int poistettavaAsiakas_id = Integer.parseInt(pathInfo.replace("/", ""));
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Dao dao = new Dao();			
			if(dao.poistaAsiakas(poistettavaAsiakas_id)){ //metodi palauttaa true/false
				out.println("{\"response\":1}");  //Auton poistaminen onnistui {"response":1}
			}else{
				out.println("{\"response\":0}");  //Auton poistaminen ep‰onnistui {"response":0}
			}	
		}
}


