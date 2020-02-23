package spring.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.scheduler.entity.EmailMessage;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Integer> {

    Optional<EmailMessage> findByEmail(String email);

    Optional<EmailMessage> findEmailMessageByQuickAccessCode(String quickAccessCode);

    @Query(value = "SELECT \n" +
            "    id,\n" +
            "    email,\n" +
            "    subject,\n" +
            "    text_message,\n" +
            "    date_created,\n" +
            "    sent,\n" +
            "    quick_access_code\n" +
            "FROM\n" +
            "	email_messages\n" +
            "WHERE\n" +
            "	sent = 0",
            nativeQuery = true)
    List<EmailMessage> findNotSent();
}
