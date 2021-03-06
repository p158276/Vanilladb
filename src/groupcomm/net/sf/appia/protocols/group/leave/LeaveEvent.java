/**
 * Appia: Group communication and protocol composition framework library
 * Copyright 2006 University of Lisbon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * Initial developer(s): Alexandre Pinto and Hugo Miranda.
 * Contributor(s): See Appia web page for a list of contributors.
 */
 package net.sf.appia.protocols.group.leave;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.protocols.group.Group;
import net.sf.appia.protocols.group.ViewID;
import net.sf.appia.protocols.group.events.GroupSendableEvent;



/**
 * Event to request that the member leaves the group.
 * <br>
 * When a member wishes to leave it should send a LeaveEvent downwards.
 * See {@link net.sf.appia.protocols.group.leave.LeaveLayer LeaveLayer} for more
 * details.
 * <br>
 * If the group is {@link net.sf.appia.protocols.group.sync.BlockOk blocked} there
 * isn't any guarantee that a leave will succeed.
 *
 * @author Alexandre Pinto
 * @version 0.1
 * @see net.sf.appia.protocols.group.leave.LeaveLayer
 */
public class LeaveEvent extends GroupSendableEvent {

  /**
   * Constructs an uninitialized <i>LeaveEvent</i>.
   */
  public LeaveEvent() {}

  /**
   * Constructs an initialized <i>LeaveEvent</i>.
   *
   * @param channel the {@link net.sf.appia.core.Channel Channel} of the Event
   * @param dir the {@link net.sf.appia.core.Direction Direction} of the Event
   * @param source the {@link net.sf.appia.core.Session Session} that is generating the Event
   * @param group the {@link net.sf.appia.protocols.group.Group Group} of the Event
   * @param view_id the {@link net.sf.appia.protocols.group.ViewID ViewID} of the Event
   * @throws AppiaEventException as the result of calling
   * {@link net.sf.appia.core.events.SendableEvent#SendableEvent(Channel,int,Session)
   * SendableEvent(Channel,Direction,Session)}
   */
  public LeaveEvent(Channel channel, int dir, Session source, Group group, ViewID view_id) throws AppiaEventException {
    super(channel,dir,source,group,view_id);
  }
}