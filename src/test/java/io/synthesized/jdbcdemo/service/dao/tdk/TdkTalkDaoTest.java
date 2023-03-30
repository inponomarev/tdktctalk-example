package io.synthesized.jdbcdemo.service.dao.tdk;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TdkTalkDaoTest {
    @Autowired
    private TalkDao talkDao;

    @Autowired
    ObjectMother objectMother;

    @Test
    void getTalksByConference() {
        Conference conference = objectMother.conference();
        final Set<Talk> talks = talkDao.getTalksByConference(conference);
        assertThat(talks).allSatisfy(talk -> {
            assertThat(talk.getConference()).isEqualTo(conference);
            assertThat(talk.getSpeakers()).isNotEmpty();
            assertThat(talk.getName()).isNotBlank();
        });
    }

    @Test
    void getTalksByConferenceApproval() {
        List<Talk> talks = new ArrayList<>(talkDao.getTalksByConference(objectMother.conference()));
        //So that the result will be consistent
        talks.sort(Comparator.comparing(Talk::getId));
        JsonApprovals.verifyAsJson(talks);
    }
}
