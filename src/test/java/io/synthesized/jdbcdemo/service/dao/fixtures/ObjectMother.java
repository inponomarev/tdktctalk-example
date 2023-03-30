package io.synthesized.jdbcdemo.service.dao.fixtures;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Speaker;
import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;

import java.util.Set;

import static java.util.Collections.singleton;

class ObjectMother {
    final static Speaker EGOROV = new Speaker(1001, "Sergey Egorov");
    final static Speaker TOLKACHEV = new Speaker(1002, "Kirill Tolkachev");
    final static Speaker BORISOV = new Speaker(1003, "Evgeny Borisov");
    final static Speaker VALEEV = new Speaker(1004, "Tagir Valeev");

    final static Conference JPOINT = new Conference(1001, "JPoint");
    final static Conference JOKER = new Conference(1002, "Joker");

    final static Talk JAVA914 = new Talk(1001,
            "Java 9-14: Small Optimizations",
            JOKER, Status.IN_REVIEW, "outdated material", singleton(VALEEV));

    final static Talk REACTIVEORNOT = new Talk(1002,
            "Reactive, or not reactive: that is the question",
            JPOINT, Status.IN_REVIEW, "", Set.of(BORISOV, TOLKACHEV));

    final static Talk SIMPSON = new Talk(1003,
            "Don't be Homer Simpson to your Reactor!",
            JPOINT, Status.IN_REVIEW, "", singleton(EGOROV));

    final static Talk TESTCONTAINERS = new Talk(1004,
            "Testcontainers: Year later",
            JOKER, Status.IN_REVIEW, "", singleton(EGOROV));

    static Set<Talk> jpointTalks() {
        return Set.of(REACTIVEORNOT, SIMPSON);
    }

    static Talk talkWithFeedback() {
        return JAVA914;
    }

    static Talk talkWithoutFeedback() {
        return REACTIVEORNOT;
    }
}
