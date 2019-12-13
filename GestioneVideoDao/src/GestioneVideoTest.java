import static org.junit.Assert.*;

import is.gestionevideo.control.GestioneVideo;
import is.gestionevideo.database.*;
import is.gestionevideo.entity.Sport;
import is.gestionevideo.entity.Video;
import is.gestionevideo.entity.VideoEvento;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.lang.Error;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GestioneVideoTest {
	
	GestioneVideo gestionevideo;
	ArrayList<Video> videotrovati;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
		Connection conn = DBManager.getConnection();
		String query = "CREATE TABLE VIDEO ( "+
						"ID  INT AUTO_INCREMENT PRIMARY KEY,"
						+ "NOME VARCHAR(30),"
						+ "DATA DATE,"
						+"SPORT VARCHAR(30),"
						+"TIPO  VARCHAR(30))";
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.executeUpdate();
		}
		query = "CREATE TABLE GIORNALISTI( "+
				"ID_VIDEO INT,"
				+ "NOME VARCHAR(30),"
				+ "COGNOME VARCHAR(30),"
				+ "PRIMARY KEY(ID_VIDEO,NOME,COGNOME))";
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.executeUpdate();
		
		}
		System.out.println("Inizializzazione Database Completata");				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Connection conn = DBManager.getConnection();
		try {
			String query = "DROP TABLE GIORNALISTI;DROP TABLE VIDEO;";
			try(PreparedStatement stmt = conn.prepareStatement(query)){
				stmt.executeUpdate();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Cancellazione tabelle completata");
	}


	

	@Before
	public void setUp() throws Exception {
		gestionevideo = new GestioneVideo();
	}

	@After
	public void tearDown() throws Exception {
		gestionevideo = null;
		Connection conn = DBManager.getConnection();
		
		
		String query = "DELETE FROM VIDEO;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.executeUpdate();
		}
		
				
		String query2 = "DELETE FROM GIORNALISTI;";

		try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
			
			stmt2.executeUpdate();
		}
		
}
	

	@Test
	public void test01RicercaPerNomeNotFound() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.APRIL,24),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		
		videotrovati = gestionevideo.ricercaVideo(null, "Samp");
		System.out.println("Video trovati = "+videotrovati.size());
		for(Video v:videotrovati) {
			System.out.println(v+"\n");
		}
		assertEquals(0,videotrovati.size());
	}
	@Test
	public void test02RicercaPerSportNotFound() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019, Month.JUNE, 13),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		
		videotrovati = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
		System.out.println("Video trovati = "+videotrovati.size());
		for(Video v : videotrovati) {
			System.out.println(v+"\n");
		}
		assertEquals(0,videotrovati.size());
		
		
	}
	@Test
	public void test03RicercaPerNomeESportNotFound() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019, Month.SEPTEMBER ,29),Sport.CALCIO);
		
		gestionevideo.caricaVideo(v1);
		
		videotrovati = gestionevideo.ricercaVideo(Sport.CALCIO,"Samp");
		
		System.out.println("Video trovati = "+videotrovati.size());
		
		for(Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		assertEquals(0,videotrovati.size());
	}
	@Test
	public void test04RicercaPerNomeTrovato() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019, Month.FEBRUARY, 21),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		
		videotrovati = gestionevideo.ricercaVideo(null, "Napoli");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for(Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		assertEquals(1,videotrovati.size());
	}
	@Test
	public void test05RicercaPerSportTrovato() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		
		gestionevideo.caricaVideo(v1);
		videotrovati = gestionevideo.ricercaVideo(Sport.CALCIO,null);
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test06RicercaPerNomeESportTrovato() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		
		gestionevideo.caricaVideo(v1);
		videotrovati = gestionevideo.ricercaVideo(Sport.CALCIO,"Napoli");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test07RicercaPerNomeNotFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		videotrovati = gestionevideo.ricercaVideo(null,"Samp");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test08RicercaPerSportNotFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		videotrovati = gestionevideo.ricercaVideo(Sport.FORMULA1,null);
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test09RicercaPerNomeESportNotFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Partita Milan-Napoli",LocalDate.of(2019, Month.MAY, 29),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(Sport.CALCIO,"Samp");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test10RicercaPerNomeOneFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Partita Milan-Napoli",LocalDate.of(2019, Month.MAY, 29),Sport.CALCIO);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(null,"Milan");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test11RicercaPerSportOneFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Gara Montecarlo",LocalDate.of(2019, Month.MAY, 29),Sport.FORMULA1);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(Sport.FORMULA1,null);
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test12RicercaPerSportENomeOneFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Gara Montecarlo",LocalDate.of(2019, Month.MAY, 29),Sport.FORMULA1);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(Sport.FORMULA1,"Montecarlo");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test13RicercaPerNomeMoreFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Gara Montecarlo",LocalDate.of(2019, Month.MAY, 29),Sport.FORMULA1);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(null,"Napoli");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test14RicercaPerSportMoreFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Gara Montecarlo",LocalDate.of(2019, Month.MAY, 29),Sport.FORMULA1);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(Sport.CALCIO,null);
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test15RicercaPerSportENomeMoreFoundPiuVideo() throws SQLException{
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",LocalDate.of(2019,Month.FEBRUARY,21),Sport.CALCIO);
		VideoEvento v2 = new VideoEvento("Partita Juventus-Napoli",LocalDate.of(2019, Month.OCTOBER, 31),Sport.CALCIO);
		VideoEvento v3 = new VideoEvento("Gara Montecarlo",LocalDate.of(2019, Month.MAY, 29),Sport.FORMULA1);
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		videotrovati = gestionevideo.ricercaVideo(Sport.CALCIO,"Napoli");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test16RicercaNomeNoEventi() throws SQLException{
		
		videotrovati = gestionevideo.ricercaVideo(null,"Samp");
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
	@Test
	public void test17RicercaNoNomeNoSportNoEventi() throws SQLException{
		
		videotrovati = gestionevideo.ricercaVideo(null,null);
		
		System.out.println("Video trovati = "+videotrovati.size());
		for (Video v: videotrovati) {
			System.out.println(v+"\n");
		}
		
	}
}
