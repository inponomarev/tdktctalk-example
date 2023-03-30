package io.synthesized.jdbcdemo.service.dao.initscript;

import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.service.TalkService;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestConfig.class)
public class InitScriptTalkServiceTest {
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private TalkService service;

    @Test
    void rejectInReviewWithFeedback() {
        //A talk with a feedback
        int id = 1001;
        //Act
        service.changeStatus(id, Status.REJECTED);
        //Assert
        assertThat(talkDao.getTalkById(id).getStatus())
                .isEqualTo(Status.REJECTED);
    }

    @Test
    void doNotRejectInReviewWithoutFeedback() {
        //A talk without a feedback
        int id = 1002;
        //Act, Assert
        assertThatThrownBy(() -> service.changeStatus(id, Status.REJECTED))
                .hasMessageContaining("feedback");
        assertThat(talkDao.getTalkById(id).getStatus())
                .isEqualTo(Status.IN_REVIEW);
    }
}
