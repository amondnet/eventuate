package net.amond.eventuate.messaging;

/**
 * Created by amond on 17. 3. 15.
 *
 * @since 1.0.1
 *
 * Exposes the property names of standard metadata added to all messages going through the bus
 */

public class StandardMetadata {
  /**
   * A event message.
   */
  public static final String EVENT_KIND = "event";
  /**
   * A command message.
   */
  public static final String COMMAND_KIND = "command";
  /**
   *
   */
  public static final String KIND = "kind";

  /**
   * Identifier of the object that originated the event, if any.
   */
  public static final String SOURCE_ID = "sourceId";

  /**
   * The simple assembly name of the message payload ( i.e. event or command ).
   */
  public static final String NAME = "name";

  /**
   * The namespace of the message payload ( i.e. event or command ).
   */
  public static final String NAMESPACE = "namespace";

  /**
   * The full type name of the message payload ( i.e. event or command ).
   */
  public static final String CANONICAL_NAME = "canonicalName";

  /**
   * The simple type name (without the namespace) of the message payload ( i.e. event or command )
   */
  public static final String TYPE_NAME = "typeName";

  /**
   * The name of the entity that originated this message.
   */
  public static final String SOURCE_TYPE = "sourceType";
}
