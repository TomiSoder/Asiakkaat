package test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import model.Asiakas;
import model.dao.Dao;

@TestMethodOrder(OrderAnnotation.class)
class JUnit_testaa_autot {

	@Test
	@Order(1) 
	public void testPoistaKaikkiAsiakkaat() {
		//Poistetaan kaikki asiakkaat
		Dao dao = new Dao();
		dao.poistaKaikkiAsiakkaat("testipassu");
		ArrayList<Asiakas> asiakkaat = dao.listaaKaikki();
		assertEquals(0, asiakkaat.size());
	}
	
	@Test
	@Order(2) 
	public void testLisaaAsiakas() {		
		//Tehd‰‰n muutama uusi testiauto
		Dao dao = new Dao();
		Asiakas asiakas_1 = new Asiakas(999,"Seppo", "Hovi", "050-333333", "sepi@hovi.com");
		Asiakas asiakas_2 = new Asiakas(999,"Matti", "Hovi", "050-222222", "matti@hovi.com");
		Asiakas asiakas_3 = new Asiakas(999,"Pekka", "Hovi", "050-111111", "pekka@hovi.com");
		Asiakas asiakas_4 = new Asiakas(999,"Lauri", "Hovi", "050-555555", "lauri@hovi.com");
		assertEquals(true, dao.lisaaAsiakas(asiakas_1));
		assertEquals(true, dao.lisaaAsiakas(asiakas_2));
		assertEquals(true, dao.lisaaAsiakas(asiakas_3));
		assertEquals(true, dao.lisaaAsiakas(asiakas_4));
	}
	
	@Test
	@Order(3) 
	public void testMuutaAsiakas() {
		//Muutetaan yht‰ autoa
		Dao dao = new Dao();
		Asiakas muutettava = dao.etsiAsiakas(1);
		muutettava.setEtunimi("MattiX");
		muutettava.setSukunimi("Mattinen");
		muutettava.setPuhelin("045-123456");
		muutettava.setSposti("sahko@posti.fi");
		dao.muutaAsiakas(muutettava, 1);	
		assertEquals("MattiX", dao.etsiAsiakas(1).getEtunimi());
		assertEquals("Mattinen", dao.etsiAsiakas(1).getSukunimi());
		assertEquals("045-123456", dao.etsiAsiakas(1).getPuhelin());
		assertEquals("sahko@posti.fi", dao.etsiAsiakas(1).getSposti());
	}
	
	@Test
	@Order(4) 
	public void testPoistaAsiakas() {
		Dao dao = new Dao();
		dao.poistaAsiakas(1);
		assertEquals(null, dao.etsiAsiakas(1));
	}

}
