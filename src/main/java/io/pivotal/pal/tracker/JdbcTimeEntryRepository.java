package io.pivotal.pal.tracker;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneId;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;
    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTimeEntryRepository() {

    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

            KeyHolder holder = new GeneratedKeyHolder();

            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement("insert into time_entries ( project_id,user_id, date,hours) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, Math.toIntExact(timeEntry.getProjectId()));
                    ps.setInt(2, Math.toIntExact(timeEntry.getUserId()));
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());

                    return ps;
                }
            }, holder);
            timeEntry.setId( holder.getKey().intValue());
            return  timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        try
        {
            return this.jdbcTemplate.queryForObject( "select id, project_id, user_id, date, hours from time_entries" +
                    "  where id = ?",new Object[]{id}, new TimeEntryMapper());

        }
        catch (DataAccessException e){
            return null;
        }


    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        jdbcTemplate.update("update time_entries set project_id=?, user_id=?,  date=?,  hours=? where id =?",
                timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours(), id);
        timeEntry.setId(id);
        return  timeEntry;
    }

    @Override
    public void delete(Long id) {

        jdbcTemplate.update("delete from  time_entries where id =?",
        id);;

    }

    @Override
    public List<TimeEntry> list() {
        return this.jdbcTemplate.query( "select id,project_id,user_id,date,hours from time_entries", new TimeEntryMapper());
    }

    private static final class TimeEntryMapper implements RowMapper<TimeEntry> {

        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimeEntry timeEntry = new TimeEntry();
            timeEntry.setId(rs.getInt("id"));
            timeEntry.setProjectId(rs.getInt("project_id"));
            timeEntry.setUserId(rs.getInt("user_id"));
            timeEntry.setDate(rs.getDate("date").toLocalDate());
            timeEntry.setHours(rs.getInt("hours"));
            return timeEntry;
        }
    }
}
