package org.eventer.repository;

import org.eventer.dto.EventDto;
import org.eventer.entity.Event;
import org.eventer.exceptions.DatabaseException;
import org.eventer.exceptions.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepository {
    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Event> findAll() {
        String sql = "select * from events";

        try{
            List<Event> events = jdbc.query(sql,(rs, rowNum) -> {
                Event event = new Event();
               event.setId(rs.getLong("id"));
               event.setGroupId(rs.getLong("group_id"));
               event.setTitle(rs.getString("title"));
               event.setDescription(rs.getString("description"));
               event.setStartDatetime(rs.getObject("start_datetime", LocalDateTime.class));
               event.setEndDatetime(rs.getObject("end_datetime", LocalDateTime.class));

               return event;
            });

            return events;
        } catch (DataAccessException e) {
            throw new RuntimeException("failed to get all events",e);
        }
    }

    public Optional<Event> findById(Long id) {
        String sql = "select * from events where id = ?";

        try{
            Event event = jdbc.queryForObject(sql,(rs, rowNum) -> {
                Event e = new Event();
                e.setId(rs.getLong("id"));
                e.setGroupId(rs.getLong("group_id"));
                e.setTitle(rs.getString("title"));
                e.setDescription(rs.getString("description"));
                e.setStartDatetime(rs.getObject("start_datetime", LocalDateTime.class));
                e.setEndDatetime(rs.getObject("end_datetime", LocalDateTime.class));

                return e;
            },id);

            return Optional.ofNullable(event);
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
        catch (DataAccessException e) {
            throw new RuntimeException("failed to find event",e);
        }
    }

    @Transactional
    public Long save(Event event) {
        String sql = "INSERT INTO events(group_id,title,description,start_datetime,end_datetime) VALUES(?,?,?,?,?) RETURNING id";

        try{
            Long id = jdbc.queryForObject(sql,Long.class,event.getGroupId(),event.getTitle(),event.getDescription(),event.getStartDatetime(),event.getEndDatetime());

            if(id == null){
                throw new DatabaseException("Failed to retrieve generated id for event");
            }

            return id;
        }catch(DataAccessException e){
            throw new DatabaseException("failed to save event",e);
        }
    }

    @Transactional
    public void update(Long id, Event event) {
        String sql = "UPDATE events SET group_id=?,title=?,description=?,start_datetime=?,end_datetime=? WHERE id=?";

        try{
            int rowsAffected = jdbc.update(sql,event.getGroupId(),event.getTitle()
                    ,event.getDescription(),event.getStartDatetime(),event.getEndDatetime()
                    ,id);

            if(rowsAffected == 0){
                throw new EntityNotFoundException("Event not found with id "+id);
            }

        }catch (DataAccessException e) {
            throw new RuntimeException("failed to update event",e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        String sql = "DELETE FROM events WHERE id=?";

        try{
            int rowsAffected = jdbc.update(sql,id);

            if(rowsAffected == 0){
                throw new EntityNotFoundException("Event not found with id " + id);
            }

        }catch (DataAccessException e) {
            throw new RuntimeException("failed to delete event",e);
        }
    }



}
