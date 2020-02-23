package spring.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.scheduler.entity.Appointment;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO appointments (\n" +
                    "	appointment_date, appointment_time, service_type,\n" +
                    "	customer_id,\n" +
                    "	master_id\n" +
                    ") VALUES (\n" +
                    "	:date, :time, :service_type,\n" +
                    "	(SELECT\n" +
                    "		MIN(id)\n" +
                    "	FROM\n" +
                    "		users\n" +
                    "	WHERE\n" +
                    "		email = :customers_email\n" +
                    "    ),\n" +
                    "	(SELECT\n" +
                    "		a.id\n" +
                    "	FROM\n" +
                    "		(SELECT\n" +
                    "			MIN(users.id) AS id\n" +
                    "		FROM\n" +
                    "			users\n" +
                    "		WHERE\n" +
                    "			users.role = 'ROLE_MASTER'\n" +
                    "			AND users.service_type = :service_type\n" +
                    "			AND users.id NOT IN (\n" +
                    "				SELECT\n" +
                    "					appointments.master_id AS id\n" +
                    "				FROM\n" +
                    "					appointments\n" +
                    "				WHERE\n" +
                    "					appointments.appointment_date = :date\n" +
                    "					AND appointments.appointment_time = :time\n" +
                    "			)\n" +
                    "		) AS a\n" +
                    "	)\n" +
                    ")",
            nativeQuery = true)
    Integer reserveTime(@Param("customers_email") String customers_email, @Param("date") String date, @Param("time") String time, @Param("service_type") String service_type);

    @Query(value = "SELECT appointments.id,\n" +
            "    appointments.appointment_date,\n" +
            "    appointments.service_provided,\n" +
            "    appointments.service_type,\n" +
            "    appointments.appointment_time,\n" +
            "    appointments.customer_id,\n" +
            "    appointments.master_id\n" +
            "FROM\n" +
            "    appointments\n" +
            "WHERE\n" +
            "    appointment_date between :start and :end",
            nativeQuery = true)
    List<Appointment> findByPeriod(@Param("start") String strDateStart, @Param("end") String strDateEnd);
}