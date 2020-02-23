package spring.scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.scheduler.entity.Feedback;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO feedbacks\n" +
                    "	(appointment_id,\n" +
                    "    rating,\n" +
                    "    text_message)\n" +
                    "SELECT\n" +
                    "	a.id,\n" +
                    "	0,\n" +
                    "	''\n" +
                    "FROM\n" +
                    "	appointments a\n" +
                    "LEFT JOIN\n" +
                    "	feedbacks f ON f.appointment_id = a.id\n" +
                    "WHERE\n" +
                    "	a.customer_id = :customer_id\n" +
                    "	AND a.service_provided = 1\n" +
                    "	AND f.id IS NULL",
            nativeQuery = true)
    Integer createNewFeedbacksOnProvidedServicesForCustomer(@Param("customer_id") int customer_id);

    @Query(value =
            "SELECT\n" +
                    "	f.id,\n" +
                    "	f.appointment_id,\n" +
                    "	f.rating,\n" +
                    "	f.text_message\n" +
                    "FROM\n" +
                    "	feedbacks f\n" +
                    "LEFT JOIN\n" +
                    "	appointments a ON f.appointment_id = a.id\n" +
                    "WHERE\n" +
                    "	a.customer_id = :customer_id\n" +
                    "	AND f.rating = 0\n" +
                    "	AND f.text_message = ''\n",
            nativeQuery = true)
    List<Feedback> findFeedbacksToLeave(@Param("customer_id") int customer_id);

    @Query(value =
            "SELECT\n" +
                    "	f.id,\n" +
                    "	f.appointment_id,\n" +
                    "	f.rating,\n" +
                    "	f.text_message\n" +
                    "FROM\n" +
                    "	feedbacks f\n" +
                    "LEFT JOIN\n" +
                    "	appointments a ON f.appointment_id = a.id\n" +
                    "WHERE\n" +
                    "	a.customer_id = :customer_id\n" +
                    "	AND (f.rating != 0\n" +
                    "	OR f.text_message != '')",
            countQuery =
                    "SELECT\n" +
                            "	COUNT(*)\n" +
                            "FROM\n" +
                            "	feedbacks f\n" +
                            "LEFT JOIN\n" +
                            "	appointments a ON f.appointment_id = a.id\n" +
                            "WHERE\n" +
                            "	a.customer_id = :customer_id\n" +
                            "	AND (f.rating != 0\n" +
                            "	OR f.text_message != '')",
            nativeQuery = true)
    Page<Feedback> findAllLeftByCustomerId(@Param("customer_id") int customer_id, Pageable pageable);

    @Query(value =
            "SELECT\n" +
                    "	f.id,\n" +
                    "	f.appointment_id,\n" +
                    "	f.rating,\n" +
                    "	f.text_message\n" +
                    "FROM\n" +
                    "	feedbacks f\n" +
                    "LEFT JOIN\n" +
                    "	appointments a ON f.appointment_id = a.id\n" +
                    "WHERE\n" +
                    "	a.master_id = :master_id\n" +
                    "	AND (f.rating != 0\n" +
                    "	OR f.text_message != '')",
            countQuery =
                    "SELECT\n" +
                            "	COUNT(*)\n" +
                            "FROM\n" +
                            "	feedbacks f\n" +
                            "LEFT JOIN\n" +
                            "	appointments a ON f.appointment_id = a.id\n" +
                            "WHERE\n" +
                            "	a.master_id = :master_id\n" +
                            "	AND (f.rating != 0\n" +
                            "	OR f.text_message != '')",
            nativeQuery = true)
    Page<Feedback> findAllLeftForMasterId(@Param("master_id") int master_id, Pageable pageable);

    @Query(value =
            "SELECT\n" +
                    "	f.id,\n" +
                    "	f.appointment_id,\n" +
                    "	f.rating,\n" +
                    "	f.text_message\n" +
                    "FROM\n" +
                    "	feedbacks f\n" +
                    "LEFT JOIN\n" +
                    "	appointments a ON f.appointment_id = a.id\n" +
                    "WHERE\n" +
                    "	f.rating != 0\n" +
                    "	OR f.text_message != ''",
            countQuery =
                    "SELECT\n" +
                            "	COUNT(*)\n" +
                            "FROM\n" +
                            "	feedbacks f\n" +
                            "LEFT JOIN\n" +
                            "	appointments a ON f.appointment_id = a.id\n" +
                            "WHERE\n" +
                            "	f.rating != 0\n" +
                            "	OR f.text_message != ''",
            nativeQuery = true)
    Page<Feedback> findAllLeft(Pageable pageable);
}