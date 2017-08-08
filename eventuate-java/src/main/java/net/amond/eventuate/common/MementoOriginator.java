package net.amond.eventuate.common;

/**
 * Defines that the implementor can create memento objects (snapshots), that can be used to recreate
 * the original state.
 *
 * @author amond
 */
public interface MementoOriginator {

  /**
   * Saves the object's state to an opaque memento object (a snapshot) that can be used to restore
   * the state.
   *
   * @return An opaque memento object that can be used to restore the state.
   */
  Memento saveToMemento();
}

