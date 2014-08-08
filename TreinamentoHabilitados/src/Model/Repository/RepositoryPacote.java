package model.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import model.Pacote;

public class RepositoryPacote extends Repository<Pacote>{
	
	
	public void adicionar (Pacote p ){
		Session session = ConnectionFactoryConfig.openManger().openSession();
		session.beginTransaction();
		session.persist(p);
		session.getTransaction().commit();
	}
	
	public List<Pacote> buscarTodos (){
		Session session = ConnectionFactoryConfig.openManger().openSession();
		List<Pacote> listPacote = new ArrayList<Pacote>();
		Criteria c = session.createCriteria(Pacote.class);
		listPacote = c.list();
		
		
		return listPacote;
	}
}
