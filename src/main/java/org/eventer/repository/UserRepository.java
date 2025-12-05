package org.eventer.repository;

import org.eventer.entity.User;
import org.eventer.exceptions.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findById(Long id) {
        String sql = "select * from users where id = ?";

      try{
          User user = jdbc.queryForObject(sql,(rs,rowNum) ->{

              User u = new User();
              u.setId(rs.getLong("id"));
              u.setUserName(rs.getString("username"));
              u.setEmail(rs.getString("email"));
              u.setPassword(rs.getString("password"));
              u.setRole(rs.getString("role"));
              u.setGroupId(rs.getLong("group_id"));

              return u;
                  },id);

          return Optional.ofNullable(user);
      } catch (EmptyResultDataAccessException e) {
          return Optional.empty();
      }
    }

    public List<User> findAll() {
        String sql = "select * from users";

        try{
            List<User> users = jdbc.query(sql,(rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUserName(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setGroupId(rs.getLong("group_id"));

                return u;
            });

            return users;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to fetch users",e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "select * from users where email = ?";

        try{
            User user = jdbc.queryForObject(sql,(rs, rowNum) ->{
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUserName(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setGroupId(rs.getLong("group_id"));

                return u;
            },email);

            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<User> findByGroupId(Long groupId) {
        String sql = "select * from users where group_id = ?";

        try{
            List<User> list = jdbc.query(sql,(rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUserName(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setGroupId(rs.getLong("group_id"));

                return u;
            },groupId);

            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to fetch users",e);
        }
    }

    @Transactional
    public Long save(User user) {
        String sql = "INSERT INTO users (username,email,password,role,group_id) VALUES (?,?,?,?,?) RETURNING id";

        try{
            Long generatedId = jdbc.queryForObject(sql,Long.class,user.getUserName(),user.getEmail(),user.getPassword(),user.getRole(),user.getGroupId());

            if(generatedId == null){
                throw new RuntimeException("Failed to get generated ID");
            }
            return generatedId;
        } catch (DataAccessException e) {
            throw new RuntimeException("Fail with adding user ",e);
        }
    }

    @Transactional
    public void update(Long id,User user) {
        String sql = "UPDATE users SET username=?,email=?,password=?,role=?,group_id=? WHERE id=?";

        try{
            int rowsAffected = jdbc.update(sql,user.getUserName(),user.getEmail(),user.getPassword(),user.getRole(),user.getGroupId(),id);

            if(rowsAffected == 0){
                throw new EntityNotFoundException("User not found");
            }

        }catch (DataAccessException e) {
            throw new RuntimeException("Fail with updating user ",e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try{
            int rowsAffected = jdbc.update(sql,id);

            if(rowsAffected == 0){
                throw new EntityNotFoundException("User not found with id " + id);
            }

        }catch (DataAccessException e) {
            throw new RuntimeException("Fail with deleting user ",e);
        }
    }

    @Transactional
    public void updatePassword(Long id, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE id=?";

        try{
            int res = jdbc.update(sql,newPassword,id);

            if(res == 0){
                throw new EntityNotFoundException("User not found with id " + id);
            }

        }catch(DataAccessException e){
            throw new RuntimeException("Fail with updating password ",e);
        }
    }

    @Transactional
    public void updateRole(Long id, String role) {
        String sql = "UPDATE users SET role=? WHERE id=?";

        try{
            int rowsAffected = jdbc.update(sql,role,id);

            if(rowsAffected == 0){
                throw new EntityNotFoundException("User not found with id " + id);
            }

        } catch(DataAccessException e){
            throw new RuntimeException("Fail with updating role ",e);
        }
    }

}
