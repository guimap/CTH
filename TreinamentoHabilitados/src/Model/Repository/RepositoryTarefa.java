package Model.Repository;

import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import Model.Tarefa;

public class RepositoryTarefa extends Repository<Model.Tarefa> {
	public java.util.List<Model.Tarefa> getAllTarefasToday(java.time.LocalDateTime dt){
		try{
			EntityManager em = Model.Repository.ConnectionFactoryRepository.getManager();
			Session s =(Session) em.getDelegate();
			java.util.Date date = new java.util.Date(dt.getYear()-1900,dt.getMonthValue()-1 , dt.getDayOfMonth());
			Criteria filtro = s.createCriteria(Tarefa.class);
			filtro.add(Restrictions.eq("dataCompromisso", date));
			java.util.List<Tarefa> list = filtro.list();
			System.out.println(date);
			System.out.println("aospdksapokdsao");
			System.out.println(list);
			return list;
			
		}catch(Exception er){
			er.printStackTrace();
			System.out.println("error?");
			return null;
		}
	}
	
	public java.util.List<Model.Tarefa> getAllTarefasToday(java.util.Date dt){
		try{
			EntityManager em = Model.Repository.ConnectionFactoryRepository.getManager();
			Session s =(Session) em.getDelegate();
			java.util.Date date = new java.util.Date(dt.getYear(),dt.getMonth(),dt.getDate());
			Criteria filtro = s.createCriteria(Tarefa.class);
			filtro.add(Restrictions.eq("dataCompromisso", date));
			java.util.List<Tarefa> list = filtro.list();
			System.out.println(date);
			System.out.println("aospdksapokdsao");
			System.out.println(list);
			return list;
			
		}catch(Exception er){
			er.printStackTrace();
			System.out.println("error?");
			return null;
		}
	}
}
