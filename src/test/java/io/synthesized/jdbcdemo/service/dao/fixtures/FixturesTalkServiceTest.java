package io.synthesized.jdbcdemo.service.dao.fixtures;

import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.TalkService;
import io.synthesized.jdbcdemo.service.dao.ConferenceDao;
import io.synthesized.jdbcdemo.service.dao.SpeakerDao;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.talkWithFeedback;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.talkWithoutFeedback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FixturesTalkServiceTest {
    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private TalkDao talkDao;
    @Autowired
        TalkService service;
    @Autowired
    private SpeakerDao speakerDao;

    @BeforeEach
    void prepareData() throws SQLException {
        conferenceDao.saveConferences(Stream.of(talkWithoutFeedback(), talkWithFeedback())
                .map(Talk::getConference).distinct().toList());
        speakerDao.saveSpeakers(Stream.of(talkWithoutFeedback(), talkWithFeedback())
                .map(Talk::getSpeakers).flatMap(Set::stream).distinct().toList());
        talkDao.insertTalks(List.of(talkWithoutFeedback(), talkWithFeedback()));
    }

    @Test
    void rejectInReviewWithFeedback() {
        int id = talkWithFeedback().getId();
        //Act
        service.changeStatus(id, Status.REJECTED);
        //Assert
        assertThat(talkDao.getTalkById(id).getStatus()).isEqualTo(Status.REJECTED);
    }

    @Test
    void doNotRejectInReviewWithoutFeedback() {
        int id = talkWithoutFeedback().getId();
        //Act, Assert
        assertThatThrownBy(() -> service.changeStatus(id, Status.REJECTED))
                .hasMessageContaining("feedback");
        assertThat(talkDao.getTalkById(id).getStatus())
                .isEqualTo(Status.IN_REVIEW);
    }
}
