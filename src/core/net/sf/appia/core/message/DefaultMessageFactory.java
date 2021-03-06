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

package net.sf.appia.core.message;

/**
 * This class defines a DefaultMessageFactory. This is the Factory used by default in Appia.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class DefaultMessageFactory implements MessageFactory {

    public DefaultMessageFactory(){}
    /**
     * 
     * @see net.sf.appia.core.message.MessageFactory#newMessage()
     */
    public Message newMessage() {
        return new Message();
    }

    /**
     * 
     * @see net.sf.appia.core.message.MessageFactory#newMessage(byte[], int, int)
     */
    public Message newMessage(byte[] payload, int offset, int length) {
        return new Message(payload,offset,length);
    }

}
