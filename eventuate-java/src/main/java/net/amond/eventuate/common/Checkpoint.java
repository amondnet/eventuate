package net.amond.eventuate.common;

/**
 * Created by amond on 17. 6. 15.
 *
 * When a microservice subscribes to the stream, using checkpoints, it creates a new checkpoint
 * stream for itself. The checkpoint stream is explicitly managed by our client as a service reads
 * and processes events from the stream. The checkpoint streams also follow a convention that we
 * set, where they are named according to the stream name and the microservice name:
 * StreamName_MicrosreviceName_checkpoints. Consider a microservice named EmailOnOrderCreated that
 * listens to the $et-OrderCreated stream. The microservice would subscribe to the stream starting
 * with the last position found in the etOrderCreated_EmailOnOrderCreated_checkpoints stream.
 *
 * @author amond
 */
public class Checkpoint {
  private int position;

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
