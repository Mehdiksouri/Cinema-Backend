package org.sid.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.PlaceRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.SeanceRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Place;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Seance;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
		@Autowired
		private VilleRepository villeRepository;
		@Autowired
		private CinemaRepository cinemaRepository;
		@Autowired
		private SalleRepository salleRepository;
		@Autowired
		private PlaceRepository placeRepository;
		@Autowired
		private SeanceRepository seanceRepository;
		@Autowired
		private FilmRepository filmRepository;
		@Autowired
		private ProjectionRepository projectionRepository;
		@Autowired
		private CategorieRepository categorieRepository;
		@Autowired
		private TicketRepository ticketRepository;
		
	@Override
	public void initVilles() {
		 Stream.of("casa","taza","rabat","kech","tata").forEach(nameVille->{
			Ville ville=new Ville();
			ville.setName(nameVille);
			
			villeRepository.save(ville);
			
		 });
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("mega","imax","fonoun","chahrazad","DAOLIZ").forEach(nameCinema->{
				
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
				
			});
		});
		
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNombreSalles();i++) {
				Salle salle=new Salle();
				salle.setName("salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(20+(int)(Math.random()*10));
				salleRepository.save(salle);
			}
		});
		
	}

	
	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for (int i = 0; i < salle.getNombrePlace(); i++) {
				Place place =new Place();
				place.setNumero(i+4);
				place.setSalle(salle);
				
				placeRepository.save(place);
				
			}
		});
		
	}
	
	
	@Override
	public void initSeances() {
		DateFormat dateFomat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","9:15","17:00","19:00","23:00").forEach(s->{
			Seance seance=new Seance();
			try {
				seance.setHeureDebut(dateFomat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}


	@Override
	public void initCtegories() {
		Stream.of("actions","histoire","fiction","drama").forEach(cat->{
			Categorie categorie=new Categorie();
			categorie.setName(cat);
	categorieRepository.save(categorie);
			
		});
		
	}

	@Override
	public void initFilms() {
		double[] durees=new double[] {1,1.5,2,2.5,3};
		List<Categorie> categories=categorieRepository.findAll();
		Stream.of("Spider-Men","Vikings","Le parrain","le Seigneur des anneaux","La ligne Verte","Gme Of Trones","Cat Women","Mawkli","Ozark","limetless","Green Book").forEach(f->{
			Film film = new Film();
			film.setTitre(f);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(f.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
			
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices=new double[] {
				30,50,60,70,90,100
		};
	villeRepository.findAll().forEach(ville->{
		ville.getCinema().forEach(cinema->{
			cinema.getSalle().forEach(salle->{
				filmRepository.findAll().forEach(film->{
					seanceRepository.findAll().forEach(seance->{
						Projection projection =new Projection();
						projection.setDateProejction(new Date());
						projection.setFilm(film);
						projection.setPrix(prices[new Random().nextInt(prices.length)]);
						projection.setSalle(salle);
						projection.setSeance(seance);
						projectionRepository.save(projection);
					});
				});
			});
		});
	});
		
	}

	@Override
	public void initTickets() {
			projectionRepository.findAll().forEach(p->{
				 p.getSalle().getPalces().forEach(place->{
					 Ticket ticket = new Ticket();
					 ticket.setPlace(place);
					 ticket.setPrix(p.getPrix());
					 ticket.setProjection(p);
					 ticket.setReserve(false);
					 ticketRepository.save(ticket);
				 });
			});
		
	}

}
