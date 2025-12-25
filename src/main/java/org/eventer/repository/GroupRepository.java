package org.eventer.repository;

import org.eventer.entity.Group;
import org.eventer.exceptions.DatabaseException;
import org.eventer.exceptions.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupRepository {
    private final JdbcTemplate jdbc;

    public GroupRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Group> findById(Long id) {
        String sql = "select * from groups where id = ?";

        try{
            Group group = jdbc.queryForObject(sql,(rs,rowNum)->{
                Group g = new Group();
                g.setId(rs.getLong("id"));
                g.setName(rs.getString("name"));

                return g;
            },id);

            return Optional.ofNullable(group);
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public List<Group> findAll() {
        String sql = "select * from groups";

        try{
            List<Group> groups = jdbc.query(sql,(rs,rowNum) ->{
               Group g = new Group();
               g.setId(rs.getLong("id"));
               g.setName(rs.getString("name"));

               return g;
            });

            return groups;
        }catch(DataAccessException e){
            throw new RuntimeException("Error finding groups", e);
        }
    }

    @Transactional
    public Long save(Group group) {
        String sql = "INSERT INTO groups (name) VALUES (?) RETURNING id";

        try{
            Long generatedId = jdbc.queryForObject(sql,Long.class,group.getName());

            if(generatedId == null){
                throw new DatabaseException("Fail to retrieve generated id");
            }

            return generatedId;
        }catch(DataAccessException e){
            throw new DatabaseException("Failed to save group", e);
        }
    }

    @Transactional
    public void update(Group group) {
        String sql = "UPDATE groups SET name = ? WHERE id = ?";

        try{
           int rowsAffected = jdbc.update(sql,group.getName(),group.getId());

           if(rowsAffected == 0){
               throw new EntityNotFoundException("Group not found with id " + group.getId());
           }

        }catch(DataAccessException e){
            throw new DatabaseException("Failed to update group", e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        String sql = "DELETE FROM groups WHERE id = ?";

        try{
            int rowsAffected = jdbc.update(sql,id);

            if(rowsAffected == 0){
                throw new EntityNotFoundException("Group not found with id " + id);
            }

        }catch(DataAccessException e){
            throw new DatabaseException("Failed to delete group", e);
        }
    }
}
