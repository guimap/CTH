/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.repository;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import model.Carro;

/**
 *
 * @author Guima
 */
public class RepositoryCarro extends Repository<model.Carro>{
    public List<Carro> pegarTodos() throws SQLException {
    	        List<Carro> lista = null;
    	        Session session = ConnectionFactoryConfig.openManger().openSession();//getCurrentSession();
    	        
    	        session.createCriteria(Carro.class);
    	        Criteria c = session.createCriteria(Carro.class);
    	        lista = c.list();
    	        
    	        session.close();
    	        return lista;
    	    
    }
}
