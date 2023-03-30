package io.synthesized.jdbcdemo.service.dao.tdk;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.dao.ConferenceDao;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class ObjectMother {
    private final ConferenceDao conferenceDao;
    private final TalkDao talkDao;

    Conference conference() {
        return conferenceDao.getConferences().iterator().next();
    }

    Talk talk(){
        return talkDao.getTalksByConference(conference()).iterator().next();
    }

    Talk talkWithoutFeedback(){
        Talk talk = talk().withStatus(Status.IN_REVIEW).withFeedback("");
        talkDao.updateTalk(talk);
        return talk;
    }

    Talk talkWithFeedback(){
        Talk talk = talk().withStatus(Status.IN_REVIEW).withFeedback("feedback");
        talkDao.updateTalk(talk);
        return talk;
    }
}
