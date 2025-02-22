package org.toure.Repository.Implementation;

import org.toure.Model.Equipe;
import org.toure.Repository.interfaces.EquipeRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class EquipeRepositoryImpl implements EquipeRepository {

    private EntityManagerFactory entityManagerFactory;

    public EquipeRepositoryImpl() {
        entityManagerFactory = Persistence.createEntityManagerFactory("tourePU");
    }

    @Override
    public Equipe create(Equipe equipe) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(equipe);
            entityManager.getTransaction().commit();

            return equipe;
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.close();
        }
    }


    @Override
    public List<Equipe> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT e FROM Equipe e", Equipe.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Equipe equipe = entityManager.find(Equipe.class, id);
            if (equipe != null) {
                entityManager.remove(equipe);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.close();
        }
    }


    @Override
    public Equipe update(Equipe equipe, long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Equipe existingEquipe = entityManager.find(Equipe.class, id);


            existingEquipe.setNom(equipe.getNom());
            existingEquipe.setClassement(equipe.getClassement());

            entityManager.getTransaction().commit();
            return existingEquipe;

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la mise à jour de l'équipe : " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }




    @Override
    public Equipe getByName(String nom) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            return entityManager.createQuery("SELECT e FROM Equipe e WHERE e.nom = :nom", Equipe.class)
                    .setParameter("nom", nom)
                    .getSingleResult();
        } catch (Exception e) {
           throw e;
        } finally {
            entityManager.close();
        }
    }


}
