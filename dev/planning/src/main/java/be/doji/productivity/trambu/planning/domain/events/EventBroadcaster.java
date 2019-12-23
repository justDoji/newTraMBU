package be.doji.productivity.trambu.planning.domain.events;

import be.doji.productivity.trambu.events.Event;

public interface EventBroadcaster {

  <T extends Event> void broadcast(T event);

}
