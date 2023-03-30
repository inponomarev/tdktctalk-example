package io.synthesized.jdbcdemo.service.dao.fixtures;

import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.dao.ConferenceDao;
import io.synthesized.jdbcdemo.service.dao.SpeakerDao;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.BORISOV;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.EGOROV;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.JAVA914;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.JOKER;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.JPOINT;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.REACTIVEORNOT;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.SIMPSON;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.TESTCONTAINERS;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.TOLKACHEV;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.VALEEV;
import static io.synthesized.jdbcdemo.service.dao.fixtures.ObjectMother.jpointTalks;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FixturesTalkDaoTest {
    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private SpeakerDao speakerDao;

    @Autowired
    private TalkDao talkDao;

    @BeforeEach
    void prepareData() throws SQLException {
        conferenceDao.saveConferences(List.of(JPOINT, JOKER));
        speakerDao.saveSpeakers(List.of(BORISOV, TOLKACHEV, VALEEV, EGOROV));
        talkDao.insertTalks(List.of(JAVA914, REACTIVEORNOT, SIMPSON, TESTCONTAINERS));
    }

    @Test
    void getTalksByConference(){
        Set<Talk> talks = talkDao.getTalksByConference(JPOINT);
        assertThat(talks).isEqualTo(jpointTalks());
    }

    @Test
    void getTalksByConferenceApproval(){
        List<Talk> talks = new ArrayList<>(talkDao.getTalksByConference(ObjectMother.JPOINT));
        //So that the result will be consistent
        talks.sort(Comparator.comparing(Talk::getId));
        JsonApprovals.verifyAsJson(talks);
    }
}
