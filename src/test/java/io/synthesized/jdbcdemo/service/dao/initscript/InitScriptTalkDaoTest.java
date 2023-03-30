package io.synthesized.jdbcdemo.service.dao.initscript;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Speaker;
import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static io.synthesized.jdbcdemo.domain.Status.IN_REVIEW;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
public class InitScriptTalkDaoTest {
    private static final Conference JPOINT = new Conference(1001, "JPoint");
    @Autowired
    private TalkDao talkDao;

    @Test
    void getTalksByConference() {
        Set<Talk> talks = talkDao.getTalksByConference(JPOINT);

        assertThat(talks).satisfiesExactlyInAnyOrder(
                talk -> {
                    assertThat(talk.getConference().getId()).isEqualTo(JPOINT.getId());
                    assertThat(talk.getName()).isEqualTo(
                            "Reactive, or not reactive: that is the question");
                    assertThat(talk.getSpeakers().stream().map(Speaker::getName))
                            .containsExactlyInAnyOrder("Evgeny Borisov", "Kirill Tolkachev");
                    assertThat(talk.getStatus()).isEqualTo(IN_REVIEW);
                },
                talk -> {
                    assertThat(talk.getConference().getId()).isEqualTo(JPOINT.getId());
                    assertThat(talk.getName()).isEqualTo(
                            "Don't be Homer Simpson to your Reactor!");
                    assertThat(talk.getSpeakers().stream().map(Speaker::getName))
                            .containsExactly("Sergey Egorov");
                    assertThat(talk.getStatus()).isEqualTo(IN_REVIEW);
                }
        );
    }

    @Test
    void getTalksByConferenceApproval() {
        List<Talk> talks = new ArrayList<>(talkDao.getTalksByConference(JPOINT));
        //So that the result will be consistent
        talks.sort(Comparator.comparing(Talk::getId));
        JsonApprovals.verifyAsJson(talks);
    }

}
