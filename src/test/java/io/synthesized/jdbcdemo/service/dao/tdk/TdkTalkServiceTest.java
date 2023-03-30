package io.synthesized.jdbcdemo.service.dao.tdk;

import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.service.TalkService;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TdkTalkServiceTest {
    @Autowired
    TalkService service;

    @Autowired
    private TalkDao talkDao;

    @Autowired
    private ObjectMother objectMother;

    @Test
    void rejectInReviewWithFeedback() {
        int id = objectMother.talkWithFeedback().getId();
        //Act
        service.changeStatus(id, Status.REJECTED);
        //Assert
        assertThat(talkDao.getTalkById(id).getStatus())
                .isEqualTo(Status.REJECTED);
    }

    @Test
    void doNotRejectInReviewWithoutFeedback() {
        int id = objectMother.talkWithoutFeedback().getId();
        //Act, Assert
        assertThatThrownBy(() ->
                service.changeStatus(id, Status.REJECTED)).hasMessageContaining("feedback");
        assertThat(talkDao.getTalkById(id).getStatus()).isEqualTo(Status.IN_REVIEW);
    }
}
